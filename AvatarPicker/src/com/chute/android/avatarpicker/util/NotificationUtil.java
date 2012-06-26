/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.util;

import com.chute.android.avatarpicker.R;

import android.content.Context;
import android.widget.Toast;

/**
 * The {@link NotificationUtil} is a utility class that contains methods for
 * displaying toast messages.
 * 
 */
public class NotificationUtil {
	@SuppressWarnings("unused")
	private static final String TAG = NotificationUtil.class.getSimpleName();

	/**
	 * Default non-args constructor
	 */
	private NotificationUtil() {
	}

	/**
	 * Method used for making a toast message for a long period of time.
	 * 
	 * @param context
	 *            - The application context.
	 * @param message
	 *            - The message to be displayed.
	 */
	public static void makeToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * Method which refers to the generic method {@see #makeToast(Context,
	 * String)}.
	 * 
	 * @param context
	 *            - The application context.
	 * @param resId
	 *            - The string ID saved in resources describing the message.
	 */
	public static void makeToast(Context context, int resId) {
		makeToast(context, context.getString(resId));
	}

	/**
	 * Method which refers to the generic method {@see #makeToast(Context, int)}
	 * . It displays toast message that indicates some sort of server error
	 * occurred.
	 * 
	 * @param context
	 *            - The application context.
	 */
	public static void makeServerErrorToast(Context context) {
		makeToast(context, R.string.http_error);
	}

	/**
	 * Method which refers to the generic method {@see #makeToast(Context,
	 * String)}, indicating server error has occurred.
	 * 
	 * @param context
	 *            - The application context.
	 * @param message
	 *            - The message to be displayed.
	 */
	public static void makeServerErrorToast(Context context, String message) {
		makeToast(context,
				message + ", " + context.getString(R.string.http_error));
	}

	/**
	 * Method which refers to the generic method {@see #makeToast(Context, int)}
	 * . It displays toast message that indicates connection error has occurred.
	 * 
	 * @param context
	 *            - The application context.
	 */
	public static void makeConnectionProblemToast(Context context) {
		makeToast(context, R.string.http_exception);
	}

	/**
	 * Method which refers to the generic method {@see #makeToast(Context,
	 * String)}, indicating connection error has occurred.
	 * 
	 * @param context
	 *            - The application context.
	 * @param message
	 *            - The message to be displayed.
	 */
	public static void makeConnectionProblemToast(Context context,
			String message) {
		makeToast(context,
				message + ", " + context.getString(R.string.http_exception));
	}

	/**
	 * Method which refers to the generic method {@see #makeToast(Context, int)}
	 * . It displays toast message that indicates parsing error has occurred.
	 * 
	 * @param context
	 *            - The application context.
	 */
	public static void makeParserErrorToast(Context context) {
		makeToast(context, R.string.parsing_exception);
	}

	/**
	 * Method which refers to the generic method {@see #makeToast(Context,
	 * String)}, indicating parsing error has occurred.
	 * 
	 * @param context
	 *            - The application context.
	 * @param message
	 *            - The message to be displayed.
	 */
	public static void makeParserErrorToast(Context context, String message) {
		makeToast(context,
				message + ", " + context.getString(R.string.parsing_exception));
	}
}
