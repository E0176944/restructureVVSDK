package com.sanofi.extraction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.opencsv.CSVWriter;
import com.sanofi.model.Response;
import com.sanofi.util.LoggerClass;
import com.sanofi.util.PostQuery;

@SuppressWarnings("unchecked")
public class ExtractObjectsCount {

	private static final Logger LOGGER = LoggerClass.getLogger();

	public void SendObjectsRequest(String url, String sessionID) throws IOException {


		//load the object api names from the text file into an arraylist
		ArrayList<String> objectsList = new ArrayList<>();
		
		//ArrayList to store the responses
		ArrayList <Response> responses = new ArrayList <Response>();
		
		//define the date format for file naming
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		
		
		objectsList = LoadObjectList();

		//create a for loop and start sending the request using PostQuery

		//create PostQuery object
		PostQuery postQuery = new PostQuery();

		for (String apiName : objectsList) {

			//build query
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("select id from ")
			.append(apiName)
			.append(" PAGESIZE 1");

			String query = queryBuilder.toString();

			//post the query to vault REST API
			try {
				String jsonResponse = postQuery.getVQLResponse(query, url, sessionID) ;
				Object objectRecordCount = null;
				objectRecordCount = new JSONParser().parse(jsonResponse);
				JSONObject jsonObject = (JSONObject) objectRecordCount;
				String responseStatus = (String) jsonObject.get("responseStatus");
				
				if(responseStatus.equalsIgnoreCase("FAILURE")&& jsonResponse !=null) {
					
					//System.out.println(jsonResponse);
					LOGGER.info("ERROR Response for Object - "+apiName+":"+jsonResponse);			
					JSONArray jsonArray = new JSONArray();
					jsonArray = (JSONArray) jsonObject.get("errors");
					JSONObject joName = (JSONObject) jsonArray.get(0);
					String message = (String) joName.get("message").toString();
					Response responseObj = new Response();
					responseObj.setCount("<<NA>>");
					responseObj.setMessage(message);
					responseObj.setObjectName(apiName);
					responseObj.setResponseStatus(responseStatus);					
					responses.add(responseObj);
					
					
				} else if (responseStatus.equalsIgnoreCase("SUCCESS")&& jsonResponse !=null) {


					LOGGER.info("SUCCESS Response for Object - "+apiName+":"+jsonResponse);	
					JSONObject jsonSuccessObject = (JSONObject) jsonObject.get("responseDetails");
					String count = String.valueOf((long)jsonSuccessObject.get("total"));
					Response responseObj = new Response();
					responseObj.setCount(count);
					responseObj.setMessage("<<NA>>");
					responseObj.setObjectName(apiName);
					responseObj.setResponseStatus(responseStatus);
					responses.add(responseObj);
					
				}
				
				else {
					//handling unknown errors
					LOGGER.info("Unknown Error Response for Object - "+apiName+":"+jsonResponse);
					JSONArray jsonArray = new JSONArray();
					jsonArray = (JSONArray) jsonObject.get("errors");
					JSONObject joName = (JSONObject) jsonArray.get(0);
					String message = (String) joName.get("message").toString();
					Response responseObj = new Response();
					responseObj.setCount("<<NA>>");
					responseObj.setMessage(message);
					responseObj.setObjectName(apiName);
					responseObj.setResponseStatus("Unknown Error");					
					responses.add(responseObj);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info("Request failed: " + e.toString());

			}
		}

		//write the Map list to a CSV file
		CSVWriter writer = null;
		try {
			//assign CSV writer
			String outputFilePath = System.getProperty("user.dir") + File.separator + "Object_Records_Count_"+ format.format(date)+".csv";
			//String outputFilePath = "C:\\Users\\E12345\\OneDrive - Sanofi\\data\\Veeva\\Scripts_tools\\Object_Records_Count_" + format.format(date) +".csv";
			writer = new CSVWriter (new FileWriter(outputFilePath));

			//print the header row
			LOGGER.info("Initiating the CSV Writer...");
			writer.writeNext(new String[] {"Object API Name", "Count", "Response Status", "Response Message"});

			//print the Map data in CSV
			for(Response response : responses) {
				writer.writeNext(new String[] {response.getObjectName(), String.valueOf(response.getCount()), response.getResponseStatus(), response.getMessage()});
				//LOGGER.info(response.getObjectName() + String.valueOf(response.getCount()) + response.getResponseStatus() + response.getMessage());
			}
			
			LOGGER.info("Compeleting the CSV Writing");

		} catch (IOException e) {

			e.printStackTrace();
			LOGGER.info("Failed to Write CSV: " + e.toString());
		}

		finally {
			if(writer!= null) {
				writer.close();
			}	

		}
		
		

	}

	public ArrayList<String> LoadObjectList() {

		String inputFileName = System.getProperty("user.dir") + File.separator + "inputfile.txt";
		// String inputFileName = "C:\\Users\\E12345\\OneDrive -
		// Sanofi\\data\\Veeva\\Scripts_tools\\inputfile.txt";
		ArrayList<String> apiNames = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
			String apiName;
			while ((apiName = br.readLine()) != null) {
				apiNames.add(apiName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("Failed to load inoutfile: " + e.toString());
		}

		return apiNames;

	}

}
