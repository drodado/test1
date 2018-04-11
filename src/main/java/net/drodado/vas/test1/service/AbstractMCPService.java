package net.drodado.vas.test1.service;

public abstract class AbstractMCPService implements MCPService {
	
	/**
	 * The type of the message. Two values are valid: {CALL|MSG}.
	 */
	public enum MessageType {
		CALL, MSG;	
	}

	/**
	 * Status of the message. Two values are valid: {DELIVERED|SEEN}.
	 */
	public enum MessageStatus {
		DELIVERED, SEEN;	
	}
	
	/**
	 * Status code of the call. Only for CALL (message_type). Two values are valid: {OK|KO}.
	 */
	public enum StatusCode {	
		OK, KO;		
	}
	
	/**
	 * Words for occurrence ranking in message_content field.
	 */
	public enum RankingWords {
		ARE, YOU, FINE, HELLO, NOT;	
	}
	

	
}
