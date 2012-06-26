/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.util.intent;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.chute.android.avatarpicker.ui.activity.AvatarActivity;
import com.chute.sdk.model.GCAccountModel;

/**
 * The {@link IntentUtil} class is a utility class that holds methods for intent
 * management.
 * 
 */
public class IntentUtil {

	public static final String TAG = IntentUtil.class.getSimpleName();

	/**
	 * Delivers data to the initial activity
	 * 
	 * @param context
	 *            - The application context
	 * @param path
	 *            - The image path
	 * @param accountName
	 *            - {@link GCAccountModel} name
	 */
	public static void deliverDataToInitialActivity(final Activity context,
			final String path, final String accountName) {
		final AvatarImageIntentWrapper wrapper = new AvatarImageIntentWrapper(
				new Intent(context, AvatarActivity.class));
		if (!TextUtils.isEmpty(path)) {
			wrapper.setPath(path);
		}
		if (!TextUtils.isEmpty(accountName)) {
			wrapper.setAccountName(accountName);
		}
		wrapper.getIntent().addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		wrapper.startActivity(context);
	}
}
