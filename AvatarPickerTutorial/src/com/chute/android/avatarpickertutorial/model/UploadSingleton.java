/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpickertutorial.model;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

import com.chute.android.avatarpicker.R;
import com.chute.android.avatarpicker.communication.api.GCAvatarService;
import com.chute.android.avatarpicker.ui.activity.AvatarActivity;
import com.chute.android.avatarpicker.util.NotificationUtil;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.asset.GCUploadProgressListener;
import com.chute.sdk.model.GCAssetModel;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.model.GCLocalAssetModel;
import com.chute.sdk.utils.Logger;

/**
 * The {@link UploadSingleton} class manages the upload process. This class is a
 * singleton object, maintaining a thread-safe instance of itself.
 * 
 */
public class UploadSingleton extends Observable {

	public static final String TAG = UploadSingleton.class.getSimpleName();

	/**
	 * The static instance of the {@link UploadSingleton}, as per the typical
	 * Singleton design pattern. Note the usage of the volatile keyword for
	 * thread-safe programming.
	 */
	private static volatile UploadSingleton instance;

	/**
	 * Notification manager
	 */
	private final NotificationManager nm;

	/**
	 * The application context
	 */
	private final Context context;

	/**
	 * A private constructor, preventing the manual initialization of this
	 * object as per the singleton pattern.
	 * 
	 * @param context
	 *            - The application context
	 */
	private UploadSingleton(Context context) {
		super();
		this.context = context;
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * Static initialization method. This method will always return the
	 * singleton instance of the {@link UploadSingleton} object and it will
	 * initialize it the first time it is requested. It uses the double-checked
	 * locking mechanism and initialization on demand.
	 * 
	 * @return the system-wide singleton instance of {@link UploadSingleton}.
	 */
	public static synchronized UploadSingleton getInstance(Context context) {
		if (instance == null) {
			instance = new UploadSingleton(context);
		}
		return instance;
	}

	/**
	 * {@link GCHttpUploadCallback} class creates HTTP request in order to get
	 * {@link GCAssetModel}.
	 * 
	 */
	private class GCHttpUploadCallback implements GCHttpCallback<GCAssetModel> {
		@Override
		public void onSuccess(GCAssetModel responseData) {
			Logger.e(TAG, "ASSETS UPLOADED");
			Logger.e(TAG, "response data = " + responseData);
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
			Logger.e(TAG, "Upload callback create Http Exception ", exception);
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
			Logger.d(TAG, "Upload callback Http Error " + statusMessage
					+ " Code " + responseCode);
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
			Logger.e(TAG, "Upload callback Parser Exception  Code "
					+ responseCode, exception);

		}
	}

	/**
	 * Method used for starting the upload process
	 * 
	 * @param localAssetModel
	 */
	public void uploadPhoto(GCLocalAssetModel localAssetModel) {
		try {
			GCAvatarService.upload(context,
					new GCUploadProgressListenerImplementation(),
					new GCHttpUploadCallback(), localAssetModel).executeAsync();
			NotificationUtil.makeToast(context, R.string.photo_uploaded);
		} catch (Exception e) {
			Log.w(TAG, "", e);
		}
	}

	/**
	 * The {@link GCUploadProgressListenerImplementation} class tracks the
	 * upload process and shows the upload progress
	 * 
	 */
	private final class GCUploadProgressListenerImplementation implements
			GCUploadProgressListener {

		private static final int DELAY = 2500;
		private static final int UPLOAD_NOTIFICATION_ID = 12335;

		private Notification notification;
		private PendingIntent contentIntent;

		Timer t;
		private long total;
		private long uploaded;

		class ProgressTask extends TimerTask {
			@Override
			public void run() {
				setNotificationInfo(total, uploaded);
			}
		}

		@Override
		public void onUploadStarted(GCAssetModel asset, Bitmap thumbnail) {
			Log.d(TAG, "Upload started");
			setSyncIndicator(true, thumbnail);
			t = new Timer();
			t.schedule(new ProgressTask(), DELAY, DELAY);
		}

		@Override
		public void onProgress(long total, long uploaded) {
			this.total = total;
			this.uploaded = uploaded;
		}

		public void setSyncIndicator(boolean state, final Bitmap bitmap) {
			if (state == false) {
				nm.cancel(UPLOAD_NOTIFICATION_ID);
				return;
			}
			Intent intent = new Intent(context, AvatarActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			contentIntent = PendingIntent.getActivity(context, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);

			notification = new Notification(R.drawable.placeholder, null,
					System.currentTimeMillis());
			notification.contentView = new RemoteViews(
					context.getPackageName(), R.layout.notification_upload);
			notification.contentIntent = contentIntent;
			if (bitmap == null) {
				notification.contentView.setImageViewResource(
						R.id.imageViewNotificationIcon, R.drawable.placeholder);
			} else {
				notification.contentView.setImageViewBitmap(
						R.id.imageViewNotificationIcon, bitmap);
			}
			notification.contentView.setTextViewText(
					R.id.textViewNotificationProgress, "");
			notification.contentView.setProgressBar(
					R.id.progressBarNotification, 100, 0, false);
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// TODO when internet connection error occurs, the notification
			// listener needs to be canceled
			// FLAG_AUTO_CANCEL quickfix
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			// notification.flags |= Notification.FLAG_NO_CLEAR;
			nm.notify(UPLOAD_NOTIFICATION_ID, notification);
		}

		private void setNotificationInfo(long total, long uploaded) {
			try {
				int percent = (int) ((uploaded * 100) / total);
				Logger.d(TAG, "Content Size " + String.valueOf(total));
				Logger.d(TAG, "Current progress " + String.valueOf(uploaded));
				Logger.d(TAG, "Current progress Text" + percent + "%");
				if (total < uploaded) {
					return;
				}
				notification.contentView.setProgressBar(
						R.id.progressBarNotification, 100, percent, false);
				notification.contentView
						.setTextViewText(
								R.id.textViewNotificationProgress,
								"Uploading  "
										+ " ("
										+ String.valueOf((int) ((uploaded / (float) total) * 100))
										+ "%)");
				nm.notify(UPLOAD_NOTIFICATION_ID, notification);
			} catch (Exception e) {
				Log.d(TAG, "", e);
			}
		}

		@Override
		public void onUploadFinished(GCAssetModel assetModel) {
			Log.d(TAG, "Upload finished");
			t.cancel();
			setSyncIndicator(false, null);
		}

	}

}
