/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.dao;

import java.util.List;

import com.chute.android.avatarpicker.model.ImageItem;

import android.content.Context;
import android.database.Cursor;

/**
 * The {@link ImageDao} interface is used for getting, deleting, saving and
 * inserting {@link ImageItem} in database.
 * 
 */
public interface ImageDao {

	public Long saveImage(Context context, ImageItem image);

	public boolean deleteImage(Context context, Long imageId);

	public ImageItem getImage(Context context, String imagePath);

	public List<ImageItem> getUploadImages(Context context, String upload);

	public List<ImageItem> getGravatarImages(Context context, String gravatar);

	public Cursor getMediaPhotos(Context context);

}
