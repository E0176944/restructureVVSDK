package com.model;

public class VaultMembershipModel {

	public String id;
	public boolean active__v;
	public String name;
	
	public boolean isActive__v() {
		return active__v;
	}
	public void setActive__v(boolean active__v) {
		this.active__v = active__v;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
