package com.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.model.UserDataModel;
import com.model.VaultMembershipModel;
import com.opencsv.CSVWriter;

public class CreateCSV {

	public static void createFileCSV(List<UserDataModel> keyFieldList, Logger logger, String fileDir) {
		
		Set<String> keyList = new LinkedHashSet<String>();
		Map<String, String> valueList = null;
		List<Map<String, String>> mainList = new ArrayList<Map<String, String>>();

		keyList.add("User Name");
		keyList.add("User Email");
		keyList.add("Security Policy");
		keyList.add("Security Policy Name");
		keyList.add("Id");
		keyList.add("Federated Id");
		keyList.add("Last Login");
		keyList.add("Created By");
		keyList.add("Created By Email");
		keyList.add("Created By Name");
		keyList.add("Vaults where Active");
		keyList.add("Vaults name");

		for (UserDataModel udm : keyFieldList) {
			List<VaultMembershipModel> vm = udm.getVault_membership();

			if (vm != null && vm.size() > 0) {
				valueList = new HashMap<>();

				valueList.put("User Name", udm.getUser_name__v());
				valueList.put("User Email", udm.getUser_email__v());
				valueList.put("Security Policy", udm.getSecurity_policy_id__v());
				valueList.put("Security Policy Name", udm.getSecurity_policy_name());
				valueList.put("Id", udm.getId());
				valueList.put("Federated Id", udm.getFederated_id__v());
				valueList.put("Last Login", udm.getLast_login__v());
				valueList.put("Created By", udm.getCreated_by());
				valueList.put("Created By Email", udm.getCreated_by_email());
				valueList.put("Created By Name", udm.getCreated_by_name());
				
				String ids = "";
				String names = "";
				for (VaultMembershipModel vmm : vm) {
					ids = ids + vmm.getId() + ";";
					names = names + vmm.getName() + ";";
				}
				StringBuffer sb = new StringBuffer(ids);
				sb.deleteCharAt(sb.length() - 1);
				valueList.put("Vaults where Active", String.valueOf(sb));
				
				StringBuffer sbName = new StringBuffer(names);
				sbName.deleteCharAt(sbName.length() - 1);
				valueList.put("Vaults name", String.valueOf(sbName));

				mainList.add(valueList);
			}
		}

		try {
			createFileCSV(keyList, mainList, logger, fileDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void createFileCSV(Set<String> keyFieldList, List<Map<String, String>> mainList, Logger logger, String fileDir)
			throws IOException {

		Map<String, String> valFieldList;

		File rootFolder = new File("C://Sandbox_Active_Users_File");
		if (!rootFolder.exists()) {
			rootFolder.mkdir();
		}

		String csvFilePath = fileDir + "//" + "active_user_report.csv";
		File file = new File(csvFilePath);
		CSVWriter writer = null;

		try {
			FileWriter outputfile = new FileWriter(file);
			writer = new CSVWriter(outputfile);
			String keyCsvLine;
			String valCsvLine;
			keyCsvLine = setToStringMaterial(keyFieldList);
			String[] keyColumn = keyCsvLine.split(",");
			writer.writeNext(keyColumn);

			for (int i = 0; i < mainList.size(); i++) {

				valFieldList = mainList.get(i);

				ArrayList<String> list = new ArrayList<String>();

				for (String s : keyFieldList) {
					String key = s;
					String value = valFieldList.get(key);

					if (value != null && !value.isEmpty()) {
						list.add(value);
					} else {
						list.add("");
					}

				}

				valCsvLine = ArrayListtoStringMaterial(list);
				String[] valColumn = valCsvLine.split(",");
				
				writer.writeNext(valColumn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("CreateCSV- createFileCSV- Error" + e.toString());
		} finally {
			writer.close();
		}
	}

	public static String setToStringMaterial(Set<String> list) {
		String str = list.toString().substring(1, list.toString().length() - 1);	
		return str;
	}

	public static String ArrayListtoStringMaterial(ArrayList<String> list) {
		String str = list.toString().substring(1, list.toString().length() - 1);
		return str;
	}

}
