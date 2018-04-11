package net.drodado.vas.test1.service;

import java.util.Arrays;
import java.util.List;

/**
 *   
 *   {
 *       "message_type": "CALL",
 *       "timestamp": 1517645700,
 *       "origin": 34969000001,
 *       "destination": 34969000101,
 *       "duration": 120,
 *       "status_code": "OK",
 *       "status_description": "OK"
 *   }
 *   
 * @author david
 *
 */
public enum CallFields {

	MESSAGE_TYPE("message_type"),
	
	TIMESTAMP("timestamp"),
	
	ORIGIN("origin"),
	
	DESTINATION("destination"),
	
	DURATION("duration"),
	
	STATUS_CODE("status_code"),
	
	STATUS_DESCRIPTION("status_description");
	
	private String fieldName;
	
	
	CallFields(String fieldName) {
		this.fieldName = fieldName;
	}

    public String getFieldName() {
    	return this.fieldName;
    }
    
    public static List<CallFields> ownValues() {
    	return Arrays.asList(DURATION, STATUS_CODE, STATUS_DESCRIPTION);
    }
    	
}
