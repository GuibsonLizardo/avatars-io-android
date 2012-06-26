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
import android.util.Log;

import com.chute.android.avatarpicker.communication.parsers.AvatarTokenObjectParser;
import com.chute.android.avatarpicker.communication.parsers.AvatarUploadCompleteObjectParser;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.GCHttpCallbackImpl;
import com.chute.sdk.api.GCHttpRequest;
import com.chute.sdk.api.GCHttpRequestStore;
import com.chute.sdk.api.asset.AssetsTokenRequest;
import com.chute.sdk.api.asset.GCUploadProgressListener;
import com.chute.sdk.model.GCAssetModel;
import com.chute.sdk.model.GCLocalAssetModel;
import com.chute.sdk.model.GCUploadToken;
import com.chute.sdk.parsers.base.GCHttpResponseParser;
import com.chute.sdk.utils.Logger;
import com.chute.sdk.utils.rest.GCS3Uploader;

/**
 * The {@link AvatarUploadRequest} class contains all the necessary methods
 * needed for uploading an avatar.
 * 
 */
public class AvatarUploadRequest implements GCHttpRequest {

	public static final String TAG = AvatarUploadRequest.class.getSimpleName();
	private final GCLocalAssetModel localAsset;
	private final GCUploadProgressListener onProgressUpdate;
	private GCUploadToken uploadToken;
	private final GCHttpCallback<GCAssetModel> callback;
	private final Context context;

	public void resumeThread() {
		synchronized (this) {
			Log.w(TAG, "Resumed thread");
			this.notifyAll();
		}
	}

	public AvatarUploadRequest(Context context,
			GCUploadProgressListener onProgressUpdate,
			GCHttpCallback<GCAssetModel> callback, GCLocalAssetModel localAsset) {
		this.context = context;
		this.callback = callback;

		this.onProgressUpdate = onProgressUpdate;
		this.localAsset = localAsset;
	}

	@Override
	public void execute() {
		final GCS3Uploader uploader = new GCS3Uploader(onProgressUpdate);
		try {
			GCHttpRequest token = getToken(context, localAsset,
					new TokenListener());
			synchronized (this) {
				Log.w(TAG, "Wait thread");
				token.execute();
				this.wait(4000);
			}
			if (uploadToken != null) {
				if (uploadToken.getUploadInfo() != null) {
					uploader.startUpload(uploadToken);
					Logger.d(TAG, "Calling upload complete");
					Logger.d(TAG, "Need to upload");
				}
				uploadComplete(context, uploadToken.getId(),
						new UploadCompleteListener()).execute();

			} else {
				throw new IllegalArgumentException("Token is Null");
			}
		} catch (Exception e) {
			Logger.e(TAG, "Error in the Upload Request", e);
			callback.onHttpException(null, e);
		}
	}

	private final class TokenListener extends GCHttpCallbackImpl<GCUploadToken> {

		@Override
		public void onSuccess(GCUploadToken responseData) {
			uploadToken = responseData;
			resumeThread();
		}

		@Override
		public void onGeneralError(int responseCode, String message) {
			super.onGeneralError(responseCode, message);
			resumeThread();
		}
	}

	private final class UploadCompleteListener extends
			GCHttpCallbackImpl<GCAssetModel> {

		@Override
		public void onSuccess(GCAssetModel responseData) {
			callback.onSuccess(responseData);
		}

		@Override
		public void onGeneralError(int responseCode, String message) {
			super.onGeneralError(responseCode, message);
			resumeThread();
		}
	}

	/**
	 * Method that defaults to the generic method {@see #verify(Context,
	 * GCHttpResponseParser, GCHttpCallback, GCLocalAssetModel...)}. This method
	 * uses {@link AvatarTokenObjectParser} which has {@link GCUploadToken} as a
	 * return type if the callback is successful.
	 * 
	 * @param context
	 *            - The application context
	 * @param localAssetModel
	 *            - {@link GCLocalAssetModel}
	 * @param callback
	 *            - Instance of {@link GCHttpCallback} interface. If successful,
	 *            the callback returns {@link GCUploadToken}.
	 * @return -
	 */
	public static GCHttpRequest getToken(final Context context,
			final GCLocalAssetModel localAssetModel,
			GCHttpCallback<GCUploadToken> callback) {
		return getToken(context, localAssetModel,
				new AvatarTokenObjectParser(), callback);
	}

	/**
	 * Method used for getting the token. It returns a JSon object containing
	 * token info using the following parameters: context, given parser and
	 * given callback.
	 * 
	 * @param context
	 *            - The application context
	 * @param localAssetModel
	 *            - {@link GCLocalAssetModel}
	 * @param parser
	 *            - Instance of {@link GCHttpResponseParser} interface. You can
	 *            add a custom parser or use the parser provided here {@see
	 *            #getToken(Context, GCLocalAssetModel, GCHttpResponseParser,
	 *            GCHttpCallback)}.
	 * @param callback
	 *            - Instance of {@link GCHttpCallback} interface. According to
	 *            the parser, the callback should have the same return type.
	 * @return - Instance of {@link AssetsTokenRequest}, class that implements
	 *         {@link GCHttpRequest}.
	 */
	public static <T> GCHttpRequest getToken(final Context context,
			final GCLocalAssetModel localAssetModel,
			final GCHttpResponseParser<T> parser,
			final GCHttpCallback<T> callback) {
		return new AvatarTokenRequest<T>(context, localAssetModel, parser,
				callback);
	}

	/**
	 * Method that defaults to the generic method {@see #uploadComplete(Context,
	 * String, GCHttpResponseParser, GCHttpCallback)}. This method uses
	 * {@link AvatarUploadCompleteObjectParser} which has {@link GCAssetModel}
	 * as a return type if the callback is successful.
	 * 
	 * @param context
	 *            - The application context
	 * @param id
	 *            - {@link GCAssetModel} ID
	 * @param callback
	 *            - Instance of {@link GCHttpCallback} interface. If successful,
	 *            the callback returns {@link GCUploadToken}.
	 * @return
	 */
	public static GCHttpRequest uploadComplete(final Context context,
			final String id, final GCHttpCallback<GCAssetModel> callback) {
		return new AvatarUploadCompleteRequest<GCAssetModel>(context, id,
				new AvatarUploadCompleteObjectParser(id), callback);
	}

	/**
	 * Method used for avatar upload process completion.
	 * 
	 * @param context
	 *            - The application context
	 * @param id
	 *            - {@link GCAssetModel} ID
	 * @param parser
	 *            - - Instance of {@link GCHttpResponseParser} interface. You
	 *            can add a custom parser or use the parser provided here {@see
	 *            #uploadComplete(Context, String, GCHttpCallback)}.
	 * @param callback
	 *            - - Instance of {@link GCHttpCallback} interface. According to
	 *            the parser, the callback should have the same return type.
	 * @return - Instance of {@link AssetsUploadCompleteRequest}, class that
	 *         implements {@link GCHttpRequest}.
	 */
	public static <T> GCHttpRequest uploadComplete(final Context context,
			final String id, final GCHttpResponseParser<T> parser,
			final GCHttpCallback<T> callback) {
		return new AvatarUploadCompleteRequest<T>(context, id, parser, callback);
	}

	@Override
	public void executeAsync() {
		GCHttpRequestStore.getInstance(context).launchServiceIntent(this);
	}

}
