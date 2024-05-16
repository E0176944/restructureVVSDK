package com.api;

import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExtractionApi {

	public static String getData(String sessionId, String urlValue, int pageoffset, Logger logger, String apiVersion) {

		Request request = null;
		Response response = null;
		String responseResult = null;
		String url = null;

		try {
			OkHttpClient client = new OkHttpClient();
			url = urlValue + apiVersion + "objects/users?vaults=all&exclude_vault_membership=false&start=" + pageoffset;

			request = new Request.Builder().url(url).get().addHeader("Authorization", sessionId).build();
			response = client.newCall(request).execute();
			responseResult = response.body().string();

		} catch (Exception e) {
			logger.info("ExtractionApi getData Error = " + e.toString());
			e.printStackTrace();

		} finally {
			response.close();
		}

		return responseResult;
	}

	public static String getVaultName(String sessionId, String urlValue, Logger logger, String apiVersion) {

		Request request = null;
		Response response = null;
		String responseResult = null;
		String url = null;

		try {
			OkHttpClient client = new OkHttpClient();
			url = urlValue + apiVersion + "objects/domain?include_application=true";

			request = new Request.Builder().url(url).get().addHeader("Authorization", sessionId).build();
			response = client.newCall(request).execute();
			responseResult = response.body().string();

		} catch (Exception e) {

			logger.info("ExtractionApi getVaultName Error = " + e.toString());
			e.printStackTrace();

		} finally {
			response.close();
		}

		return responseResult;
	}

	public static String getSecuritypolicies(String sessionId, String urlValue, Logger logger, String apiVersion) {

		Request request = null;
		Response response = null;
		String responseResult = null;
		String url = null;

		try {
			OkHttpClient client = new OkHttpClient();
			url = urlValue + apiVersion + "objects/securitypolicies";

			request = new Request.Builder().url(url).get().addHeader("Authorization", sessionId).build();
			response = client.newCall(request).execute();
			responseResult = response.body().string();

		} catch (Exception e) {

			logger.info("ExtractionApi getSecuritypolicies Error = " + e.toString());
			e.printStackTrace();

		} finally {
			response.close();
		}

		return responseResult;
	}

	public static String getUserName(String sessionId, String urlValue, Logger logger, String apiVersion,
			long pageoffset) {

		String q = "SELECT id, user_email__v,user_first_name__v,user_last_name__v FROM users PAGESIZE 1000 PAGEOFFSET "
				+ pageoffset;

		Request request = null;
		Response response = null;
		String responseResult = null;
		String url = null;

		try {
			OkHttpClient client = new OkHttpClient();
			url = urlValue + apiVersion + "query";
			MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

			RequestBody body = RequestBody.create(mediaType, "q=" + q);

			request = new Request.Builder().url(url).method("POST", body).addHeader("Authorization", sessionId)
					.addHeader("Content-Type", "application/x-www-form-urlencoded").build();

			response = client.newCall(request).execute();
			responseResult = response.body().string();

		} catch (Exception e) {
			logger.info("ExtractionApi getUserName Error = " + e.toString());
			e.printStackTrace();
			logger.info(
					"**************************************************END**************************************************");

		} finally {
			response.close();
		}
		return responseResult;
	}

}
