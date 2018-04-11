package net.drodado.vas.test1.service.validator;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractJsonLineValidator implements JsonLineValidator {

	/**
	 * Auxiliary class to maintain information about the validation of the message.
	 */
	protected class ValidationInfo {
		
		boolean hasMissingFields = false;
		boolean hasBlankContentFields = false;
		boolean hasFieldErrors = false;
		
		boolean hasErrors() {
			return or(hasMissingFields, hasBlankContentFields, hasFieldErrors);
		}

		@Override
		public String toString() {
			return "ValidationInfo [hasMissingFields=" + hasMissingFields + ", hasBlankContentFields="
					+ hasBlankContentFields + ", hasFieldErrors=" + hasFieldErrors + "]";
		}
		
	}
	
	public class ValidationResume {
		
		Properties properties;
		ValidationInfo validationInfo;
		
		private ValidationResume() {};
		
		ValidationResume(final Properties properties, final ValidationInfo validationInfo) {
			this.properties = properties;
			this.validationInfo = validationInfo;
		}
		
		public boolean isValid() { return !validationInfo.hasErrors(); }

		public String getProperty(String property) {
			return properties.getProperty(property);
		}
		
		@Override
		public String toString() {
			return "ValidationResume [properties=" + properties + ", validationInfo=" + validationInfo + "]";
		}
		
	}
	
	static boolean and(boolean... booleans) {
		for (boolean aux : booleans) {
			if (!aux) {
				return false;
			}
		}
		return true;
	}
	
	
	static boolean or(boolean... booleans) {
		for (boolean aux : booleans) {
			if (aux) {
				return true;
			}
		}
		return false;
	}
	
	boolean isValidPhoneNumber(String number) {
		final String regex = "^\\+?[0-9. ()-]{10,25}$";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}
	
}
