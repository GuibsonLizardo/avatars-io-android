/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.ui.activity;

import java.io.File;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.chute.android.avatarpicker.R;
import com.chute.android.avatarpicker.dao.impl.ImageDaoImpl;
import com.chute.android.avatarpicker.model.ImageItem;
import com.chute.android.avatarpicker.ui.adapter.AvatarServicesAdapter;
import com.chute.android.avatarpicker.ui.adapter.AvatarServicesAdapter.ViewHolder;
import com.chute.android.avatarpicker.ui.imagemanipulation.CropImage;
import com.chute.android.avatarpicker.util.AppUtil;
import com.chute.android.avatarpicker.util.NotificationUtil;
import com.chute.android.avatarpicker.util.intent.CropImageIntentWrapper;
import com.chute.android.avatarpicker.util.intent.IntentUtil;
import com.chute.android.photopickerplus.util.Constants;
import com.chute.android.photopickerplus.util.intent.PhotoActivityIntentWrapper;
import com.chute.android.photopickerplus.util.intent.PhotoPickerPlusIntentWrapper;
import com.chute.sdk.api.GCHttpCallback;
import com.chute.sdk.api.account.GCAccounts;
import com.chute.sdk.api.authentication.GCAuthenticationFactory.AccountType;
import com.chute.sdk.collections.GCAccountsCollection;
import com.chute.sdk.model.GCAccountModel;
import com.chute.sdk.model.GCAccountStore;
import com.chute.sdk.model.GCHttpRequestParameters;
import com.chute.sdk.utils.GCPreferenceUtil;
import com.darko.imagedownloader.FileCache;
import com.darko.imagedownloader.ImageLoader;

/**
 * Displays avatar services in a GridView.
 * 
 */
public class AvatarActivity extends Activity {

	public static final String TAG = AvatarActivity.class.getSimpleName();

	/**
	 * Request code needed for the intent indicating data is returned from
	 * PhotoPicker+ component.
	 */
	public static int REQUEST_CODE_PP = 1232;

	/**
	 * Request code needed for the intent indicating data is returned from
	 * {@link CropImage} activity.
	 */
	public static final int REQUEST_CROP_IMAGE = 42;

	/**
	 * Array of available services.
	 */
	public static final String[] SERVICES = new String[] { "Upload",
			"Facebook", "Twitter", "Instagram", "Gravatar" };

	/**
	 * Temporary file for image cropping.
	 */
	private File tempFileForCroppedImage;

	/**
	 * Type of the account. It can be: Facebook, Evernote, Chute, Twitter,
	 * Foursquare, Picasa, Flickr, Instagram.
	 */
	private AccountType accountType;

	/**
	 * Services grid.
	 */
	private GridView grid;

	/**
	 * Adapter for filling the grid.
	 */
	private AvatarServicesAdapter adapter;

