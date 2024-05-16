package com.sanofi.model;

public class Response {
	
	private String responseStatus;
	private String count;
	private String message;
	private String objectName;
	
	public String getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	@Override
	public String toString() {
		return "Response [responseStatus=" + responseStatus + ", count=" + count + ", message=" + message
				+ ", objectName=" + objectName + "]";
	}
}
