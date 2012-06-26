// Copyright (c) 2011, Chute Corporation. All rights reserved.
// 
//  Redistribution and use in source and binary forms, with or without modification, 
//  are permitted provided that the following conditions are met:
// 
//     * Redistributions of source code must retain the above copyright notice, this 
//       list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright notice,
//       this list of conditions and the following disclaimer in the documentation
//       and/or other materials provided with the distribution.
//     * Neither the name of the  Chute Corporation nor the names
//       of its contributors may be used to endorse or promote products derived from
//       this software without specific prior written permission.
// 
//  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
//  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
//  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
//  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
//  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
//  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
//  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
//  OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
//  OF THE POSSIBILITY OF SUCH DAMAGE.
// 
package com.chute.android.avatarpicker.communication.api;

import android.content.Context;

import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.GCHttpRequest;
import com.chute.sdk.api.asset.AssetsUploadRequest;
import com.chute.sdk.api.asset.GCUploadProgressListener;
import com.chute.sdk.collections.GCLocalAssetCollection;
import com.chute.sdk.model.GCAssetModel;
import com.chute.sdk.model.GCLocalAssetModel;

/**
 * The {@link GCAvatarService} class is a helper class for uploading avatars.
 * 
 */
public class GCAvatarService {

	public static final String TAG = GCAvatarService.class.getSimpleName();

	/**
	 * A private no-args default constructor.
	 */
	private GCAvatarService() {
		super();
	}

	/**
	 * Method used for uploading avatars. It returns a JSON object containing a
	 * string using the following parameters: context,
	 * {@link GCLocalAssetCollection}, {@link GCUploadProgressListener} and the
	 * given callback and parser.
	 * 
	 * @param context
	 *            The application context.
	 * @param onProgressUpdate
	 *            Instance of {@link GCUploadProgressListener} interface.
	 * @param callback
	 *            Instance of {@link GCHttpCallback} interface. According to the
	 *            parser, the callback should have the same return type.
	 * @param asset
	 *            Instance of {@link GCLocalAssetModel} class.
	 * @return Instance of {@link AssetsUploadRequest}, a class that implements
	 *         {@link GCHttpRequest}.
	 */
	public static GCHttpRequest upload(final Context context,
			GCUploadProgressListener onProgressUpdate,
			final GCHttpCallback<GCAssetModel> callback,
			final GCLocalAssetModel asset) {

		return new AvatarUploadRequest(context, onProgressUpdate, callback,
				asset);
	}

}
