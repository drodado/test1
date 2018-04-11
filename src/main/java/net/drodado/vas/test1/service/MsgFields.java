package net.drodado.vas.test1.service;

import java.util.Arrays;
import java.util.List;

/**
 *   {
 *       "message_type": "MSG",
 *       "timestamp": 1517559332,
 *       "origin": 34960000003,
 *       "destination": 34960000103,
 *       "message_content": "B",
 *       "message_status": "SEEN"
 *   }
 *     
 * @author david
 *
 */
public enum MsgFields {

	MESSAGE_TYPE("message_type"),
	
	TIMESTAMP("timestamp"),
	
	ORIGIN("origin"),
	
	DESTINATION("destination"),
	
	MESSAGE_CONTENT("message_content"),
	
	MESSAGE_STATUS("message_status");
	
	private String fieldName;
	
	
	MsgFields(String fieldName) {
		this.fieldName = fieldName;
	}

    public String getFieldName() {
    	return this.fieldName;
    }
    
    public static List<MsgFields> ownValues() {
    	return Arrays.asList(MESSAGE_CONTENT, MESSAGE_STATUS);
    }
    	
}
