/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * The {@link ImagesMetaData} interface contains meta data for all available
 * images.
 * 
 */
public interface ImagesMetaData {

	/**
	 * The authority portion of the URI representing this entity.
	 */
	public static final String AUTHORITY = "com.chute.android.avatarpicker.images";

	/**
	 * {@link Images} table.
	 * 
	 */
	public interface Images extends BaseColumns {

		/**
		 * Table name.
		 */
		public static final String TABLE_NAME = "Images";
		/**
		 * The URI that identifies the {@link ContentProvider}. The URI is used
		 * by Android for registering the ContentProvider as part of the
		 * application. The {@link ContentResolver} class will locate and use
		 * the ContentProvider based on the provided URI.
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/" + TABLE_NAME);

		// Columns
		/**
		 * The unique ID for a row. The ContentProvider’s URI mechanism uses 
		 * the ID to refer to saved data entities.
		 */
		public static final String _ID = BaseColumns._ID;
		public static final int ID_COLUMN = 0;

		/**
		 * Path of the image.
		 */
		public static final String FILE_PATH = "file_path";
		public static final int FILE_PATH_COLUMN = 1;

		/**
		 * Service name which the image belongs to.
		 */
		public static final String SERVICE = "service";
		public static final int SERVICE_COLUMN = 1;

		public static final String TABLE_SQL = "CREATE TABLE " + TABLE_NAME
				+ "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + FILE_PATH
				+ " TEXT," + SERVICE + " TEXT " + ");";
	}

}
