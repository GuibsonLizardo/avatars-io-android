/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.util.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.chute.android.avatarpicker.model.ImageItem;
import com.chute.android.avatarpicker.ui.imagemanipulation.CropImage;
import com.chute.sdk.model.GCAccountModel;

/**
 * The {@link AvatarImageIntentWrapper} class is a wrapper class that has the
 * responsibility of wrapping the parameters needed for the intent.
 * 
 */
public class AvatarImageIntentWrapper extends IntentWrapper {

	public static final String TAG = AvatarImageIntentWrapper.class
			.getSimpleName();

	/**
	 * Key for holding the image path
	 */
	private static final String KEY_PATH = "key_path";
	/**
	 * Key for holding the bitmap
	 */
	private static final String KEY_BITMAP = "key_bitmap";
	/**
	 * Key for holding {@link GCAccountModel} name
	 */
	private static final String KEY_ACCOUNT_NAME = "key_account_name";

	/**
	 * Launches {@link CropImage} class
	 * 
	 * @param context
	 *            - The application context
	 */
	public AvatarImageIntentWrapper(Context context) {
		super(context, CropImage.class);
	}

	/**
	 * Initializes the intent
	 * 
	 * @param intent
	 */
	public AvatarImageIntentWrapper(Intent intent) {
		super(intent);
	}

	/**
	 * Gets bitmap
	 * 
	 * @return - {@link Bitmap}
	 */
	public Bitmap getBitmap() {
		return (Bitmap) getIntent().getExtras().getParcelable(KEY_BITMAP);
	}

	/**
	 * Sets bitmap
	 * 
	 * @param bitmap
	 */
	public void setBitmap(Bitmap bitmap) {
		getIntent().putExtra(KEY_PATH, (Bitmap) bitmap);
	}

	/**
	 * Gets image path
	 * 
	 * @return - {@link ImageItem} path
	 */
	public String getPath() {
		return getIntent().getExtras().getString(KEY_PATH);
	}

	/**
	 * Sets image path
	 * 
	 * @param path
	 *            - {@link ImageItem} path
	 */
	public void setPath(String path) {
		getIntent().putExtra(KEY_PATH, path);
	}

	/**
	 * Gets URI
	 * 
	 * @return - {@link Uri}
	 */
	public Uri getUri() {
		return getIntent().getData();
	}

	/**
	 * Sets URI
	 * 
	 * @param uri
	 *            - {@link Uri}
	 */
	public void setUri(Uri uri) {
		getIntent().setData(uri);
	}

	/**
	 * Sets account name
	 * 
	 * @param accountName
	 *            - {@link GCAccountModel} name
	 */
	public void setAccountName(String accountName) {
		getIntent().putExtra(KEY_ACCOUNT_NAME, accountName);
	}

	/**
	 * Gets account name
	 * 
	 * @return - {@link GCAccountModel} name
	 */
	public String getAccountName() {
		return getIntent().getExtras().getString(KEY_ACCOUNT_NAME);
	}

	public void startActivityForResult(Activity context, int code) {
		context.startActivityForResult(getIntent(), code);
	}
}
