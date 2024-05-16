package com.extraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.swing.SwingWorker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.api.ExtractionApi;
import com.model.UserDataModel;
import com.model.VaultMembershipModel;
import com.util.CreateCSV;

public class ExtractionTask extends SwingWorker<Void, Void> {

	public ExtractForm gui;
	private String sessionIdValue, urlValue, apiVersion;
	private Logger logger;
	private Map<String, String> vaultName;
	private Map<String, String> securitypoliciesName;
	private Map<String, String> userNameMap;
	private Map<String, String> userEmailMap;
	private String fileDir;

	long startTime;

	List<UserDataModel> mainList;

	String folderName = "";

	public ExtractionTask(ExtractForm gui, String sessionIdValue, String urlValue, Logger logger, String apiVersion,
			Map<String, String> vaultName, Map<String, String> securitypoliciesName,String fileDir) {
		this.gui = gui;
		this.sessionIdValue = sessionIdValue;
		this.urlValue = urlValue;
		this.logger = logger;
		this.apiVersion = apiVersion;
		this.vaultName = vaultName;
		this.securitypoliciesName = securitypoliciesName;
		this.fileDir = fileDir;
	}

	@Override
	protected Void doInBackground() throws Exception {
		try {

			gui.submitButton.setEnabled(false);

			logger.info("*********************Extraction Start*******************");
			System.out.println("*********************Extraction Start*******************");

			startTime = System.currentTimeMillis();
			mainList = new ArrayList<>();

			boolean userpage = true;
			long pageoffset = 0;
			long usersize = 0;
			userNameMap = new HashMap<String, String>();
			userEmailMap = new HashMap<String, String>();

			do {

				System.out.println("Fetching user data pageoffset = " + pageoffset);

				String users = ExtractionApi.getUserName(sessionIdValue, urlValue, logger, apiVersion, pageoffset);

				Object userParseResponse = null;
				userParseResponse = new JSONParser().parse(users);

				JSONObject jsonObject = (JSONObject) userParseResponse;
				JSONObject jo = (JSONObject) jsonObject.get("responseDetails");

				pageoffset = (Long) jo.get("pageoffset");
				usersize = (Long) jo.get("size");

				if (usersize == 1000) {
					pageoffset = pageoffset + 1000;
				} else {
					userpage = false;
				}

				JSONArray jsonArray = new JSONArray();
				jsonArray = (JSONArray) jsonObject.get("data");

				if (jsonArray != null && jsonArray.size() > 0) {
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject joName = (JSONObject) jsonArray.get(i);
						String email = (String) joName.get("user_email__v");
						String id = (String) joName.get("id").toString();
						String fname = (String) joName.get("user_first_name__v").toString();
						String lname = (String) joName.get("user_last_name__v").toString();
						userNameMap.put(id, fname + " " + lname);
						userEmailMap.put(id, email);
					}
				}

			} while (userpage == true);

			String dataResponse;
			JSONArray jsonDocArray;
			long size = 0;
			int startValue = 0;
			boolean page = true;

			do {
				System.out.println("***Loading user data cummulatively. Total records loaded now  = " + startValue + "***");

				dataResponse = ExtractionApi.getData(sessionIdValue, urlValue, startValue, logger, apiVersion);

				Object dataParseResponse = null;
				dataParseResponse = new JSONParser().parse(dataResponse);

				JSONObject jsonObject = (JSONObject) dataParseResponse;

				size = (Long) jsonObject.get("size");

				if (size == 200) {
					startValue = startValue + 200;
				} else {
					page = false;
				}

				jsonDocArray = new JSONArray();
				jsonDocArray = (JSONArray) jsonObject.get("users");

				getAllData(jsonDocArray);

				CreateCSV.createFileCSV(mainList, logger, fileDir);

			} while (size == 200 && page == true);

			if (page == false) {

				if (mainList != null && mainList.size() > 0) {

					CreateCSV.createFileCSV(mainList, logger, fileDir);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("ExtractionTask doInBackground Error - " + e.toString());
		}
		return null;
	}

	@Override
	protected void done() {
		super.done();

		gui.submitButton.setEnabled(true);

		long finishTime = System.currentTimeMillis();
		long timeElapsed = finishTime - startTime;

		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeElapsed),
				TimeUnit.MILLISECONDS.toMinutes(timeElapsed)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeElapsed)),
				TimeUnit.MILLISECONDS.toSeconds(timeElapsed)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeElapsed)));

		gui.hide();

		logger.info("Extraction is finished." + " The total time taken for extraction is : " + hms);
		System.out.println("Extraction is finished." + " The total time taken for extraction is : " + hms);
	}

	/**
	 * Document Filter Part
	 */

	public void getAllData(JSONArray jsonArray) {

		JSONObject jsonObj;

		try {
			Iterator<?> docRetrive = jsonArray.iterator();

			while (docRetrive.hasNext()) {
				jsonObj = (JSONObject) docRetrive.next();
				Object jsonObject = jsonObj.get("user");
				JSONObject jsonObjectIdValue = (JSONObject) jsonObject;

				UserDataModel adm = new UserDataModel();

				if (jsonObjectIdValue.containsKey("user_name__v")) {

					if (jsonObjectIdValue.get("user_name__v") != null) {
						adm.setUser_name__v((String) jsonObjectIdValue.get("user_name__v").toString());
					} else {
						adm.setUser_name__v("");
					}
				} else {
					adm.setUser_name__v("");
				}

				if (jsonObjectIdValue.containsKey("user_email__v")) {
					if (jsonObjectIdValue.get("user_email__v") != null) {
						adm.setUser_email__v((String) jsonObjectIdValue.get("user_email__v").toString());
					} else {
						adm.setUser_email__v("");
					}
				} else {
					adm.setUser_email__v("");
				}

				if (jsonObjectIdValue.containsKey("security_policy_id__v")) {
					if (jsonObjectIdValue.get("security_policy_id__v") != null) {

						String sid = (String) jsonObjectIdValue.get("security_policy_id__v").toString();
						adm.setSecurity_policy_id__v(sid);
						adm.setSecurity_policy_name(securitypoliciesName.get(sid));

					} else {
						adm.setSecurity_policy_id__v("");
						adm.setSecurity_policy_name("");
					}
				} else {
					adm.setSecurity_policy_id__v("");
					adm.setSecurity_policy_name("");
				}

				if (jsonObjectIdValue.containsKey("id")) {
					if (jsonObjectIdValue.get("id") != null) {
						adm.setId((String) jsonObjectIdValue.get("id").toString());
					} else {
						adm.setId("");
					}
				} else {
					adm.setId("");
				}

				if (jsonObjectIdValue.containsKey("federated_id__v")) {
					if (jsonObjectIdValue.get("federated_id__v") != null) {
						adm.setFederated_id__v((String) jsonObjectIdValue.get("federated_id__v").toString());
					} else {
						adm.setFederated_id__v("");
					}
				} else {
					adm.setFederated_id__v("0");
				}

				if (jsonObjectIdValue.containsKey("last_login__v")) {
					if (jsonObjectIdValue.get("last_login__v") != null) {
						adm.setLast_login__v((String) jsonObjectIdValue.get("last_login__v").toString());
					} else {
						adm.setLast_login__v("");
					}
				} else {
					adm.setLast_login__v("");
				}

				if (jsonObjectIdValue.containsKey("created_by__v")) {
					if (jsonObjectIdValue.get("created_by__v") != null) {
						String userId = (String) jsonObjectIdValue.get("created_by__v").toString();
						adm.setCreated_by(userId);
						String name = userNameMap.get(userId);
						if (name != null) {
							adm.setCreated_by_name(name);
						} else {
							adm.setCreated_by_name("");
						}

						String email = userEmailMap.get(userId);
						if (email != null) {
							adm.setCreated_by_email(email);
						} else {
							adm.setCreated_by_email("");
						}
					} else {
						adm.setCreated_by("");
						adm.setCreated_by_name("");
						adm.setCreated_by_email("");
					}
				} else {
					adm.setCreated_by("");
					adm.setCreated_by_name("");
					adm.setCreated_by_email("");
				}

				if (jsonObjectIdValue.containsKey("vault_membership")) {

					Object obj = jsonObjectIdValue.get("vault_membership");
					JSONArray ja = (JSONArray) obj;

					if (ja != null && ja.size() > 0) {
						Iterator<?> jacRetrive = ja.iterator();
						JSONObject jo;
						List<VaultMembershipModel> vmList = new ArrayList<>();

						while (jacRetrive.hasNext()) {
							jo = (JSONObject) jacRetrive.next();
							VaultMembershipModel vm = new VaultMembershipModel();
							boolean check = (boolean) jo.get("active__v");
							String id = String.valueOf((long) jo.get("id"));
							if (check) {
								vm.setId(id);
								vm.setName(vaultName.get(id));
								vmList.add(vm);
							}

						}
						adm.setVault_membership(vmList);
					}
				}

				mainList.add(adm);

			}

		} catch (Exception e1) {
			logger.info("ExtractionTask getAlldocument Error - " + e1.toString());
		}

	}

}
