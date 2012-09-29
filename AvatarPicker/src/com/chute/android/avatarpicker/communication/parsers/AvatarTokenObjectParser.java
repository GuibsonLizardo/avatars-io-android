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
package com.chute.android.avatarpicker.communication.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import com.chute.sdk.model.GCUploadToken;
import com.chute.sdk.model.inner.Meta;
import com.chute.sdk.model.inner.UploadInfo;
import com.chute.sdk.parsers.base.GCHttpResponseParser;

/**
 * The {@link AvatarTokenObjectParser} class encapsulates the parsed JSon
 * response from the server to {@link GCUploadToken}.
 */
public class AvatarTokenObjectParser implements
		GCHttpResponseParser<GCUploadToken> {

	@Override
	public GCUploadToken parse(String responseBody) throws JSONException {
		GCUploadToken token = new GCUploadToken();
		Meta meta = new Meta();
		JSONObject obj = new JSONObject(responseBody);
		meta.setVersion(obj.getJSONObject("meta").getInt("version"));
		meta.setCode(obj.getJSONObject("meta").getInt("code"));
		token.setMeta(meta);

		obj = obj.getJSONObject("data");
		token.setSourceUrl(obj.getJSONObject("data").getString("source_url"));
		token.setUrl(obj.getString("url"));
		token.setId(obj.getString("id"));
		JSONObject info = obj.optJSONObject("upload_info");
		if (info != null && info.length() > 0) {
			UploadInfo uploadInfo = new UploadInfo();
			uploadInfo.setSignature(info.getString("signature"));
			uploadInfo.setDate(info.getString("date"));
			uploadInfo.setUploadUrl(info.getString("upload_url"));
			uploadInfo.setFilepath(info.getString("file_path"));
			uploadInfo.setContentType(info.getString("content_type"));
			uploadInfo.setType(info.getString("type"));
			token.setUploadInfo(uploadInfo);
		}
		return token;
	}
}
