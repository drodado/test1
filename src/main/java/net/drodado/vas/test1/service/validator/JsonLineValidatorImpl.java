package net.drodado.vas.test1.service.validator;

import java.sql.Timestamp;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import net.drodado.vas.test1.beans.Metrics;
import net.drodado.vas.test1.service.AbstractMCPService.MessageStatus;
import net.drodado.vas.test1.service.AbstractMCPService.MessageType;
import net.drodado.vas.test1.service.AbstractMCPService.StatusCode;
import net.drodado.vas.test1.service.CallFields;
import net.drodado.vas.test1.service.CommonFields;
import net.drodado.vas.test1.service.MsgFields;
import net.drodado.vas.test1.util.MCPUtils;

@Component
public class JsonLineValidatorImpl extends AbstractJsonLineValidator {
	
	private static Log logger = LogFactory.getLog("JsonLineValidatorImpl");


	public JsonLineValidatorImpl() {
		super();
	}
	
	/**
	 * Fields validation for a line message in json format.
	 * 
	 * @param jsonLine Line message in json format.
	 * @param metrics Metrics instance to be uptated.
	 */
	public ValidationResume validateFields(final String jsonLine, final Metrics metrics) {
		
	    final Properties properties = MCPUtils.getPropertiesFromJson(jsonLine);

	    final ValidationInfo validationInfo = new ValidationInfo();
	    
		MessageType messageType = null;
		
		for(CommonFields commonField : CommonFields.values()) {
			
			final String fieldName = commonField.getFieldName();
			
			if (!properties.containsKey(fieldName)) {
				
				logger.error(String.format("Field name [%s] not found.", fieldName));
				validationInfo.hasMissingFields = true;
				
			} else if ("".equalsIgnoreCase(properties.getProperty(fieldName).trim())) {
				
				logger.error(String.format("Field name [%s] not defined.", fieldName));
				validationInfo.hasBlankContentFields = true;
				
			} else {
				
				switch (commonField) {
				
					case MESSAGE_TYPE:
						
						try {
					    	messageType = MessageType.valueOf(properties.getProperty(CommonFields.MESSAGE_TYPE.getFieldName()).toUpperCase());  	
					    } catch(Exception exception) {
					    	logger.error(
					    			String.format("Invalid value for field name [%s]: [%s].", 
					    					CommonFields.MESSAGE_TYPE.getFieldName(), 
					    					properties.getProperty(CallFields.MESSAGE_TYPE.getFieldName())));
					    	validationInfo.hasFieldErrors = true;
					    }
						break;
						
					case TIMESTAMP:
						
						try {
							final long time = Long.parseLong(properties.getProperty(CommonFields.TIMESTAMP.getFieldName()));
					    	new Timestamp(time);  	
					    } catch(Exception exception) {
					    	logger.error(
					    			String.format("Invalid value for field name [%s]: [%s].", 
					    					CommonFields.TIMESTAMP.getFieldName(), 
					    					properties.getProperty(CallFields.TIMESTAMP.getFieldName())));
					    	validationInfo.hasFieldErrors = true;
					    }
						break;
						
					case ORIGIN:
						validateMSISDN(commonField.getFieldName(), properties, validationInfo);
						break;
						
					case DESTINATION:
						
						validateMSISDN(commonField.getFieldName(), properties, validationInfo);
						break;
						
					default:
						break;
				}
			}
		}
		
		if ( messageType != null ) {
			switch (messageType) {
				case CALL: validateCallFields(properties, validationInfo);				
					break;
				case MSG: validateMsgFields(properties, validationInfo);
					break;
				default:
					break;
			}
		}
		
		if ( validationInfo.hasMissingFields ) { metrics.increaseNumberOfRowsWithMissingFields(); }
		if ( validationInfo.hasBlankContentFields ) { metrics.increaseNumberOfMessagesWithBlankContent(); }
		if ( validationInfo.hasFieldErrors ) { metrics.increaseNumberOfRowsWithFieldErrors(); }
	    
		ValidationResume resume = new ValidationResume(properties, validationInfo);
		if ( !resume.isValid() ) {
			logger.error(String.format("ERROR IN LINE. NOT VALIDATED: %s", jsonLine));
		}
		
		return resume;
	}
	
	
	/**
	 * Validation of the own fields of a message of type CALL.
	 */
	private void validateCallFields(final Properties properties, final ValidationInfo validationInfo) {
		for(CallFields callfield : CallFields.ownValues()) {
			if ( validateBlankOrMissingField(callfield.getFieldName(), properties, validationInfo) ) {
				
				switch (callfield) {
				
					case DURATION:
						
						try { Integer.valueOf(properties.getProperty(callfield.getFieldName())); }
						catch(NumberFormatException numberFormatException) {
					    	logger.error(
					    			String.format("Invalid value for field name [%s]: [%s].", 
					    					callfield.getFieldName(), 
					    					properties.getProperty(callfield.getFieldName())));
					    	validationInfo.hasFieldErrors = true;
						}
						break;
						
					case STATUS_CODE:
						
						try {
							StatusCode.valueOf(properties.getProperty(callfield.getFieldName()).toUpperCase());
						}
						catch(Exception numberFormatException) {
					    	logger.error(
					    			String.format("Invalid value for field name [%s]: [%s].", 
					    					callfield.getFieldName(), 
					    					properties.getProperty(callfield.getFieldName())));
					    	validationInfo.hasFieldErrors = true;
						}
						break;
						
					case STATUS_DESCRIPTION:
						break;
						
					default:
						break;
				}
			}			
		}
	}
	
	
	/**
	 * Validation of the own fields of a message of type MSG.
	 */
	private void validateMsgFields(final Properties properties, final ValidationInfo validationInfo) {
		for(MsgFields msgField: MsgFields.ownValues()) {
			if ( validateMissingField(msgField.getFieldName(), properties, validationInfo) ) {
				
				switch (msgField) {
					case MESSAGE_CONTENT:					
						break;
						
					case MESSAGE_STATUS:
						
						if ( validateBlankField(msgField.getFieldName(), properties, validationInfo) ) {
							try {
								MessageStatus.valueOf(properties.getProperty(msgField.getFieldName()).toUpperCase());
							}
							catch(Exception numberFormatException) {
						    	logger.error(
						    			String.format("Invalid value for field name [%s]: [%s].", 
						    					msgField.getFieldName(), 
						    					properties.getProperty(msgField.getFieldName())));
						    	validationInfo.hasFieldErrors = true;
							}
						}
						break;
						
					default:
						break;
				}
				
			}
		}
	}
	
	
	/**
	 * Missing or blank field validation.
	 */
	private boolean validateBlankOrMissingField(final String fieldName, final Properties properties, final ValidationInfo validationInfo) {
		if ( validateMissingField(fieldName, properties, validationInfo) ) {
			return validateBlankField(fieldName, properties, validationInfo);
		}
		return false;
	}
	
