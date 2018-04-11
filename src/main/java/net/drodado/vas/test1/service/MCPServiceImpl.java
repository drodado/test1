package net.drodado.vas.test1.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import net.drodado.vas.test1.Environment;
import net.drodado.vas.test1.beans.KPI;
import net.drodado.vas.test1.beans.MCPJsonFile;
import net.drodado.vas.test1.beans.Metrics;
import net.drodado.vas.test1.exceptions.MCPServiceException;
import net.drodado.vas.test1.service.validator.AbstractJsonLineValidator.ValidationResume;
import net.drodado.vas.test1.service.validator.JsonLineValidator;
import net.drodado.vas.test1.util.MCPUtils;

/**
 * Mobile Communication Platform Service class.
 * 
 * @author drodado
 *
 */
@Service
public class MCPServiceImpl extends AbstractMCPService {

	private static Log logger = LogFactory.getLog("MCPServiceImpl");

	@Autowired
	private Metrics metrics;

	@Autowired
	private KPI kpis;

	@Autowired
	private JsonLineValidator jsonLineValidator;

	
	
	private void init(String filename) throws MCPServiceException {
		this.metrics = new Metrics(filename);
		MCPUtils.loadMSISDN();
	}

	
	/**
	 * Service to treat a MCP JSON file by date.
	 * 
	 * @param date Date forfile to be processed.
	 * @return Processed content file.
	 * @throws MCPServiceException 
	 */
	public MCPJsonFile mcpFileTreatment(String date) throws MCPServiceException {

		final long start = System.currentTimeMillis();
		
		String filename = date;
		try {
			filename = buildFilename(date);
			init(filename);
		} catch(Exception exception) {
			logger.error(exception.getMessage());
			throw new MCPServiceException(exception);
		}

		final MCPJsonFile mcpJsonFile = getJsonFromFile(metrics.getFilename());
		final List<String> jsonMessages = mcpJsonFile.getValidJSONLines();
		for (String jsonLine : jsonMessages) {
			final ValidationResume validationResume = jsonLineValidator.validateFields(jsonLine, metrics);
			if (validationResume.isValid()) {
				updateStatistics(validationResume);
			}
		}

		final long duration = System.currentTimeMillis() - start;

		logger.info(String.format("Time in process [%s]: %s ms.", metrics.getFilename(), duration));

		updateKpis(duration);

		return mcpJsonFile;
	}
	
	
	private void updateKpis(long duration) {
		kpis.addDurationOfEachJSONProcess(duration);
		kpis.updateTotalNumberOfDifferentOriginCountryCodes(metrics.getTotalNumberOfDifferentOriginCountryCodes());
		kpis.updateTotalNumberOfDifferentDestinationCountryCodes(metrics.getTotalNumberOfDifferentDestinationCountryCodes());
	}
	
	
	private void updateStatistics(final ValidationResume validationResume) {

		final MessageType messageType = MessageType.valueOf(validationResume.getProperty(CommonFields.MESSAGE_TYPE.getFieldName()).toUpperCase());

		switch (messageType) {
			case CALL:
				updateCallStatistics(validationResume);
				break;
			case MSG:
				updateMsgStatistics(validationResume);
				break;
			default:
				break;
		}
	}

	
	private void updateCallStatistics(final ValidationResume validationResume) {
		
		kpis.increaseTotalNumberOfCalls();
		
		// Number of calls grouped by country code.
		final String originCountryDialCode = MCPUtils.getCountryDialCode(validationResume.getProperty(CommonFields.ORIGIN.getFieldName())).getDialCode();
		metrics.increaseNumberOfCallsOriginGroupedByCountryCode(originCountryDialCode);
		
		final String destinationCountryDialCode = MCPUtils.getCountryDialCode(validationResume.getProperty(CommonFields.DESTINATION.getFieldName())).getDialCode();
		metrics.increaseNumberOfCallsDestinationGroupedByCountryCode(destinationCountryDialCode);
		
		// Relationship between OK/KO calls.
		final StatusCode statusCode = StatusCode.valueOf(validationResume.getProperty(CallFields.STATUS_CODE.getFieldName()).toUpperCase());
		switch (statusCode) {
			case OK:
				metrics.getRelationshipBetweenCalls().increaseOk();
				break;
			case KO:
				metrics.getRelationshipBetweenCalls().increaseKo();
				break;
			default:
				break;
		}
		
		metrics.increaseTotalCallsDurationGroupedByCountryCode(originCountryDialCode, validationResume.getProperty(CallFields.DURATION.getFieldName()));
	}

	
	private void updateMsgStatistics(final ValidationResume validationResume) {
		kpis.increaseTotalNumberOfMessages();
		if ("".equalsIgnoreCase(validationResume.getProperty(MsgFields.MESSAGE_CONTENT.getFieldName()).trim())) {
			metrics.increaseNumberOfMessagesWithBlankContent();
		} else {
			for (RankingWords rankingWord : RankingWords.values()) {
				if ( validationResume.getProperty(MsgFields.MESSAGE_CONTENT.getFieldName()).toUpperCase().contains(rankingWord.name())) {
					metrics.increaseRankingWord(rankingWord);
				}
			}
		}
	}

	
	/**
	 * Service to return a set of counters, metrics, related with a processed JSON
	 * file.
	 * 
	 * @return Metrics bean.
	 */
	public Metrics metrics() {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering service (metrics)...");
		}
		return this.metrics;
	}

	
	/**
	 * Service to return a set of counters, kpis, related with the service.
	 * 
	 * @return KPI bean.
	 */
	public KPI kpis() {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering service (kpis)...");
		}
		return this.kpis;
	}

	
	private String getMCPFileFromUrl(String filename) throws MCPServiceException {
		try {
			logger.info(String.format("Recovering file: %s", filename));
			final String urlPattern = Environment.getUrlForMCPFile();
			final String url = urlPattern.concat(filename);
			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
			return new RestTemplate().getForObject(builder.toUriString(), String.class);
		} catch (Exception ioException) {
			logger.error(ioException);
			if (ioException instanceof HttpClientErrorException) {
				if (HttpStatus.NOT_FOUND.equals(((HttpClientErrorException) ioException).getStatusCode())) {
					logger.error(String.format("FILE NOT FOUND: %s", filename));
				}
			}
			throw new MCPServiceException(ioException);
		}
	}

	
	/**
	 * Recovery and reading of a message file according to a date received as a
	 * parameter.
	 * 
	 * @param filename
	 *            File will be requested.
	 * @return
	 * @throws MCPServiceException
	 */
	private MCPJsonFile getJsonFromFile(String filename) throws MCPServiceException {

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Recovering file [%s]...", filename));
		}

		final String contentMCPFile = getMCPFileFromUrl(filename);

		logger.info(String.format("File recovered [%s]...", filename));
		logger.info(String.format("File content:\n[%s]...", contentMCPFile));

		kpis.increaseTotalNumberOfProcessedJSONFiles();

		final String[] lines = MCPUtils.formatLines(contentMCPFile);

		final List<String> validJSONLines = new ArrayList<>();
		final List<String> wrongJSONLines = new ArrayList<>();

		for (String line : lines) {

			kpis.increaseTotalNumberOfRows();

			if (logger.isDebugEnabled()) {
				logger.debug(line);
			}

			boolean validLine = MCPUtils.isJSONValid(line);

			if (validLine) {
				validJSONLines.add(line);
			} else {
				wrongJSONLines.add(line);
			}
		}

		if (wrongJSONLines.size() > 0) {
			logger.error(String.format("[%d] lines were not valid JSON format and will not processed.",
					wrongJSONLines.size()));
			if (logger.isDebugEnabled()) {
				for (String wrongLine : wrongJSONLines) {
					logger.error(String.format("line: %s", wrongLine));
				}
			}
		}

		logger.info(String.format("[%d] lines received.", lines.length));
		logger.info(String.format("[%d] lines were json valid.", validJSONLines.size()));
		logger.info(String.format("[%d] lines were wrong and will not processed.", wrongJSONLines.size()));

		return new MCPJsonFile(lines, filename, validJSONLines, wrongJSONLines);
	}
	
	private String buildFilename(String date) throws MCPServiceException {
		try {
			final String replacePttrn = "YYYYMMDD";
			return Environment.getMCPFilenamePattern().replace(replacePttrn, date).concat(Environment.getMCPFileExternsion());
		} catch(IOException exception) {
			logger.error(exception.getLocalizedMessage());
			throw new MCPServiceException(exception);
		}
	}
	
}
