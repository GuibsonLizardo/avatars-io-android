/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.util;

import java.util.List;

import android.content.ContentValues;

/**
 * The {@link SQLUtils} class is a helper class that works with SQL
 * 
 */
public class SQLUtils {

	public static String concatenateWhere(String column, Object[] ids) {
		StringBuilder where = new StringBuilder(column + " IN (");
		int n = ids.length;
		if (n > 0) {
			where.append(ids[0]);
			for (int i = 0; i < ids.length; i++) {
				where.append(",").append(ids[i]);
			}
		}
		where.append(" )");
		return where.toString();
	}

	public static String concatenateWhereLong(String column, List<Long> ids) {
		StringBuilder where = new StringBuilder(column + " IN (");
		int n = ids.size();
		if (n > 0) {
			where.append(ids.get(0));
			for (int i = 1; i < n; i++) {
				where.append(",").append(ids.get(i));
			}
		}
		where.append(" )");
		return where.toString();
	}

	public static String concatenateWhereString(String column,
			List<String> items) {
		StringBuilder where = new StringBuilder(column + " IN ('");
		int n = items.size();
		if (n > 0) {
			where.append(items.get(0));
			for (int i = 1; i < n; i++) {
				where.append("','").append(items.get(i));
			}
		}
		where.append("')");
		return where.toString();
	}

	public static String concatenateWhere(String column, String value) {
		return column + "='" + value + "'";
	}

	public static String concatenateWhere(String column, int value) {
		return column + "=" + value;
	}

	public static String concatenateWhere(String column, long value) {
		return column + "=" + value;
	}

	public static String concatenateWhere(String column, boolean value) {
		return column + "='" + value + "'";
	}

	public static ContentValues[] prepareAppCVarray(
			List<ContentValues> appContentValues) {
		ContentValues[] appscv = new ContentValues[appContentValues.size()];
		for (int i = 0; i < appContentValues.size(); i++) {
			appscv[i] = appContentValues.get(i);
		}
		return appscv;
	}
}