	/**
	 * Missing field validation.
	 */
	private boolean validateMissingField(final String fieldName, final Properties properties, final ValidationInfo validationInfo) {
		if (!properties.containsKey(fieldName)) {
			logger.error(String.format("Field name [%s] not found.", fieldName));
			validationInfo.hasMissingFields = true;
			return false;
		}
		return true;
	}
	
	
	/**
	 * Blank field validation.
	 */
	private boolean validateBlankField(final String fieldName, final Properties properties, final ValidationInfo validationInfo) {
		if ("".equalsIgnoreCase(properties.getProperty(fieldName).trim())) {
			logger.error(String.format("Field name [%s] not defined.", fieldName));
			validationInfo.hasBlankContentFields = true;
			return false;
		}
		return true;
	}
	
	
	/**
	 * Validate MSISDN.
	 */
	private boolean validateMSISDN(final String fieldName, final Properties properties, final ValidationInfo validationInfo) {
		final String phoneNumber = ((String) properties.getProperty(fieldName)).trim();
		if ( !isValidPhoneNumber(phoneNumber) ) {
			logger.error(String.format("Field name [%s] is not valid number: %s", fieldName, phoneNumber));
			validationInfo.hasFieldErrors = true;
			return false;
		} else if (	MCPUtils.getCountryDialCode(phoneNumber)==null ) {
			logger.error(String.format("Field name [%s] is not valid number. Can not identify the country dial code: %s", fieldName, phoneNumber));
			validationInfo.hasFieldErrors = true;
			return false;
		}
		return true;
	}
	
	
}
