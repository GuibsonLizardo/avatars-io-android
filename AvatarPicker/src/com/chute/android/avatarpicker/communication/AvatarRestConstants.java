package com.chute.android.avatarpicker.communication;


/**
 * This class is a helper class that contains 
 * URL strings for managing the avatar API calls.
 *
 */
public class AvatarRestConstants {

	/**
	 * Base avatar URL
	 */
	public static final String BASE_URL = "http://avatars.io";
	/**
	 * URL needed for token request
	 */
	public static final String URL_UPLOAD_TOKEN = BASE_URL + "/v1/token";
	/**
	 * URL needed for avatar upload process completion
	 */
	public static final String URL_UPLOADS_COMPLETE = BASE_URL + "/v1/token/%s/complete";
	/**
	 * URL needed for getting the avatar
	 */
	public static final String URL_GET_AVATAR = BASE_URL + "/%s/%s";

}
