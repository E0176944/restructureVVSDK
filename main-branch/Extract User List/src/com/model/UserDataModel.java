package com.model;

import java.util.List;

public class UserDataModel {

	public String user_name__v;

	public String user_email__v;

	public String security_policy_id__v;
	
	public String security_policy_name;

	public String id;

	public String federated_id__v;

	public String last_login__v;
	
	public String created_by;
	
	public String created_by_email;
	
	public String created_by_name;

	public List<VaultMembershipModel> vault_membership;

	public String getUser_name__v() {
		return user_name__v;
	}

	public void setUser_name__v(String user_name__v) {
		this.user_name__v = user_name__v;
	}

	public String getUser_email__v() {
		return user_email__v;
	}

	public void setUser_email__v(String user_email__v) {
		this.user_email__v = user_email__v;
	}

	public String getSecurity_policy_id__v() {
		return security_policy_id__v;
	}

	public void setSecurity_policy_id__v(String security_policy_id__v) {
		this.security_policy_id__v = security_policy_id__v;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFederated_id__v() {
		return federated_id__v;
	}

	public void setFederated_id__v(String federated_id__v) {
		this.federated_id__v = federated_id__v;
	}

	public String getLast_login__v() {
		return last_login__v;
	}

	public void setLast_login__v(String last_login__v) {
		this.last_login__v = last_login__v;
	}

	public List<VaultMembershipModel> getVault_membership() {
		return vault_membership;
	}

	public void setVault_membership(List<VaultMembershipModel> vault_membership) {
		this.vault_membership = vault_membership;
	}

	public String getSecurity_policy_name() {
		return security_policy_name;
	}

	public void setSecurity_policy_name(String security_policy_name) {
		this.security_policy_name = security_policy_name;
	}

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getCreated_by_name() {
		return created_by_name;
	}

	public void setCreated_by_name(String created_by_name) {
		this.created_by_name = created_by_name;
	}

	public String getCreated_by_email() {
		return created_by_email;
	}

	public void setCreated_by_email(String created_by_email) {
		this.created_by_email = created_by_email;
	}
	
	
	
}
