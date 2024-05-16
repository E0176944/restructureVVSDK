package com.sanofi.starter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.sanofi.extraction.ExtractObjectsCount;
import com.sanofi.util.LoggerClass;
import com.sanofi.util.SessionManager;

public class ExecuteProgram {
	
	private static String url = null;
	private static String userID = null;
	private static String passWord = null;
	private static final Logger LOGGER = LoggerClass.getLogger();
	
	public static void main(String[] args) throws SecurityException, IOException {
	
		//Load the environment details and credentials from the property file
		ReadProperties();
		SessionManager sessionMgr = new SessionManager();
		
		
		String sessionID = sessionMgr.getSession(url, userID, passWord);
		System.out.println("SessionID:"+sessionID);
		

		//String sessionID = "";
		ExtractObjectsCount extractObjectCountObj = new ExtractObjectsCount();
		try {
				if(sessionID != null) {
					LOGGER.info("Session created for the application URL: "+url);
					extractObjectCountObj.SendObjectsRequest(url, sessionID);
				}
				
				else {
					LOGGER.info("Session ID not created for the application URL: "+url);
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void ReadProperties() {
		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			//String configFilePath = System.getProperty("user.dir") + File.separator + "config.properties";
			String configFilePath = "C:\\Users\\E0176944\\OneDrive - Sanofi\\data\\Veeva\\Scripts_tools\\extractor_tool\\config.properties";
			fileInputStream = new FileInputStream(configFilePath);
			properties.load(fileInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		url = properties.getProperty("url");
		userID = properties.getProperty("userID");
		passWord = properties.getProperty("passWord");
		
	}

}
