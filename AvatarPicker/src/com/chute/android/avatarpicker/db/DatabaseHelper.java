/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.db;

import com.chute.android.avatarpicker.db.ImagesMetaData.Images;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The {@link DatabaseHelper} class is implemented as a SQLiteOpenHelper
 * intended to help with creation/management of the database instance itself.
 * 
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String TAG = DatabaseHelper.class.getSimpleName();

	/**
	 * The current database name
	 */
	public static final String DATABASE_NAME = "images.db";
	/**
	 * The current version number
	 */
	public static final int DATABASE_VERSION = 2;

	/**
	 * Constructor
	 * 
	 * @param context
	 */
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Create SQLLite database
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
		createIndexes(db);
		createTestData(db);
	}

	/**
	 * Implementing the {@see #onUpgrade(SQLiteDatabase, int, int)} method and
	 * changing the version of the database, causes the database to be upgraded
	 * automatically next time the code is executed.
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion != DATABASE_VERSION) {
			dropTables(db);
			onCreate(db);
		}
	}

	/**
	 * Create tables SQL clause
	 * 
	 * @param db
	 */
	private void createTables(SQLiteDatabase db) {
		db.execSQL(Images.TABLE_SQL);
	}

	private void createIndexes(SQLiteDatabase db) {
	}

	private void dropTables(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + Images.TABLE_NAME);
	}

	private void createTestData(SQLiteDatabase db) {
	}

}
