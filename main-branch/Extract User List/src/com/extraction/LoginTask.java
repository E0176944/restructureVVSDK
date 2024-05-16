package com.extraction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.api.ExtractionApi;
import com.model.UserDetails;
import com.util.HelperFile;

public class LoginTask extends SwingWorker<Void, Void> {
	
	private String userName;
	private String pwdValue;
	private String urlvalue;
	private LoginForm gui;
	boolean sessionAlive = false;
	
	private String name;
	private String id;
	private String sessionIdValue;
	private String apiVersion;
	private Logger logger;
	private HelperFile helperFile;
	private Map<String,String> vaultName;
	private Map<String,String> securitypoliciesName;

	public LoginTask(LoginForm gui, String userName, String pwdValue, String urlvalue,String apiVersion, Logger logger) {
		this.userName = userName;
		this.pwdValue = pwdValue;
		this.urlvalue = urlvalue;
		this.gui = gui;
		this.apiVersion = apiVersion;
		this.logger = logger;
		helperFile = new HelperFile();

	}

	@Override
	protected Void doInBackground() throws Exception {

		try {

			logger.info("Url Value = " + urlvalue);

			Map<String, String> map = helperFile.userValidation(userName, pwdValue, urlvalue, logger, apiVersion);

			String status = map.get("status");

			if (status.equalsIgnoreCase("SUCCESS")) {

				sessionIdValue = map.get("sessionId");
				String vaultId = map.get("vaultId");

				logger.info("Session Value = " + sessionIdValue);

				if (sessionIdValue != null) {

					List<UserDetails> list = helperFile.isSessionAliveDetails(urlvalue, sessionIdValue, logger,
							apiVersion, vaultId);

					if (list != null && list.size() > 0) {

						UserDetails userDetails = list.get(0);
						sessionAlive = userDetails.isAlive();
						boolean isActive = userDetails.isActive__v();

						if (sessionAlive) {

							if (isActive) {
								
								name = userDetails.getUser_first_name__v() + " " + userDetails.getUser_last_name__v();
								id = String.valueOf(userDetails.getId());
								
								getVaultName();
								getSecuritypolicies();
								
							} else {

								JOptionPane.showMessageDialog(gui,
										"Your profile is inactive. Please contact vault admin.");
								gui.hide();
							}

						} else {

							JOptionPane.showMessageDialog(gui, "Invalid Environment");
							gui.hide();
						}

					} else {

						JOptionPane.showMessageDialog(gui, "Invalid Environment");
						gui.hide();
					}

				} else {

					JOptionPane.showMessageDialog(gui, "Invalid Username or Password");
					gui.hide();
				}
			} else {
				String type = map.get("type");
				String message = map.get("message");
				JOptionPane.showMessageDialog(gui, type + ", " + message);
				gui.hide();

			}

		} catch (Exception e) {
			logger.info("LoginTask  doInBackground Exception = " + e.getMessage());

			JOptionPane.showMessageDialog(gui, "Error executing login task: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			gui.hide();
			e.printStackTrace();
			cancel(true);

		}
		return null;
	}

	@Override
	protected void done() {
		if (!isCancelled()) {
			if (sessionAlive) {
				ExtractForm page = new ExtractForm();
				page.sesUserUrl(sessionIdValue, userName, pwdValue, urlvalue, logger, name, id, apiVersion, vaultName, securitypoliciesName);
				page.setVisible(true);
				gui.dispose();
			}
		}
	}
	
	
	
	private void getVaultName(){
		
		String json = ExtractionApi.getVaultName(sessionIdValue, urlvalue, logger, apiVersion);
		vaultName = new HashMap<String, String>();
		
		try{
			Object documentParseResponse = null;
			documentParseResponse = new JSONParser().parse(json);
			JSONObject jsonDocObject = (JSONObject) documentParseResponse;
			JSONObject mainJo = (JSONObject) jsonDocObject.get("domain__v");
			JSONArray mainJa = (JSONArray) mainJo.get("vaults__v");
			
			Iterator<?> jaRetrive = mainJa.iterator();
			while (jaRetrive.hasNext()) {
				JSONObject jsonDocObj = (JSONObject) jaRetrive.next();
				
				String name = "";
				String id = "";
				
				if (jsonDocObj.containsKey("vault_name__v")) {
					name = (String) jsonDocObj.get("vault_name__v");
				}
				
				if (jsonDocObj.containsKey("id")) {
					id = String.valueOf((long) jsonDocObj.get("id"));
				}
				
				if(name != null && !name.equals("") && id != null && !id.equals("")){
					vaultName.put(id, name);
				}
			}
			
		}catch (Exception e) {
		}
		
		
	}
	
	private void getSecuritypolicies(){
		
		String json = ExtractionApi.getSecuritypolicies(sessionIdValue, urlvalue, logger, apiVersion);
		securitypoliciesName = new HashMap<String, String>();
		
		try{
			Object documentParseResponse = null;
			documentParseResponse = new JSONParser().parse(json);
			JSONObject jsonDocObject = (JSONObject) documentParseResponse;
			JSONArray mainJa = (JSONArray) jsonDocObject.get("security_policies__v");
			
			Iterator<?> jaRetrive = mainJa.iterator();
			while (jaRetrive.hasNext()) {
				JSONObject jsonDocObj = (JSONObject) jaRetrive.next();
				
				String name = "";
				String id = "";
				
				if (jsonDocObj.containsKey("label__v")) {
					name = (String) jsonDocObj.get("label__v");
				}
				
				if (jsonDocObj.containsKey("name__v")) {
					id = (String) jsonDocObj.get("name__v");
				}
				
				if(name != null && !name.equals("") && id != null && !id.equals("")){
					securitypoliciesName.put(id, name);
				}
			}
			
			
		}catch (Exception e) {
		}
	}
	
}
