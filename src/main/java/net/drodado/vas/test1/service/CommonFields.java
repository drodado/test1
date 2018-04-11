package net.drodado.vas.test1.service;

public enum CommonFields {

	MESSAGE_TYPE("message_type"),
	
	TIMESTAMP("timestamp"),
	
	ORIGIN("origin"),
	
	DESTINATION("destination");
	
	private String fieldName;
	
	CommonFields(String fieldName) {
		this.fieldName = fieldName;
	}
	
    public String getFieldName() {
    	return this.fieldName;
    }
	
}
