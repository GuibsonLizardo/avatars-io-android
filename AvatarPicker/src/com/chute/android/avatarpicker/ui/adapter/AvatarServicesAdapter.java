/*
 *  Copyright (c) 2012 Chute Corporation

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.avatarpicker.ui.adapter;

import java.util.LinkedList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chute.android.avatarpicker.R;
import com.chute.android.avatarpicker.communication.AvatarRestConstants;
import com.chute.android.avatarpicker.dao.impl.ImageDaoImpl;
import com.chute.android.avatarpicker.model.ImageItem;
import com.chute.sdk.api.authentication.GCAuthenticationFactory.AccountType;
import com.chute.sdk.utils.GCPreferenceUtil;
import com.darko.imagedownloader.ImageLoader;

/**
 * The {@link AvatarServicesAdapter} class fills the GridView with ImageViews
 * representing avatar services such as: Facebook, Twitter, Instagram and
 * Gravatar. NOTE: It must be called using the Activity context instead of
 * Application context.
 */
public class AvatarServicesAdapter extends BaseAdapter {

	public static final String TAG = AvatarServicesAdapter.class
			.getSimpleName();
	/**
	 * The Activity context
	 */
	private Context context;
	/**
	 * Supplies information about a display, such as its size, density, and font
	 * scaling.
	 */
	private final DisplayMetrics metrics;
	/**
	 * Inflater for the view recycling
	 */
	private static LayoutInflater inflater = null;
	/**
	 * The image loader
	 */
	public ImageLoader imageLoader;
	/**
	 * Image DAO for database management.
	 */
	public ImageDaoImpl imageDao;
	/**
	 * Array of services: Upload, Facebook, Twitter, Instagram, Gravatar.
	 */
	private final String[] serviceValues;
	/**
	 * Image item
	 */
	private ImageItem imageItem;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param serviceValues
	 */
	public AvatarServicesAdapter(Context context, String[] serviceValues) {
		this.context = context;
		this.serviceValues = serviceValues;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = ImageLoader.getLoader(context);
		metrics = context.getResources().getDisplayMetrics();
		imageDao = ImageDaoImpl.getInstance();
	}

