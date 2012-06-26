/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpickertutorial.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.chute.android.avatarpicker.util.intent.AvatarImageIntentWrapper;
import com.chute.android.avatarpickertutorial.R;
import com.chute.android.avatarpickertutorial.intent.AvatarPickerIntentWrapper;
import com.chute.android.avatarpickertutorial.model.UploadSingleton;
import com.chute.sdk.model.GCAccountStore;
import com.chute.sdk.model.GCChuteModel;
import com.chute.sdk.model.GCLocalAssetModel;
import com.chute.sdk.utils.Logger;
import com.darko.imagedownloader.ImageLoader;

/**
 * {@link AvatarPickerTutorialActivity} contains an ImageView for displaying the
 * selected avatars picked using the AvatarPicker component.
 * 
 */
public class AvatarPickerTutorialActivity extends Activity {

	public static final String TAG = AvatarPickerTutorialActivity.class
			.getSimpleName();

	/**
	 * ImageView for displaying avatars
	 */
	private ImageView defaultAvatar;

	/**
	 * An instance of the {@link ImageLoader}
	 */
	private ImageLoader loader;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avatar_picker_tutorial_activity);

		// Test token
		GCAccountStore
				.getInstance(getApplicationContext())
				.setPassword(
						"d3149b3ce0f1bc15a6330df8a8b1c431d2d40a853763022cbf1bb2817a30dbfa");

		loader = ImageLoader.getLoader(getApplicationContext());

		defaultAvatar = (ImageView) findViewById(R.id.defaultAvatar);
		defaultAvatar.setOnClickListener(new DefaultAvatarClickListener());
	}

	/**
	 * When the default avatar ImageView is clicked, AvatarPicker component is
	 * launched.
	 * 
	 */
	private final class DefaultAvatarClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			final AvatarPickerIntentWrapper wrapper = new AvatarPickerIntentWrapper(
					AvatarPickerTutorialActivity.this);
			wrapper.setChuteId("608"); // set chute ID for avatar upload
			wrapper.startActivityForResult(AvatarPickerTutorialActivity.this,
					AvatarPickerIntentWrapper.REQUEST_CODE);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		final AvatarImageIntentWrapper wrapper = new AvatarImageIntentWrapper(
				data);
		loader.displayImage(wrapper.getPath(), defaultAvatar);
		Logger.e(TAG, "image path =  " + wrapper.getPath());
		if (wrapper.getAccountName() == null) {
			uploadPhoto(wrapper.getPath());
		}
	}

	/**
	 * If the returned image lives on the device, it gets uploaded to a
	 * {@link GCChuteModel}.
	 * 
	 * @param path
	 *            - The {@link GCLocalAssetModel} path.
	 */
	public void uploadPhoto(final String path) {
		GCLocalAssetModel asset = new GCLocalAssetModel();
		asset.setFile(path);
		asset.setIdentifier("avatar");
		UploadSingleton.getInstance(getApplicationContext()).uploadPhoto(asset);
	}

}