package com.Alot.reminder;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookiePolicy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.util.Log;

public class ApiConnector {
	private String url;
	private MultipartEntity entity;

	public ApiConnector(String URL) {
		this.url = URL;
		entity = new MultipartEntity();
	}

	@SuppressLint("NewApi")
	public JSONArray getBuyerInfo() {

		HttpEntity httpEntity = null;

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
					CookiePolicy.ACCEPT_ALL);
			// client,
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			httpEntity = httpResponse.getEntity();
		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		JSONArray jsonArray = null;
		if (httpEntity != null) {
			try {
				String entityResponse = EntityUtils.toString(httpEntity);
				Log.e("Entity Response", entityResponse);
				jsonArray = new JSONArray(entityResponse);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return jsonArray;
	}

	public void addString(String key, String value)
			throws UnsupportedEncodingException {
		entity.addPart(key, new StringBody(value));
	}

	public void addFile(String key, File file) {
		entity.addPart(key, new FileBody(file));
	}

	public JSONArray executePost() throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(this.url);
		httppost.setEntity(entity);
		HttpResponse response = httpclient.execute(httppost);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				response.getEntity().getContent(), "UTF-8"));
		String sResponse;
		StringBuilder s = new StringBuilder();

		while ((sResponse = reader.readLine()) != null) {
			s = s.append(sResponse);
		}
		return new JSONArray(s.toString());
	}
}
