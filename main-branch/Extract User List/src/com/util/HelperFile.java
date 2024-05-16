package com.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.model.UserDetails;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HelperFile {

	public Map<String, String> userValidation(String userValue, String pwdValue, String urlvalue, Logger logger,
			String apiVersion) {
		String userAvailableorNot = "";
		Map<String, String> map = new HashMap<>();

		try {
			HttpClient httpClient = null;

			httpClient = new DefaultHttpClient();
			String url = urlvalue + apiVersion + "auth";
			logger.info("Login url value = " + url);
			HttpPost httpPost = new HttpPost(url);
			httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
			httpPost.addHeader("accept", "application/json");

			StringBuilder reqBody = new StringBuilder();

			reqBody.append("username=" + userValue);
			reqBody.append("&password=" + pwdValue);
			String dataToSend = reqBody.toString();

			StringEntity se = new StringEntity(dataToSend);

			httpPost.setEntity(se);
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream in = entity.getContent();
				userAvailableorNot = convertStreamToString(in, logger);
				map = sessionIdValue(logger, userAvailableorNot);

			}

		} catch (Exception exc) {
			logger.info("LoginForm userValidation Exception = " + exc.toString());
		} finally {

		}

		return map;
	}

	private Map<String, String> sessionIdValue(Logger logger, String userAvailableorNot) {

		Object responseResult;
		JSONArray arrayResponse;
		String sessionId = null;
		Map<String, String> map = new HashMap<>();

		try {
			Object authParseResponse = new JSONParser().parse(userAvailableorNot);
			JSONObject jsonresonse = (JSONObject) authParseResponse;
			JSONParser parser = new JSONParser();
			responseResult = parser.parse(jsonresonse.toString());
			JSONObject jo = (JSONObject) responseResult;
			String status = (String) jo.get("responseStatus");

			if (status.equalsIgnoreCase("SUCCESS")) {
				sessionId = (String) jo.get("sessionId");
				long vaultId = (Long) jo.get("vaultId");
				map.put("status", status);
				map.put("type", "");
				map.put("message", "");
				map.put("sessionId", sessionId);
				map.put("vaultId", String.valueOf(vaultId));
			} else {
				arrayResponse = new JSONArray();
				arrayResponse = (JSONArray) jo.get("errors");
				JSONObject jobj = (JSONObject) arrayResponse.get(0);
				String type = (String) jobj.get("type");
				String message = (String) jobj.get("message");
				map.put("status", status);
				map.put("type", type);
				map.put("message", message);
				map.put("sessionId", "");
				map.put("vaultId", "0");
			}
		} catch (ParseException e) {
			logger.info("LoginForm sessionIdValue Exception = " + e.toString());
		}

		return map;
	}

	public boolean isSessionAlive(String vaultURL, String vaultSession, Logger logger, String apiVersion) {

		Request request = null;
		Response response = null;
		OkHttpClient client = new OkHttpClient();
		boolean sessionAlive = false;
		if (vaultSession == null) {
			return false;
		}
		String validateUserSessionEndpoint = vaultURL + apiVersion + "objects/users/me";
		request = new Request.Builder().url(validateUserSessionEndpoint).addHeader("Authorization", vaultSession).get()
				.build();
		String jasonResponse = null;
		try {
			response = client.newCall(request).execute();
			jasonResponse = response.body().string();
			org.json.JSONObject jsonObject = new org.json.JSONObject(jasonResponse);
			String reponseStatus = jsonObject.getString("responseStatus");
			if (reponseStatus.equalsIgnoreCase("success")) {
				sessionAlive = true;

			} else {
				sessionAlive = false;
			}
		} catch (Exception e) {
			logger.info("HelperFile isSessionAlive Exception = " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return sessionAlive;
	}

	public List<UserDetails> isSessionAliveDetails(String vaultURL, String vaultSession, Logger logger,
			String apiVersion, String vaultId) {

		List<UserDetails> list = new ArrayList<>();

		Request request = null;
		Response response = null;
		OkHttpClient client = new OkHttpClient();
		boolean sessionAlive = false;

		if (vaultSession == null) {
			return null;
		}

		String validateUserSessionEndpoint = vaultURL + apiVersion + "objects/users/me";
		request = new Request.Builder().url(validateUserSessionEndpoint).addHeader("Authorization", vaultSession).get()
				.build();
		String jasonResponse = null;
		try {
			response = client.newCall(request).execute();
			jasonResponse = response.body().string();
			org.json.JSONObject jsonObject = new org.json.JSONObject(jasonResponse);
			String reponseStatus = jsonObject.getString("responseStatus");

			if (reponseStatus.equalsIgnoreCase("success")) {
				sessionAlive = true;

				org.json.JSONArray ja = jsonObject.getJSONArray("users");
				org.json.JSONObject userObj = ja.getJSONObject(0);
				org.json.JSONObject userObjDetails = userObj.getJSONObject("user");
																			
				UserDetails userDetails = new UserDetails();
				userDetails.setAlive(sessionAlive);
				userDetails.setId(userObjDetails.getLong("id"));
				userDetails.setUser_name__v(userObjDetails.getString("user_name__v"));
				userDetails.setUser_first_name__v(userObjDetails.getString("user_first_name__v"));
				userDetails.setUser_last_name__v(userObjDetails.getString("user_last_name__v"));
				userDetails.setUser_email__v(userObjDetails.getString("user_email__v"));
				userDetails.setActive__v(userObjDetails.getBoolean("active__v"));
				userDetails.setIs_domain_admin__v(userObjDetails.getBoolean("is_domain_admin__v"));
				userDetails.setDomain_active__v(userObjDetails.getBoolean("domain_active__v"));
				userDetails.setSecurity_profile__v(userObjDetails.getString("security_profile__v"));
				userDetails.setLicense_type__v(userObjDetails.getString("license_type__v"));				
				list.add(userDetails);
			} else {
				sessionAlive = false;
				UserDetails userDetails = new UserDetails();
				userDetails.setAlive(sessionAlive);
				userDetails.setId((long) 0);
				userDetails.setUser_name__v("");
				userDetails.setUser_first_name__v("");
				userDetails.setUser_last_name__v("");
				userDetails.setUser_email__v("");
				userDetails.setActive__v(false);
				userDetails.setIs_domain_admin__v(false);
				userDetails.setDomain_active__v(false);
				userDetails.setSecurity_profile__v("");
				userDetails.setLicense_type__v("");
				list.add(userDetails);
			}

		} catch (Exception e) {
			logger.info("HelperFile isSessionAliveDetails Exception = " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return list;
	}

	private static String convertStreamToString(InputStream is, Logger logger) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		String line = null;

		try {
			// Case 1
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			logger.info("convertStreamToString" + e.toString());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				logger.info("convertStreamToString" + e.toString());
			}
		}
		return sb.toString();

	}

}