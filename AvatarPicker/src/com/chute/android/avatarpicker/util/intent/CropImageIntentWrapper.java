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
import android.net.Uri;

import com.chute.android.avatarpicker.ui.imagemanipulation.CropImage;

/**
 * The {@link CropImageIntentWrapper} class is a wrapper class that has the
 * responsibility of wrapping the parameters needed for the intent.
 * 
 */
public class CropImageIntentWrapper extends IntentWrapper {

	public static final String TAG = CropImageIntentWrapper.class
			.getSimpleName();

	/**
	 * Keys for holding options that specify the output image size
	 */
	private static final String KEY_OUTPUTX = "outputX";
	private static final String KEY_OUTOPUTY = "outputY";
	private static final String KEY_ASPECTX = "aspectX";
	private static final String KEY_ASPECTY = "aspectY";

	/**
	 * Key for holding boolean value indicating whether the output should be
	 * scaled to fit.
	 */
	private static final String KEY_SCALE = "scale";

	/**
	 * Key for holding boolean value indicating if there should be face
	 * detection.
	 */
	private static final String KEY_NO_FACE_DETECTION = "NoFaceDetection";

	/**
	 * Key for holding the URI output
	 */
	private static final String KEY_OUTPUT = "output";

	/**
	 * Launches {@link CropImage} class.
	 * 
	 * @param context
	 *            - The application context.
	 */
	public CropImageIntentWrapper(Context context) {
		super(context, CropImage.class);
	}

	/**
	 * {@link Intent} initialization.
	 * 
	 * @param intent
	 */
	public CropImageIntentWrapper(Intent intent) {
		super(intent);
	}

	/**
	 * Getters and setters
	 */
	public void setOutputX(int outputX) {
		getIntent().putExtra(KEY_OUTPUTX, outputX);
	}

	public int getOutputX() {
		return getIntent().getExtras().getInt(KEY_OUTPUTX);
	}

	public void setOutputY(int outputY) {
		getIntent().putExtra(KEY_OUTOPUTY, outputY);
	}

	public int getOutputY() {
		return getIntent().getExtras().getInt(KEY_OUTOPUTY);
	}

	public void setAspectX(int aspectX) {
		getIntent().putExtra(KEY_ASPECTX, aspectX);
	}

	public int getAspectX() {
		return getIntent().getExtras().getInt(KEY_ASPECTX);
	}

	public void setAspectY(int aspectY) {
		getIntent().putExtra(KEY_ASPECTY, aspectY);
	}

	public int getAspectY() {
		return getIntent().getExtras().getInt(KEY_ASPECTY);
	}

	public void setScale(boolean scale) {
		getIntent().putExtra(KEY_SCALE, scale);
	}

	public boolean getScale() {
		return getIntent().getExtras().getBoolean(KEY_SCALE);
	}

	public void setNoFaceDetection(boolean noFaceDetection) {
		getIntent().putExtra(KEY_NO_FACE_DETECTION, noFaceDetection);
	}

	public boolean getNoFaceDetection() {
		return getIntent().getExtras().getBoolean(KEY_NO_FACE_DETECTION);
	}

	public void setOutput(Uri output) {
		getIntent().putExtra(KEY_OUTPUT, output);
	}

	public Uri getOutput() {
		return getIntent().getExtras().getParcelable(KEY_OUTPUT);
	}

	public Uri getUri() {
		return getIntent().getData();
	}

	public void setUri(Uri uri) {
		getIntent().setData(uri);
	}

	public void startActivityForResult(Activity context, int code) {
		context.startActivityForResult(getIntent(), code);
	}
}
