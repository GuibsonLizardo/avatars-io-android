/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.db;

import java.util.Arrays;
import java.util.List;

import com.chute.sdk.utils.Logger;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

/**
 * {@link GenericProvider} class implements the {@link ContentProvider} class
 * with the logic for data access and update. The ContentProvider API exposes
 * several methods for inserting, updating, querying, and deleting data.
 * 
 */
public abstract class GenericProvider extends ContentProvider {

	private static String TAG = GenericProvider.class.getSimpleName();

	/**
	 * An instance of {@link SQLiteOpenHelper} class.
	 */
	private SQLiteOpenHelper dbHelper;
	/**
	 * List of indicators.
	 */
	private static List<Integer> indicators;
	/**
	 * Used to determine how to process the URI.
	 */
	private static UriMatcher uriMatcher;
	private boolean debug = true;

	public abstract void init();

	/**
	 * The {@see #onCreate()} method is called when the provider is instantiated
	 * by the {@link ContentResolver} class. It is used to bootstrap the
	 * database via the {@link DatabaseHelper} class instance db.
	 */
	@Override
	public boolean onCreate() {
		init();
		if (dbHelper != null) {
			if (debug)
				Log.i(TAG, "Database is created");
			return true;
		} else {
			if (debug)
				Log.w(TAG, "Database is NOT created");
			return false;
		}
	}

	/**
	 * The {@see #getType(Uri)} method uses the uriMatcher to lookup the MIME
	 * type for a given URI.
	 */
	@Override
	public String getType(Uri uri) {
		SqlArguments args = new SqlArguments(uri, null, null);
		if (TextUtils.isEmpty(args.where)) {
			return "vnd.android.cursor.dir/" + args.table;
		} else {
			return "vnd.android.cursor.item/" + args.table;
		}
	}

	/**
	 * The {@see #delete(Uri, String, String[])} method is used for deleting
	 * data from one or more rows from the content provider.
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
		SQLiteDatabase sqlDatabase = dbHelper.getWritableDatabase();
		int count = sqlDatabase.delete(args.table, args.where, args.args);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		if (debug)
			Logger.e(TAG, count + " row/s deleted - table:" + args.table
					+ " , selection:" + args.where + " , args:" + selectionArgs);
		return count;
	}

	/**
	 * The {@see #insert(Uri, ContentValues)} methods is used to handle requests
	 * to insert a new row.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		SqlArguments args = new SqlArguments(uri);
		SQLiteDatabase sqlDatabase = dbHelper.getWritableDatabase();
		final long rowId = sqlDatabase.insert(args.table, null, initialValues);
		if (rowId <= 0) {
			throw new SQLException("Failed to insert row into " + uri);
		}
		uri = ContentUris.withAppendedId(uri, rowId);
		getContext().getContentResolver().notifyChange(uri, null);
		if (debug)
			Logger.e(
					TAG,
					"Row inserted in table:" + args.table + " - "
							+ uri.toString());
		return uri;
	}

	/**
	 * The {@see #bulkInsert(Uri, ContentValues[])} method is used for handling
	 * requests, inserting a set of new rows, or the default implementation will
	 * iterate over the values and call <code>insert(Uri, ContentValues)</code>
	 * on each of them.
	 */
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SqlArguments args = new SqlArguments(uri);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			int numValues = values.length;
			for (int i = 0; i < numValues; i++) {
				if (db.insert(args.table, null, values[i]) < 0)
					return 0;
			}
			db.setTransactionSuccessful();
			if (debug)
				Logger.e(TAG, numValues + " rows inserted in table:"
						+ args.table);
		} finally {
			db.endTransaction();
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return values.length;
	}

	/**
	 * The {@see #query(Uri, String[], String, String[], String)} method is
	 * implemented to handle query requests from clients.
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(args.table);
		SQLiteDatabase sqlDatabase = dbHelper.getWritableDatabase();
		Cursor result = qb.query(sqlDatabase, projection, args.where,
				args.args, null, null, sortOrder);
		if (debug)
			Log.i(TAG,
					"query - table:" + args.table + " , selection:"
							+ args.where + " , args:" + selectionArgs
							+ " , sort order:" + sortOrder + " | rows : "
							+ result.getCount());
		result.setNotificationUri(getContext().getContentResolver(), uri);
		return result;
	}

	/**
	 * The {@see #update(Uri, ContentValues, String, String[])} method is
	 * implemented for handling requests to update one or more rows. The
	 * implementation should update all rows matching the selection to set the
	 * columns according to the provided values map.
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SqlArguments args = new SqlArguments(uri, selection, selectionArgs);
		SQLiteDatabase sqlDatabase = dbHelper.getWritableDatabase();
		int count = sqlDatabase.update(args.table, values, args.where,
				args.args);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		if (debug)
			Logger.e(TAG, count + " row/s updated - table:" + args.table
					+ " , selection:" + args.where + " , args:" + selectionArgs);
		return count;
	}

	/**
	 * Setter methods
	 * 
	 * @param dbHelper
	 */
	public void setDbHelper(SQLiteOpenHelper dbHelper) {
		this.dbHelper = dbHelper;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public static void setIndicators(List<Integer> indicators) {
		GenericProvider.indicators = indicators;
	}

	public static void setUriMatcher(UriMatcher uriMatcher) {
		GenericProvider.uriMatcher = uriMatcher;
	}

	public static class SqlArguments {

		public final String table;
		public final String where;
		public final String[] args;

		public SqlArguments(Uri url, String where, String[] args) {
			if (isUriInknown(url)) {
				throw new IllegalArgumentException("Unknown URI " + url);
			}
			if (url.getPathSegments().size() == 1) {
				this.table = url.getPathSegments().get(0);
				this.where = where;
				this.args = args;
			} else if (url.getPathSegments().size() != 2) {
				throw new IllegalArgumentException("Invalid URI: " + url);
			} else if (!TextUtils.isEmpty(where)) {
				throw new UnsupportedOperationException(
						"WHERE clause not supported: " + url);
			} else {
				this.table = url.getPathSegments().get(0);
				this.where = BaseColumns._ID + "=" + ContentUris.parseId(url);
				this.args = null;
			}
		}

		public SqlArguments(Uri url) {
			if (isUriInknown(url)) {
				throw new IllegalArgumentException("Unknown URI " + url);
			}
			if (url.getPathSegments().size() == 1) {
				table = url.getPathSegments().get(0);
				where = null;
				args = null;
			} else {
				throw new IllegalArgumentException("Invalid URI: " + url);
			}
		}

		private boolean isUriInknown(Uri uri) {
			boolean unknown = true;
			if (indicators == null || indicators.size() < 1) {
				unknown = false;
			} else {
				int match = uriMatcher.match(uri);
				if (indicators.contains(match)) {
					unknown = false;
				}
			}
			return unknown;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "SqlArguments [table: " + table + ", where: " + where
					+ ", args: " + Arrays.toString(args) + "]";
		}
	}
}
