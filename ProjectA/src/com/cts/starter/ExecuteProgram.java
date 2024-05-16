package com.cts.starter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import com.cts.extraction.ExtractObjectsCount;
import com.cts.util.LoggerClass;
import com.cts.util.SessionManager;

public class ExecuteProgram {
	
	/*@author CTS
	 * Written on 3-May-2023
	 * Helps to collect the object records counts
	 */
	
	private static String url = null;
	private static String userID = null;
	private static String passWord = null;
	private static final Logger LOGGER = LoggerClass.getLogger();
	
	public static void main(String[] args) throws SecurityException, IOException {
	
		//Load the environment details and credentials from the property file
		ReadProperties();
		SessionManager sessionMgr = new SessionManager();
		//===========
		
		String sessionID = sessionMgr.getSession(url, userID, passWord);
		LOGGER.info("Session created for the application URL: "+url);

		//String sessionID = "";
		ExtractObjectsCount extractObjectCountObj = new ExtractObjectsCount();
		try {
				extractObjectCountObj.SendObjectsRequest(url, sessionID);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void ReadProperties() {
		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			//Load the config file from the same directory where Jar file is located
			String configFilePath = System.getProperty("user.dir") + File.separator + "config.properties";
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
