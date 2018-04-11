package net.drodado.vas.test1.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.drodado.vas.test1.beans.Country;
import net.drodado.vas.test1.exceptions.MCPServiceException;

public class MCPUtils {

	private static HashMap<String, Country> countries = null;
	
	
	/**
	 * To parse an object to json.
	 * 
	 * @param object
	 * @return
	 */
    public static String prettyGson(Object object) {
        //return new Gson().toJson(metrics);
    	final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
		return prettyGson.toJson(object);
    }
    
    
	public static String[] formatLines(String contentFile) {
		String[] lines = contentFile.split("\n");				
		return lines;
	}
	
	
	public static void loadMSISDN() throws MCPServiceException {
		new MCPUtils().loadMSISDN("countries.json");
	}
	
	
	private void loadMSISDN(String file) throws MCPServiceException {
		try {
			if ( countries == null ) {
				countries = new HashMap<>();
				ObjectMapper mapper = new ObjectMapper();
				List<Country> aux = mapper.readValue(ClassLoader.getSystemResourceAsStream(file),
						mapper.getTypeFactory().constructCollectionType(ArrayList.class, Country.class));
				for(Country country : aux) {
					countries.put(country.getDialCode(), country);
				}
			}
		} catch(Exception exception) {
			throw new MCPServiceException(exception);
		}
	}
	
	public static Country getCountryDialCode(String phoneNumber) {
		String auxCode = phoneNumber.substring(0, 1);
		if ( countries.containsKey(auxCode) ) {
			return countries.get(auxCode);
		}
		auxCode = phoneNumber.substring(0, 2);
		if ( countries.containsKey(auxCode) ) {
			return countries.get(auxCode);
		}
		auxCode = phoneNumber.substring(0, 3);
		if ( countries.containsKey(auxCode) ) {
			return countries.get(auxCode);
		}
		auxCode = phoneNumber.substring(0, 4);
		if ( countries.containsKey(auxCode) ) {
			return countries.get(auxCode);
		}
		return null;
	}
	
	public static boolean isJSONValid(String jsonInString) {
		boolean valid = true;
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString);
		} catch (IOException e) {
			valid = false;
		}
		return valid;
	}
	

	public static Properties getPropertiesFromJson(String json) {
		return new Gson().fromJson(json, Properties.class);
	}

	
	public static Country getCountry(String countryCode) {
		return countries.get(countryCode);
	}
	
}
