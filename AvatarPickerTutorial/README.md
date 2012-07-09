Introduction
====

AvatarPickerTutorial is a tutorial project that shows how to use the AvatarPicker component. It contains Chute SDK library, PhotoPicker+ library as well as AvatarPicker library.
This tutorial enables browsing device gallery photos using the PhotoPicker+ component, as well as social services avatars, selecting a photo and displaying the selected photo and its path.

![image8](https://github.com/chute/avatars-io-android/raw/master/AvatarPickerTutorial/screenshots/8.png)![image9](https://github.com/chute/avatars-io-android/raw/master/AvatarPickerTutorial/screenshots/9.png)![image10](https://github.com/chute/avatars-io-android/raw/master/AvatarPickerTutorial/screenshots/10.png)![image11](https://github.com/chute/avatars-io-android/raw/master/AvatarPickerTutorial/screenshots/11.png)![image12](https://github.com/chute/avatars-io-android/raw/master/AvatarPickerTutorial/screenshots/12.png)![image13](https://github.com/chute/avatars-io-android/raw/master/AvatarPickerTutorial/screenshots/13.png)

Setup
====

* Go through [ProjectSetup.md](https://github.com/chute/avatars-io/blob/master/android/AvatarPickerTutorial/ProjectSetup.md) for a complete guide on how to setup the chute SDK.
  
* Add the [PhotoPicker+](https://github.com/chute/photo-picker-plus/tree/master/Android/ChutePhotoPicker+) component to your project by either copying all the resources and source code or by adding it as an Android Library project.

* Add the [AvatarPicker](https://github.com/chute/avatars-io/tree/master/android/AvatarPicker) component to your project by either copying all the resources and source code or by adding it as an Android Library project.

* The next thing you need to do is register the activities, services and the application class into the AndroidManifest.xml file:

    ```
        <application
        android:name=".app.AvatarPickerTutorialApp"
        android:icon="@drawable/ic_launcher"
        android:screenOrientation="portrait"
        android:theme="@android:style/Theme.Light.NoTitleBar" 
        android:label="@string/app_name" >
        <service android:name="com.chute.sdk.api.GCHttpService" />
        <activity
            android:name=".app.AvatarPickerTutorialActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" 
            android:label="@string/app_name" >
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
         <activity 
            android:name="com.chute.android.avatarpicker.ui.activity.AvatarActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
         </activity>
         <activity
            android:name="com.chute.android.avatarpicker.ui.imagemanipulation.CropImage"
            android:configChanges="orientation|keyboardHidden"
            android:label="@string/crop_label"
            android:process=":CropImage"
            android:theme="@android:style/Theme.Black.NoTitleBar" >
        </activity>
         <activity
            android:name="com.chute.android.photopickerplus.app.ChooseServiceActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <activity
            android:name="com.chute.android.photopickerplus.app.AlbumsActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <activity
            android:name="com.chute.sdk.api.authentication.GCAuthenticationActivity"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <activity
            android:name="com.chute.android.photopickerplus.app.GridActivity"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Light.NoTitleBar" >
        </activity>
        <provider android:name="com.chute.android.avatarpicker.db.ImagesProvider"
			android:authorities="com.chute.android.avatarpicker.images" 
			android:exported="true"
			android:enabled="true" android:multiprocess="true" />
       </application>
    ```


Usage
====

##AvatarPickerTutorialApp.java 
This class is the extended Application class. It is registered inside the "application" tag in the manifest and is used for initializing the utility classes used in the component.
AvatarPickerTutorialApp can extend AvatarPickerApp like shown in this tutorial:

<pre><code>
public class AvatarPickerTutorialApp extends AvatarPickerApp {

}
</code></pre>

This way the developer can use his own methods and code inside the Application class. 

If the developer decides to extend the Application class instead of AvatarPickerApp he must copy the all the code below:

<pre><code>
public class AvatarPickerTutorialApp extends Application {
    private static ImageLoader createImageLoader(Context context) {
		ImageLoader imageLoader = new ImageLoader(context, R.drawable.placeholder);
		imageLoader.setDefaultImageSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 75, context.getResources()
						.getDisplayMetrics()));
		return imageLoader;
    }

    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
	super.onCreate();
	mImageLoader = createImageLoader(this);
	GCPreferenceUtil.init(getApplicationContext());
    }

    @Override
    public Object getSystemService(String name) {
	if (ImageLoader.IMAGE_LOADER_SERVICE.equals(name)) {
	    return mImageLoader;
	} else {
	    return super.getSystemService(name);
	}
    }

}
</code></pre>

AvatarPickerTutorialApp can also be neglected by registering AvatarPickerApp into the manifest instead of AvatarPickerTutoiralApp if the developer doesn't have the need for extending the Application class.

##AvatarPickerTutorialActivity.java 
This Activity class contains an ImageView. When clicked, the AvatarPickerIntentWrapper starts AvatarActivity from AvatarPicker component. AvatarPickerIntentWrapper is a wrapper class that wraps the parameters needed for the intent.

<pre><code>
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
</code></pre>

AvatarActivity contains photo grid displaying avatars from different services. The user can authenticate using Facebook, Twitter, Flickr, Picasa, Instagram and Gravatar or can choose a photo using the PhotoPicker+ component.
When selecting a photo using the PhotoPicker+, the photo is additionally cropped, returned to the activity that started AvatarPicker component and uploaded into a Chute. 
When picking one of the available social services, a result is returned to the activity that started the component where the selected avatar is displayed.

<pre><code>
Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		final AvatarImageIntentWrapper wrapper = new AvatarImageIntentWrapper(
				data);
		loader.displayImage(wrapper.getPath(), defaultAvatar);
		if (wrapper.getAccountName() == null) {
			uploadPhoto(wrapper.getPath());
		}
	}