	@Override
	public int getCount() {
		return serviceValues.length;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public static class ViewHolder {
		public ImageView serviceIcon;
		public TextView serviceTxt;
		public ImageView serviceSmall;

		public ViewHolder() {
		}

		public ViewHolder(ImageView serviceIcon, TextView serviceTxt,
				ImageView serviceSmall) {
			this.serviceIcon = serviceIcon;
			this.serviceSmall = serviceSmall;
			this.serviceTxt = serviceTxt;
		}

		public ImageView getServiceIcon() {
			return serviceIcon;
		}

		public void setServiceIcon(ImageView serviceIcon) {
			this.serviceIcon = serviceIcon;
		}

		public TextView getServiceTxt() {
			return serviceTxt;
		}

		public void setServiceTxt(TextView serviceTxt) {
			this.serviceTxt = serviceTxt;
		}

		public ImageView getServiceSmall() {
			return serviceSmall;
		}

		public void setServiceSmall(ImageView serviceSmall) {
			this.serviceSmall = serviceSmall;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ImageView serviceIcon;
		ImageView serviceSmall;
		TextView serviceTxt;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.grid_adapter_item, null);
			serviceIcon = (ImageView) vi.findViewById(R.id.serviceIcon);
			serviceSmall = (ImageView) vi.findViewById(R.id.serviceSmall);
			serviceTxt = (TextView) vi.findViewById(R.id.serviceTxt);
			serviceIcon.setLayoutParams(new RelativeLayout.LayoutParams(
					metrics.widthPixels / 4, metrics.widthPixels / 4));
			vi.setTag(new ViewHolder(serviceIcon, serviceTxt, serviceSmall));
		} else {
			ViewHolder holder = (ViewHolder) vi.getTag();
			serviceIcon = holder.getServiceIcon();
			serviceSmall = holder.getServiceSmall();
			serviceTxt = holder.getServiceTxt();
		}
		String service = serviceValues[position];

		if (service.equals("Facebook")) {
			serviceIcon.setTag(AccountType.FACEBOOK);
			if (GCPreferenceUtil.get().hasAccountId(
					(AccountType) serviceIcon.getTag())) {
				serviceTxt.setVisibility(View.INVISIBLE);
				serviceSmall.setVisibility(View.VISIBLE);
				serviceSmall.setBackgroundResource(R.drawable.facebook_small);
				String url = String.format(
						AvatarRestConstants.URL_GET_AVATAR,
						((AccountType) serviceIcon.getTag()).getName(),
						GCPreferenceUtil.get().getUidForAccount(
								(AccountType) serviceIcon.getTag()));
				imageItem = new ImageItem();
				imageItem.setFilePath(url.concat("?size=large"));
				imageItem.setService("facebook");
				imageDao.saveImage(context, imageItem);
				imageLoader
						.displayImage(url.concat("?size=large"), serviceIcon);
			} else {
				serviceTxt.setText(context.getResources().getString(
						R.string.txt_facebook));
				serviceIcon.setBackgroundResource(R.drawable.facebook);
				serviceSmall.setVisibility(View.INVISIBLE);
			}
		} else if (service.equals("Twitter")) {
			serviceIcon.setTag(AccountType.TWITTER);
			if (GCPreferenceUtil.get().hasAccountId(
					(AccountType) serviceIcon.getTag())) {
				serviceTxt.setVisibility(View.INVISIBLE);
				serviceSmall.setVisibility(View.VISIBLE);
				serviceSmall.setBackgroundResource(R.drawable.twitter_small);
				String url = String.format(
						AvatarRestConstants.URL_GET_AVATAR,
						((AccountType) serviceIcon.getTag()).getName(),
						GCPreferenceUtil.get().getUidForAccount(
								(AccountType) serviceIcon.getTag()));
				imageItem = new ImageItem();
				imageItem.setFilePath(url.concat("?size=large"));
				imageItem.setService("twitter");
				imageDao.saveImage(context, imageItem);
				imageLoader
						.displayImage(url.concat("?size=large"), serviceIcon);
			} else {
				serviceTxt.setText(context.getResources().getString(
						R.string.txt_twitter));
				serviceIcon.setBackgroundResource(R.drawable.twitter);
				serviceSmall.setVisibility(View.INVISIBLE);
			}
		} else if (service.equals("Instagram")) {
			serviceIcon.setTag(AccountType.INSTAGRAM);
			if (GCPreferenceUtil.get().hasAccountId(
					(AccountType) serviceIcon.getTag())) {
				serviceTxt.setVisibility(View.INVISIBLE);
				serviceSmall.setVisibility(View.VISIBLE);
				serviceSmall.setBackgroundResource(R.drawable.instagram_small);
				String url = String.format(
						AvatarRestConstants.URL_GET_AVATAR,
						((AccountType) serviceIcon.getTag()).getName(),
						GCPreferenceUtil.get().getUidForAccount(
								(AccountType) serviceIcon.getTag()));
				imageItem = new ImageItem();
				imageItem.setFilePath(url);
				imageItem.setService("instagram");
				imageDao.saveImage(context, imageItem);
				imageLoader.displayImage(url, serviceIcon);
			} else {
				serviceTxt.setText(context.getResources().getString(
						R.string.txt_instagram));
				serviceIcon.setBackgroundResource(R.drawable.instagram);
				serviceSmall.setVisibility(View.INVISIBLE);
			}
		} else if (service.equals("Upload")) {
			if (imageDao.getUploadImages(context, "upload").isEmpty()) {
				serviceIcon.setBackgroundResource(R.drawable.upload);
				serviceTxt.setText(context.getResources().getString(
						R.string.txt_upload));
				serviceSmall.setVisibility(View.INVISIBLE);
			} else {
				LinkedList<ImageItem> uploadImages = (LinkedList<ImageItem>) imageDao
						.getUploadImages(context, "upload");
				String lastImagePath = uploadImages.getLast().getFilePath();
				imageLoader.displayImage(lastImagePath, serviceIcon);
				serviceTxt.setVisibility(View.INVISIBLE);
				serviceSmall.setVisibility(View.VISIBLE);
				serviceSmall.setBackgroundResource(R.drawable.upload_small);
			}
		} else if (service.equals("Gravatar")) {
			if (imageDao.getGravatarImages(context, "gravatar").isEmpty()) {
				serviceIcon.setBackgroundResource(R.drawable.gravatar);
				serviceTxt.setText(context.getResources().getString(
						R.string.txt_gravatar));
				serviceSmall.setVisibility(View.INVISIBLE);
			} else {
				LinkedList<ImageItem> gravatarImages = (LinkedList<ImageItem>) imageDao
						.getGravatarImages(context, "gravatar");
				String lastImagePath = gravatarImages.getLast().getFilePath();
				imageLoader.displayImage(lastImagePath, serviceIcon);
				serviceTxt.setVisibility(View.INVISIBLE);
				serviceSmall.setVisibility(View.VISIBLE);
				serviceSmall.setBackgroundResource(R.drawable.gravatar_small);
			}
		}
		return vi;
	}

}
