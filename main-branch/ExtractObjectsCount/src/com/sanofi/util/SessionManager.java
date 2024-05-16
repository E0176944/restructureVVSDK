package com.sanofi.util;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SessionManager {
	
	
	public final String getSession(String url, String userName, String passWord) {
		String jsonResponse=null;
		//assign variables
		String urlAuth = url.concat("auth");	
			
		//get session
		
		OkHttpClient httpClient = new OkHttpClient();
		
		
        RequestBody requestBody = new FormBody.Builder()
		        .add("username", userName)
		        .add("password", passWord)
		        .build();
		Request request = new Request.Builder()
                .url(urlAuth)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("accept", "application/json")
                .post(requestBody)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed : HTTP error code : " + response.code());
            }
            jsonResponse = response.body().string();
            
        } catch (IOException e) {
			
			e.printStackTrace();
		}
        return jsonResponse;

		}		


	}