</code></pre>


##Avatar sizes
Avatars are available in 3 sizes:
s - small - 50x50, 
m - medium - 75x75, 
l - large - 150x150 (when available, otherwise medium returned).
In order to get the desired size, add "?size=large" to the URL for large sized avatar, "?size=medium" for
medium sized avatar or "?size=small" for small sized avatar.

<pre><code>
String url = String.format(Constants.URL_GET_AVATAR,
			((AccountType) serviceIcon.getTag()).getName(),
			GCPreferenceUtil.get().getUidForAccount((AccountType) serviceIcon.getTag()));
imageLoader.displayImage(url.concat("?size=large"), serviceIcon);
</code></pre>


##Avatar upload
The avatar to be uploaded is wrapped in a GCLocalAssetModel and upload callback is started:
<pre><code>
public static GCHttpRequest upload(final Context context,
			GCUploadProgressListener onProgressUpdate,
			final GCHttpCallback<GCAssetModel> callback,
			final GCLocalAssetModel asset) {

		return new AvatarUploadRequest(context, onProgressUpdate,
				callback, asset);
	}
</code></pre>
In the <code>execute()</code> method of the AvatarUploadRequest, another HTTP request is being executed in order to get the upload token:
<pre><code>
public static <T> GCHttpRequest getToken(final Context context,
			final GCLocalAssetModel localAssetModel,
			final GCHttpResponseParser<T> parser,
			final GCHttpCallback<T> callback) {
		return new AvatarTokenRequest<T>(context, localAssetModel, parser,
				callback);
	}
</code></pre>
After the token is retrieved, the upload info is uploaded to s3 and the upload process is completed executing AvatarUploadComplete HTTP request
where the upload info is posted to the following URL: "http://avatars.io/v1/token/%s/complete".
<pre><code>
public static GCHttpRequest uploadComplete(final Context context,
			final String id,
			final GCHttpCallback<GCAssetModel> callback) {
		return new AvatarUploadCompleteRequest<GCAssetModel>(context, id, new AvatarUploadCompleteObjectParser(id), callback);
	}
</code></pre>	