	/**
	 * Width and height of the image to be cropped.
	 */
	private int width = 200;
	private int height = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avatar_activity);

		grid = (GridView) findViewById(R.id.avatarsGrid);
		adapter = new AvatarServicesAdapter(this, SERVICES);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new GridItemClickedListener());

	}

	private final class GridItemClickedListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ViewHolder holder = (ViewHolder) view.getTag();
			/**
			 * If Upload service chosen, launch PhotoPicker+ component.
			 */
			if (position == 0) {
				final PhotoPickerPlusIntentWrapper wrapper = new PhotoPickerPlusIntentWrapper(
						AvatarActivity.this);
				wrapper.setMultiPicker(false);
				wrapper.startActivityForResult(AvatarActivity.this,
						REQUEST_CODE_PP);
				/**
				 * If Gravatar service chosen, open dialog for e-mail submition.
				 */
			} else if (position == 6) {
				gravatarDialog().show();
				/**
				 * For any other service, start the authentication activity.
				 */
			} else {
				accountType = (AccountType) (holder.getServiceIcon().getTag());
				if (GCPreferenceUtil.get().hasAccountId(accountType)) {
					serviceClicked(
							GCPreferenceUtil.get().getAccountId(accountType),
							accountType.getName(), GCPreferenceUtil.get()
									.getUidForAccount(accountType));
				} else {
					GCAccountStore.getInstance(getApplicationContext())
							.startAuthenticationActivity(AvatarActivity.this,
									accountType, Constants.PERMISSIONS_SCOPE,
									Constants.CALLBACK_URL,
									Constants.CLIENT_ID,
									Constants.CLIENT_SECRET);
				}
			}

		}
	}

	/**
	 * When service is clicked, send image URL to the initial activity.
	 * 
	 * @param accountId
	 *            - {@link GCAccountModel} ID.
	 * @param accountName
	 *            - {@link GCAccountModel} name.
	 * @param username
	 *            - {@link GCAccountModel} user ID.
	 */
	public void serviceClicked(String accountId, String accountName, String uid) {
		String url = String.format(
				com.chute.android.avatarpicker.communication.AvatarRestConstants.URL_GET_AVATAR,
				accountName, uid);
		switch (accountType) {
		case FACEBOOK:
			IntentUtil.deliverDataToInitialActivity(AvatarActivity.this,
					url.concat("?size=large"), accountName);
			break;
		case TWITTER:
			IntentUtil.deliverDataToInitialActivity(AvatarActivity.this,
					url.concat("?size=large"), accountName);
			break;
		case INSTAGRAM:
			IntentUtil.deliverDataToInitialActivity(AvatarActivity.this, url,
					accountName);
			break;
		}

	}

	/**
	 * {@link AccountsCallback} class creates HTTP request in order to get
	 * {@link GCLocalAccountsCollection}.
	 * 
	 */
	private final class AccountsCallback implements
			GCHttpCallback<GCAccountsCollection> {

		@Override
		public void onSuccess(GCAccountsCollection responseData) {
			if (accountType == null) {
				return;
			}
			for (GCAccountModel account : responseData) {
				if (account.getType().equalsIgnoreCase(accountType.getName())) {
					GCPreferenceUtil.get().setNameForAccount(accountType,
							account.getUser().getName());
					GCPreferenceUtil.get().setIdForAccount(accountType,
							account.getId());
					GCPreferenceUtil.get().setUidForAccount(accountType,
							account.getUid());
					serviceClicked(account.getId(), accountType.getName(),
							GCPreferenceUtil.get()
									.getUidForAccount(accountType));
				}
			}
		}

		@Override
		public void onHttpException(GCHttpRequestParameters params,
				Throwable exception) {
			NotificationUtil
					.makeConnectionProblemToast(getApplicationContext());
		}

		@Override
		public void onHttpError(int responseCode, String statusMessage) {
			NotificationUtil.makeServerErrorToast(getApplicationContext());
		}

		@Override
		public void onParserException(int responseCode, Throwable exception) {
			NotificationUtil.makeServerErrorToast(getApplicationContext());
		}

	}

	/**
	 * If the returned image lives online, {@link DownloadBitmapTask} downloads
	 * it. When the thread finishes, the image is sent for cropping.
	 * 
	 */
	private class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {

		private String string;

		public DownloadBitmapTask(String string) {
			this.string = string;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			return ImageLoader.getLoader(getApplicationContext())
					.downloadBitmap(string);
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			File file = AppUtil.getFilefromBitmap(getApplicationContext(),
					result);
			tempFileForCroppedImage = new FileCache(getApplicationContext())
					.getFile(file.getAbsolutePath());
			tempFileForCroppedImage.deleteOnExit();
			Log.d(TAG, tempFileForCroppedImage.getPath());
			Uri uriOutput = Uri.fromFile(tempFileForCroppedImage);
			Uri uri = Uri.fromFile(new File(file.getAbsolutePath()));

			startCropImageActivity(uri, uriOutput);
			return;

		}
	}

	/**
	 * Launches {@link CropImage} activity.
	 * 
	 * @param uri
	 * @param uriOutput
	 */
	public void startCropImageActivity(Uri uri, Uri uriOutput) {
		CropImageIntentWrapper wrapper = new CropImageIntentWrapper(
				AvatarActivity.this);
		wrapper.setUri(uri);
		wrapper.setOutputX(width);
		wrapper.setOutputY(height);
		wrapper.setAspectX(width);
		wrapper.setAspectY(height);
		wrapper.setScale(true);
		wrapper.setNoFaceDetection(true);
		wrapper.setOutput(uriOutput);
		wrapper.startActivityForResult(AvatarActivity.this, REQUEST_CROP_IMAGE);
	}

	/**
	 * Creates dialog needed for Gravatar e-mail submition.
	 * 
	 * @return
	 */
	public Dialog gravatarDialog() {
		final Dialog dialog = new Dialog(AvatarActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.gravatar_dialog);
		final EditText editEmail = (EditText) dialog
				.findViewById(R.id.edit_email);
		TextView title = (TextView) dialog.findViewById(R.id.dlg_title);
		title.setText(getResources().getString(R.string.txt_gravatar));
		Button submit = (Button) dialog.findViewById(R.id.dlg_btn_submit);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = editEmail.getText().toString().toLowerCase();
				if (TextUtils.isEmpty(email)) {
					NotificationUtil.makeToast(
							getApplicationContext(),
							getResources().getString(
									R.string.please_enter_email));
				} else {
					String gravatar_url = "http://avatars.io/email/" + email;
					ImageItem imageItem = new ImageItem();
					imageItem.setFilePath(gravatar_url);
					imageItem.setService("gravatar");
					ImageDaoImpl.getInstance().saveImage(
							getApplicationContext(), imageItem);
					IntentUtil.deliverDataToInitialActivity(
							AvatarActivity.this, gravatar_url, "gravatar");
					dialog.dismiss();
				}
			}
		});
		return dialog;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			// get avatar call
			return;
		}
		if (requestCode == GCAccountStore.AUTHENTICATION_REQUEST_CODE) {
			GCAccounts.all(getApplicationContext(), new AccountsCallback())
					.executeAsync();
		}
		if (requestCode == REQUEST_CODE_PP) {
			final PhotoActivityIntentWrapper wrapper = new PhotoActivityIntentWrapper(
					data);
			Uri uri = Uri.parse(wrapper.getMediaCollection().get(0).getUrl());
			if (uri.getScheme().contentEquals("http")) {
				new DownloadBitmapTask(uri.toString()).execute();
			} else {
				tempFileForCroppedImage = new FileCache(getApplicationContext())
						.getFile(uri.getPath());
				tempFileForCroppedImage.deleteOnExit();
				Log.d(TAG, tempFileForCroppedImage.getPath());

				Uri uriFromFile = Uri.fromFile(new File(uri.getPath()));
				Uri uriOutput = Uri.fromFile(tempFileForCroppedImage);
				startCropImageActivity(uriFromFile, uriOutput);
				return;
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setResult(Activity.RESULT_OK,
				new Intent().putExtras(intent.getExtras()));
		finish();
	}

}
