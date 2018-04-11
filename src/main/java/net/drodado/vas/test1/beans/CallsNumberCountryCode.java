package net.drodado.vas.test1.beans;

import org.springframework.stereotype.Component;

@Component
public class CallsNumberCountryCode {

	private String countryCode = "";
	
	private String countryDescr = "";
	
	private long number = 0;
	
	
	public CallsNumberCountryCode() {
		super();
	}
	

	public CallsNumberCountryCode(String countryCode, String countryDescr) {
		super();
		this.countryCode = countryCode;
		this.countryDescr = countryDescr;
	}


	public long getNumber() {
		return number;
	}
	
	
	public void increaseNumber() {
		++this.number;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public String getCountryDescr() {
		return countryDescr;
	}

	
	@Override
	public String toString() {
		return "CallsNumberCountryCode [countryCode=" + countryCode + ", countryDescr=" + countryDescr + ", number="
				+ number + "]";
	}

}
