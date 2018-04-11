package net.drodado.vas.test1.beans;

import org.springframework.stereotype.Component;

@Component
public class AverageCallsDurationCountryCode {

	private String countryCode = "";
	
	private Long average = Long.parseLong("0");
	
	
	public AverageCallsDurationCountryCode() {
		super();
	}


	public AverageCallsDurationCountryCode(String countryCode, Long average) {
		super();
		this.countryCode = countryCode;
		this.average = average;
	}


	public String getCountryCode() {
		return countryCode;
	}


	public Long getAverage() {
		return average;
	}


	@Override
	public String toString() {
		return "AverageCallsDurationCountryCode [countryCode=" + countryCode + ", average=" + average + "]";
	}


}
