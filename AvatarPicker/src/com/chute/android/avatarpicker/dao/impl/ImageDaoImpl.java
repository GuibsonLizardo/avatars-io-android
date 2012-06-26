/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.dao.impl;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.chute.android.avatarpicker.dao.ImageDao;
import com.chute.android.avatarpicker.db.ImagesMetaData;
import com.chute.android.avatarpicker.db.ImagesMetaData.Images;
import com.chute.android.avatarpicker.model.ImageItem;
import com.chute.android.avatarpicker.util.SQLUtils;

/**
 * The {@link ImageDaoImpl} class represents an implementation of
 * {@link ImageDao} interface. This class is a singleton object, maintaining a
 * thread-safe instance of itself. It is responsible of getting, deleting,
 * saving and inserting {@link ImageItem} in database.
 * 
 */
public class ImageDaoImpl implements ImageDao {

	/**
	 * The static instance of the {@link ImageDaoImpl}, as per the typical
	 * Singleton design pattern. Note the usage of the volatile keyword for
	 * thread-safe programming.
	 */
	private static volatile ImageDaoImpl instance;

	/**
	 * Static initialization method. This method will always return the
	 * singleton instance of the {@link ImageDaoImpl} object and it will
	 * initialize it the first time it is requested. It uses the double-checked
	 * locking mechanism and initialization on demand.
	 * 
	 * @return the system-wide singleton instance of {@link ImageDaoImpl}.
	 */
	public static ImageDaoImpl getInstance() {
		if (instance == null) {
			synchronized (ImageDaoImpl.class) {
				if (instance == null) {
					instance = new ImageDaoImpl();
				}
			}
		}

		return instance;
	}

	/**
	 * A private no-args default constructor, preventing the manual
	 * initialization of this object as per the singleton pattern.
	 */
	private ImageDaoImpl() {
	}

	/**
	 * Saves the given {@link ImageItem} in {@link ImagesMetaData.Images} table
	 * in database.
	 */
	@Override
	public Long saveImage(Context context, ImageItem image) {
		ContentValues values = putItemToContentValues(image);

		Uri uri = context.getContentResolver().insert(Images.CONTENT_URI,
				values);

		if (uri != null && uri.getLastPathSegment() != null) {
			return Long.parseLong(uri.getLastPathSegment());
		}

		return null;
	}

	/**
	 * Deletes an {@link ImageItem} in {@link ImagesMetaData.Images} table in
	 * database containing the given ID.
	 */
	@Override
	public boolean deleteImage(Context context, Long imageId) {
		int deletedRows = context.getContentResolver().delete(
				Images.CONTENT_URI,
				SQLUtils.concatenateWhere(Images._ID, imageId), null);

		if (deletedRows > 0) {
			return true;
		}

		return false;
	}

	/**
	 * Given the path of the image, returns an {@link ImageItem}.
	 */
	@Override
	public ImageItem getImage(Context context, String imagePath) {
		Cursor cursor = context.getContentResolver().query(Images.CONTENT_URI,
				null, Images.FILE_PATH + "='" + imagePath + "'", null, null);
		ImageItem image = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			image = getItemFromCursor(cursor);
		}
		if (cursor != null) {
			cursor.close();
		}
		return image;
	}

	/**
	 * Finds the desired {@link ImageItem} from database using the given cursor
	 * object.
	 * 
	 * @param cursor
	 *            - Cursor needed for {@link ImagesMetaData.Images} table
	 *            manipulation in database.
	 * @return - {@link ImageItem}
	 */
	private ImageItem getItemFromCursor(Cursor cursor) {
		ImageItem image = new ImageItem();
		image.setId(cursor.getLong(cursor.getColumnIndex(Images._ID)));
		image.setService(cursor.getString(cursor.getColumnIndex(Images.SERVICE)));
		image.setFilePath(cursor.getString(cursor
				.getColumnIndex(Images.FILE_PATH)));
		return image;
	}

	/**
	 * Inserts {@link ImageItem} in database.
	 * 
	 * @param image
	 *            - the {@link ImageItem} to be inserted.
	 * @return - {@link ContentValues} object.
	 */
	private ContentValues putItemToContentValues(ImageItem image) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Images.SERVICE, image.getService());
		contentValues.put(Images.FILE_PATH, image.getFilePath());
		return contentValues;
	}

	/**
	 * Returns a list of {@link ImageItem} which belong to Gravatar service.
	 */
	@Override
	public List<ImageItem> getGravatarImages(Context context, String gravatar) {
		Cursor cursor = context.getContentResolver()
				.query(ImagesMetaData.Images.CONTENT_URI, null,
						SQLUtils.concatenateWhere(Images.SERVICE, gravatar),
						null, null);
		List<ImageItem> images = new LinkedList<ImageItem>();
		if (cursor != null && cursor.getCount() > 0) {
			ImageItem image = null;
			cursor.moveToFirst();
			do {
				image = getItemFromCursor(cursor);
				images.add(image);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return images;
	}

	/**
	 * Returns a list of {@link ImageItem} which belong to Upload service t.e.
	 * images picked using the PhotoPicker+.
	 */
	@Override
	public List<ImageItem> getUploadImages(Context context, String upload) {
		Cursor cursor = context.getContentResolver().query(
				ImagesMetaData.Images.CONTENT_URI, null,
				SQLUtils.concatenateWhere(Images.SERVICE, upload), null, null);
		List<ImageItem> images = new LinkedList<ImageItem>();
		if (cursor != null && cursor.getCount() > 0) {
			ImageItem image = null;
			cursor.moveToFirst();
			do {
				image = getItemFromCursor(cursor);
				images.add(image);
			} while (cursor.moveToNext());
		}
		if (cursor != null) {
			cursor.close();
		}
		return images;
	}

	/**
	 * Gets device images.
	 */
	public Cursor getMediaPhotos(Context context) {
		String[] projection = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA };
		Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String query = MediaStore.Images.Media.DATA + " LIKE \"%DCIM%\"";
		return context.getContentResolver().query(images, projection, query,
				null, null);
	}

}
