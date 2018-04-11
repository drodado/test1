package net.drodado.vas.test1.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class KPI {

	/**
	 * Total number of processed JSON files.
	 */
	private int totalNumberOfProcessedJSONFiles = 0;
	
	/**
	 * Total number of rows.
	 */
	private int totalNumberOfRows = 0;
	
	/**
	 * Total number of calls.
	 */
	private int totalNumberOfCalls = 0;
	
	/**
	 * Total number of messages.
	 */
	private int totalNumberOfMessages = 0;
	
	/**
	 * Total number of different origin country codes (https://en.wikipedia.org/wiki/MSISDN).
	 */
	private int totalNumberOfDifferentOriginCountryCodes = 0;
	private Set<String> originCountryCodes = new HashSet<>();
	
	/**
	 * Total number of different destination country codes (https://en.wikipedia.org/wiki/MSISDN)
	 */
	private int totalNumberOfDifferentDestinationCountryCodes = 0;
	private Set<String> destinationCountryCodes = new HashSet<>();
	
	/**
	 * Duration of each JSON process.
	 */
	private List<Long> durationOfEachJSONProcess = new ArrayList<>();
	

	
	public KPI() {
		super();
	}
	
	
	public int getTotalNumberOfProcessedJSONFiles() {
		return totalNumberOfProcessedJSONFiles;
	}
	
	
	public void increaseTotalNumberOfProcessedJSONFiles() {
		++this.totalNumberOfProcessedJSONFiles;
	}

	
	public int getTotalNumberOfRows() {
		return totalNumberOfRows;
	}
	
	
	public void increaseTotalNumberOfRows() {
		++this.totalNumberOfRows;
	}
	

	public int getTotalNumberOfCalls() {
		return totalNumberOfCalls;
	}
	
	
	public void increaseTotalNumberOfCalls() {
		++this.totalNumberOfCalls;
	}
	

	public int getTotalNumberOfMessages() {
		return totalNumberOfMessages;
	}
	
	
	public void increaseTotalNumberOfMessages() {
		++this.totalNumberOfMessages;
	}
	

	public int getTotalNumberOfDifferentOriginCountryCodes() {
		return totalNumberOfDifferentOriginCountryCodes;
	}
	
	
	public void updateTotalNumberOfDifferentOriginCountryCodes(Set<String> countryCodes) {
		this.originCountryCodes.addAll(countryCodes);
		this.totalNumberOfDifferentOriginCountryCodes = this.originCountryCodes.size();
	}
	

	public int getTotalNumberOfDifferentDestinationCountryCodes() {
		return totalNumberOfDifferentDestinationCountryCodes;
	}
	
	
	public void updateTotalNumberOfDifferentDestinationCountryCodes(Set<String> countryCodes) {
		this.destinationCountryCodes.addAll(countryCodes);
		this.totalNumberOfDifferentDestinationCountryCodes = this.destinationCountryCodes.size();
	}

	
	public List<Long> getDurationOfEachJSONProcess() {
		return durationOfEachJSONProcess;
	}
	
	public void addDurationOfEachJSONProcess(long duration) {
		this.durationOfEachJSONProcess.add(duration);
	}


	@Override
	public String toString() {
		return "KPI [totalNumberOfProcessedJSONFiles=" + totalNumberOfProcessedJSONFiles + ", totalNumberOfRows="
				+ totalNumberOfRows + ", totalNumberOfCalls=" + totalNumberOfCalls + ", totalNumberOfMessages="
				+ totalNumberOfMessages + ", totalNumberOfDifferentOriginCountryCodes="
				+ totalNumberOfDifferentOriginCountryCodes + ", totalNumberOfDifferentDestinationCountryCodes="
				+ totalNumberOfDifferentDestinationCountryCodes + ", durationOfEachJSONProcess="
				+ durationOfEachJSONProcess + "]";
	}
	

}
