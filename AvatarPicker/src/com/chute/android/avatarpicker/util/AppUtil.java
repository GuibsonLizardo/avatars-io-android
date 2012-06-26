/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * The {@link AppUtil} class contains utility methods.
 * 
 */
public class AppUtil {

	public static final String TAG = AppUtil.class.getSimpleName();

	/**
	 * Default non-args constructor
	 */
	private AppUtil() {
	}

	/**
	 * The following method creates a File from a given Bitmap.
	 * 
	 * @param context
	 *            - The application context.
	 * @param bitmap
	 *            - The bitmap to be converted into a File.
	 * @return
	 */
	public static File getFilefromBitmap(Context context, Bitmap bitmap) {
		File file = null;
		try {
			String path = Environment.getExternalStorageDirectory().toString();
			OutputStream fOut = null;
			file = new File(path, "1" + ".jpg");
			fOut = new FileOutputStream(file);

			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), file.getName(), file.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;

	}
}
