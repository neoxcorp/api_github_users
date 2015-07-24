package com.api_github_users.communicator;

import com.api_github_users.list_ui.User;
import com.api_github_users.utils.LogTag;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

class BackendCommunicatorStub implements BackendCommunicator {

	private String url;

	final String TAG_USER_ID = "id";
	final String TAG_LOGIN = "login";
	final String TAG_AVATAR_URL = "avatar_url";
	final String TAG_HTML_URL = "html_url";

	final String BASE_API_URL = "https://api.github.com/users";
	final String SINCE = "?since=";
	final String PER_PAGE = "&per_page=30";

	// https://api.github.com/users?since=0&per_page=30

	@Override
	public ArrayList<User> postGetData(int sinceId) throws InterruptedException {
		ArrayList<User> userArrayList = new ArrayList<>();

		url = BASE_API_URL + SINCE + sinceId + PER_PAGE;

		LogTag.d(url);

		String response = getJSON(url, 1500);

		JSONArray dataJsonArray;
		JSONObject userJSON;

		try {
			dataJsonArray = new JSONArray(response);

			for (int i = 0; i < dataJsonArray.length(); i++) {
				userJSON = dataJsonArray.getJSONObject(i);
				userArrayList.add(new User(userJSON.getInt(TAG_USER_ID), userJSON.getString(TAG_LOGIN),
						userJSON.getString(TAG_AVATAR_URL), userJSON.getString(TAG_HTML_URL)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return userArrayList;
	}

	private String getJSON(String url, int timeout) {
		HttpURLConnection c = null;
		try {
			URL u = new URL(url);
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.setConnectTimeout(timeout);
			c.setReadTimeout(timeout);
			c.connect();
			int status = c.getResponseCode();

			switch (status) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = br.readLine()) != null) {
						sb.append(line+"\n");
					}
					br.close();
					return sb.toString();
			}
		} catch (MalformedURLException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
		} finally {
			if (c != null) {
				try {
					c.disconnect();
				} catch (Exception ex) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		return null;
	}
}