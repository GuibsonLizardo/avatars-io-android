/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpickertutorial.intent;

import com.chute.android.avatarpicker.ui.activity.AvatarActivity;
import com.chute.sdk.model.GCChuteModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * The {@link AvatarPickerIntentWrapper} class is a wrapper class that has
 * the responsibility of wrapping the parameters needed for the intent.
 * 
 */
public class AvatarPickerIntentWrapper extends IntentWrapper {

	/**
	 * Request code needed for returning data in
	 * <code>onActivityResult(int requestCode, int resultCode, Intent data)</code>
	 */
	public static final int REQUEST_CODE = 1;

	/**
	 * {@link GCChuteModel} ID.
	 */
	public static final String KEY_CHUTE_ID = "key_chute_id";
	public static final String TAG = AvatarPickerIntentWrapper.class
			.getSimpleName();

	/**
	 * Initializes the intent
	 * 
	 * @param intent
	 */
	public AvatarPickerIntentWrapper(Intent intent) {
		super(intent);
	}

	/**
	 * Launches an activity
	 * 
	 * @param context
	 *            - The application context
	 */
	public AvatarPickerIntentWrapper(Context packageContext, Class<?> cls) {
		super(packageContext, cls);
	}

	/**
	 * Getter and setter for {@link GCChuteModel} ID.
	 */
	public String getChuteId() {
		return getIntent().getExtras().getString(KEY_CHUTE_ID);
	}

	public void setChuteId(String id) {
		getIntent().putExtra(KEY_CHUTE_ID, id);
	}

	/**
	 * Launches {@link AvatarActivity} class
	 * 
	 * @param context
	 *            - The application context
	 */
	public AvatarPickerIntentWrapper(Context packageContext) {
		super(new Intent(packageContext, AvatarActivity.class));
	}

	public void startActivityForResult(Activity context, int code) {
		context.startActivityForResult(getIntent(), code);
	}

}
