package com.cts.util;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostQuery {
	
	
	
	public String getVQLResponse (String query, String url, String sessionId) {
		
		String queryResponse = null;
		
		
		//assign variables
		String queryURL = url.concat("query");
		
		
		OkHttpClient httpClient = new OkHttpClient();
		
		
        RequestBody requestBody = new FormBody.Builder()
		        .add("q", query)
		        .build();
        
		Request request = new Request.Builder()
                .url(queryURL)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("accept", "application/json")
                .addHeader("Authorization", sessionId)
                .post(requestBody)
                .build();
		
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed : HTTP error code : " + response.code());
            }
            
            queryResponse = response.body().string();
           
            
        } catch (IOException e) {
			
			e.printStackTrace();
		}
		return queryResponse;
		
	}

}
