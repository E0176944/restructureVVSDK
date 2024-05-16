package com.cts.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerClass {
	
	private static final Logger LOGGER = Logger.getLogger(LoggerClass.class.getName());
	private static FileHandler fileHandler;
	
	static {
		try {
			//set the date format
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			//create log file
			FileHandler fileHandler = new FileHandler(System.getProperty("user.dir") + File.separator + "extractionLog_" + format.format(date) +".log", true);
			fileHandler.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fileHandler);
			LOGGER.info("Logger file created...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Logger getLogger() {
		return LOGGER;
	}

}
