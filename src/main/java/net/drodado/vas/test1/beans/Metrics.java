package net.drodado.vas.test1.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.drodado.vas.test1.service.AbstractMCPService.RankingWords;
import net.drodado.vas.test1.util.MCPUtils;

@Component
public class Metrics {

	/**
	 * Number of rows with missing fields.
	 */
	private int numberOfRowsWithMissingFields = 0;
	
	/**
	 * Number of messages with blank content.
	 */
	private int numberOfMessagesWithBlankContent = 0;
	
	/**
	 * Number of rows with fields errors.
	 */
	private int numberOfRowsWithFieldErrors = 0;
	
	/**
	 * Number of calls origin grouped by country code.
	 */
	private List<CallsNumberCountryCode> numberOfCallsOriginGroupedByCountryCode = new ArrayList<>();
	
	/**
	 * Number of calls destination grouped by country code.
	 */
	private List<CallsNumberCountryCode> numberOfCallsDestinationGroupedByCountryCode = new ArrayList<>();
	
	/**
	 * Relationship between OK/KO calls.
	 */
	@Autowired
	private RelationshipBetweenCalls relationshipBetweenCalls;
	
	/**
	 * Average call duration grouped by country code.
	 */
	private HashMap<String, AverageCallsDurationCountryCode> averageCallDurationGroupedByCountryCode = new HashMap<String, AverageCallsDurationCountryCode>();

	/**
	 * Total call duration grouped by country code.
	 */
	private HashMap<String, Long> totalCallDurationGroupedByCountryCode = new HashMap<String, Long>();
	
	private Map<RankingWords, Integer> rankingWords = new HashMap<>();

	private String filename;
	
	
	public Metrics() {
		super();
		this.relationshipBetweenCalls = new RelationshipBetweenCalls();
		for (RankingWords word: RankingWords.values()) {
			rankingWords.put(word, 0);
		}
	}
	
	public Metrics(String filename) {
		this();
		this.filename = filename;
	}
	

	public int getNumberOfRowsWithMissingFields() {
		return numberOfRowsWithMissingFields;
	}
	
	
	public void increaseNumberOfRowsWithMissingFields() {
		++this.numberOfRowsWithMissingFields;
	}
	

	public int getNumberOfMessagesWithBlankContent() {
		return numberOfMessagesWithBlankContent;
	}
	

	public void increaseNumberOfMessagesWithBlankContent() {
		++this.numberOfMessagesWithBlankContent;
	}
	

	public int getNumberOfRowsWithFieldErrors() {
		return numberOfRowsWithFieldErrors;
	}
	
	
	public void increaseNumberOfRowsWithFieldErrors() {
		++this.numberOfRowsWithFieldErrors;
	}


	public void increaseNumberOfCallsOriginGroupedByCountryCode(String countryDialCode) {
		CallsNumberCountryCode match = null;
		for ( CallsNumberCountryCode callsNumberCountryCode : this.numberOfCallsOriginGroupedByCountryCode ) {
			if ( callsNumberCountryCode.getCountryCode().equalsIgnoreCase(countryDialCode) ) {
				match = callsNumberCountryCode;
				break;
			}
		}
		if ( match==null ) {
			final Country country = MCPUtils.getCountryDialCode(countryDialCode);
			match = new CallsNumberCountryCode(country.getDialCode(), country.getName());
			this.numberOfCallsOriginGroupedByCountryCode.add(match);
		}
		match.increaseNumber();
	}
	
	public void increaseNumberOfCallsDestinationGroupedByCountryCode(String countryDialCode) {
		CallsNumberCountryCode match = null;
		for ( CallsNumberCountryCode callsNumberCountryCode : this.numberOfCallsDestinationGroupedByCountryCode ) {
			if ( callsNumberCountryCode.getCountryCode().equalsIgnoreCase(countryDialCode) ) {
				match = callsNumberCountryCode;
				break;
			}
		}
		if ( match==null ) {
			final Country country = MCPUtils.getCountryDialCode(countryDialCode);
			match = new CallsNumberCountryCode(country.getDialCode(), country.getName());
			this.numberOfCallsDestinationGroupedByCountryCode.add(match);
		}
		match.increaseNumber();
	}
	
	
	public long getNumberOfCallsDestinationGroupedByCountryCode(String countryDialCode) {
		for ( CallsNumberCountryCode callsNumberCountryCode : this.numberOfCallsDestinationGroupedByCountryCode ) {
			if ( callsNumberCountryCode.getCountryCode().equalsIgnoreCase(countryDialCode) ) {
				return callsNumberCountryCode.getNumber();
			}
		}
		return 0;
	}
	
	
	public long getNumberOfCallsOriginGroupedByCountryCode(String countryDialCode) {
		return getNumberOfCallsGroupedByCountryCode(countryDialCode, this.numberOfCallsOriginGroupedByCountryCode);
	}
	
	
	public long getNumberOfCallsGroupedByCountryCode(String countryDialCode, List<CallsNumberCountryCode> callsByCountryCode) {
		for ( CallsNumberCountryCode callsNumberCountryCode : callsByCountryCode ) {
			if ( callsNumberCountryCode.getCountryCode().equalsIgnoreCase(countryDialCode) ) {
				return callsNumberCountryCode.getNumber();
			}
		}
		return 0;
	}
	
	
	public int getNumberOfCallsGroupedByCountryCode() {
		return this.numberOfCallsOriginGroupedByCountryCode.size();
	}


	public RelationshipBetweenCalls getRelationshipBetweenCalls() {
		return relationshipBetweenCalls;
	}


	public HashMap<String, AverageCallsDurationCountryCode> getAverageCallDurationGroupedByCountryCode() {
		return averageCallDurationGroupedByCountryCode;
	}


	public String getFilename() {
		return filename;
	}

	
	public void increaseTotalCallsDurationGroupedByCountryCode(String countryDialCode, String duration) {
		if ( !totalCallDurationGroupedByCountryCode.containsKey(countryDialCode) ) {
			totalCallDurationGroupedByCountryCode.put(countryDialCode, Long.parseLong("0"));
		}
		totalCallDurationGroupedByCountryCode.put(countryDialCode, totalCallDurationGroupedByCountryCode.get(countryDialCode)+(Long.parseLong(duration)));
		updateAverageCallDurationGroupedByCountryCode(countryDialCode);
	}
	
	
	private void updateAverageCallDurationGroupedByCountryCode(String countryDialCode) {
		final Long average = Long.divideUnsigned(
				totalCallDurationGroupedByCountryCode.get(countryDialCode),
				getNumberOfCallsOriginGroupedByCountryCode(countryDialCode));
		this.averageCallDurationGroupedByCountryCode.put(countryDialCode, new AverageCallsDurationCountryCode(countryDialCode, average));
	}

	
	public Set<String> getTotalNumberOfDifferentOriginCountryCodes() {
		return getCountryCodes(numberOfCallsOriginGroupedByCountryCode);
	}
	
	
	public Set<String> getTotalNumberOfDifferentDestinationCountryCodes() {
		return getCountryCodes(numberOfCallsDestinationGroupedByCountryCode);
	}
	
	
	public Set<String> getCountryCodes(List<CallsNumberCountryCode> callsNumberCountryCodes) {
		Set<String> result = new HashSet<>();
		for(CallsNumberCountryCode aux : callsNumberCountryCodes) {
			result.add(aux.getCountryCode());
		}
		return result;
	}
	
	
	public void increaseRankingWord(RankingWords rankingWord) {
		rankingWords.put(rankingWord, rankingWords.get(rankingWord).intValue()+1);
	}
	
}
