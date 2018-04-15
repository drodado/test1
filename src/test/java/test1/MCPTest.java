package test1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import net.drodado.vas.test1.beans.KPI;
import net.drodado.vas.test1.beans.MCPJsonFile;
import net.drodado.vas.test1.beans.MSISDN;
import net.drodado.vas.test1.beans.Metrics;
import net.drodado.vas.test1.controller.MCPController;
import net.drodado.vas.test1.controller.MCPControllerImpl;
import net.drodado.vas.test1.exceptions.MCPServiceException;
import net.drodado.vas.test1.service.MCPService;
import net.drodado.vas.test1.service.MCPServiceImpl;

/**
 * Test class to verify the integration between the controller class and the service class
 * of the Mobile Communication Platform Service.
 * 
 * @author drodado
 *
 */
public class MCPTest {

	private MCPController mcpController;

	private MCPService mcpService;

	@Before
	public void init() {
		mcpService = mock(MCPServiceImpl.class);
		mcpController = new MCPControllerImpl(mcpService);
	}

//	@Test
//	public void mcpFileTreatment() {
//		final String date = "20180131";
//		try {
//			MCPJsonFile mcpJsonFile = mcpService.mcpFileTreatment(date);
//			assertEquals("MCP_20180131.json", mcpJsonFile.getFilename());
//		} catch(MCPServiceException exception) {
//			exception.printStackTrace();
//		}
//	}
	
	/**
	 * Test for serialization from a Metrics object to JSON.
	 */
	@Test
	public void metricsToJSON() {
		final Metrics metrics = new Metrics();
		final Gson gson = new Gson();
		final String representacionJSON = gson.toJson(metrics);
		assertEquals("{\"numberOfRowsWithMissingFields\":0,\"numberOfMessagesWithBlankContent\":0,\"numberOfRowsWithFieldErrors\":0,"
				+ "\"numberOfCallsOriginGroupedByCountryCode\":[],\"numberOfCallsDestinationGroupedByCountryCode\":[],"
				+ "\"relationshipBetweenCalls\":{\"ok\":0,\"ko\":0},\"averageCallDurationGroupedByCountryCode\":{},"
				+ "\"totalCallDurationGroupedByCountryCode\":{},\"rankingWords\":{\"FINE\":0,\"YOU\":0,\"ARE\":0,\"NOT\":0,\"HELLO\":0}}"
				, representacionJSON);
	}
	
	/**
	 * Test for serialization from a KPI object to JSON.
	 */
	@Test
	public void kpisToJSON() {
		final KPI kpis = new KPI();
		final Gson gson = new Gson();
		final String representacionJSON = gson.toJson(kpis);
		assertEquals("{\"totalNumberOfProcessedJSONFiles\":0,\"totalNumberOfRows\":0,\"totalNumberOfCalls\":0,\"totalNumberOfMessages\":0,"
				+ "\"totalNumberOfDifferentOriginCountryCodes\":0,\"originCountryCodes\":[],\"totalNumberOfDifferentDestinationCountryCodes\":0,"
				+ "\"destinationCountryCodes\":[],\"durationOfEachJSONProcess\":[]}"
				, representacionJSON);
	}
	
	/**
	 * Test for deserialization from a CALL JSON message to a properties.
	 */
	@Test
	public void callJSONToProperties() {
		final String json = "{"+
			    "\"message_type\": \"CALL\","+
			    "\"timestamp\": 1517645700,"+
			    "\"origin\": 34969000001,"+
			    "\"destination\": 34969000101,"+
			    "\"duration\": 120,"+
			    "\"status_code\": \"OK\","+
			    "\"status_description\": \"OK\""+
			  "}";
		final Gson gson = new Gson();
	    final Properties properties = gson.fromJson(json, Properties.class);
	    assertEquals("CALL", properties.getProperty("message_type"));
		assertEquals("1517645700", properties.getProperty("timestamp"));
		assertEquals("34969000001", properties.getProperty("origin"));
		assertEquals("34969000101", properties.getProperty("destination"));
		assertEquals("120", properties.getProperty("duration"));
		assertEquals("OK", properties.getProperty("status_code"));
		assertEquals("OK", properties.getProperty("status_description"));
	}
	
	/**
	 * Test for deserialization from a CALL JSON message to a properties.
	 */
	@Test
	public void msgJSONToProperties() {
		final String json = "{"+
			    "\"message_type\": \"MSG\","+
			    "\"timestamp\": 1517559332,"+
			    "\"origin\": 34960000003,"+
			    "\"destination\": 34960000103,"+
			    "\"message_content\": \"B\","+
			    "\"message_status\": \"SEEN\""+
			  "}";
		final Gson gson = new Gson();
	    final Properties properties = gson.fromJson(json, Properties.class);
	    assertEquals("MSG", properties.getProperty("message_type"));
		assertEquals("1517559332", properties.getProperty("timestamp"));
		assertEquals("34960000003", properties.getProperty("origin"));
		assertEquals("34960000103", properties.getProperty("destination"));
		assertEquals("B", properties.getProperty("message_content"));
		assertEquals("SEEN", properties.getProperty("message_status"));
	}
	
	/**
	 * MSISDN: 919961345678
	 * 
	 * 	CC	India	91
	 * 	NDC	Kerala	9961
	 * 	SN	Subscriber's number	345678
	 */
	@Test
	public void parseMSISDNToObject() {
		final String code = "919961345678";
		final MSISDN msisdn = new MSISDN(code);
		assertEquals("91", msisdn.getCc());
		assertEquals("9961", msisdn.getNdc());
		assertEquals("345678", msisdn.getSn());
	}
	
}
