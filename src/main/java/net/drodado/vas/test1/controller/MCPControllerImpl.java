package net.drodado.vas.test1.controller;

import static net.drodado.vas.test1.util.MCPUtils.prettyGson;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import net.drodado.vas.test1.Environment;
import net.drodado.vas.test1.beans.MCPJsonFile;
import net.drodado.vas.test1.beans.KPI;
import net.drodado.vas.test1.beans.Metrics;
import net.drodado.vas.test1.exceptions.MCPServiceException;
import net.drodado.vas.test1.service.MCPService;

/**
 * Mobile Communication Platform Service Controller.
 * 
 * @author drodado
 *
 */
@Controller
public class MCPControllerImpl implements MCPController {

	private static Log logger = LogFactory.getLog("MCPControllerImpl");
	
	
	@Autowired
	private MCPService mcpService;
	
	
	public MCPControllerImpl(MCPService mcpService) {
		super();
		this.mcpService = mcpService;
	}
	
    /**
     * HTTP endpoint (/test1/mcpfile/{date}) to process a MCP JSON file.
     * 
     * @return 
     */
    @RequestMapping(value = "/test1/mcpfile/{date}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> mcpFileTreatment(@PathVariable("date") String date) {
    	
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("Entering HTTP endpoint (/test1/mcpfile/{date})...");
    	}
  
    	try {		
    		final MCPJsonFile mCPJsonFile = mcpService.mcpFileTreatment(date);
    		
    		if ( logger.isDebugEnabled() ) {
    			logger.debug("Exiting HTTP endpoint (/test1/mcpfile/{date}).");
    		}
    		
    		return new ResponseEntity<String>(mCPJsonFile.toHttpResponse(), HttpStatus.OK);
    		
    	} catch(MCPServiceException exception) {
			if ( exception.getCause() instanceof HttpClientErrorException ) {
				HttpClientErrorException clientErrorException = (HttpClientErrorException) exception.getCause();
				return new ResponseEntity<String>(
						String.format("ERROR: %s", clientErrorException.getMessage()), clientErrorException.getStatusCode());
			} else {
				return new ResponseEntity<String>(
						String.format("ERROR: %s", exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
			}
    	}
    }
    
    
    /**
     * HTTP endpoint (/test1/metrics) that returns a set of counters related with the processed JSON file.
     * 
     * @return Metrics in JSON format.
     */
    @RequestMapping(value = "/test1/metrics", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> metrics() {
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("Entering HTTP endpoint (/test1/metrics)...");
    	}
    	
        final Metrics metrics = mcpService.metrics();
		final String response = prettyGson(metrics);
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("Exiting HTTP endpoint (/test1/metrics).");
		}
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
    }
	
    /**
     * HTTP endpoint (/test1/kpis) that returns a set of counters related with the service.
     * 
     * @return Metrics in JSON format.
     */
    @RequestMapping(value = "/test1/kpis", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> kpis() {
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("Entering HTTP endpoint (/test1/kpis)...");
    	}
    	
        final KPI kpis = mcpService.kpis();
		final String response = prettyGson(kpis);
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("Exiting HTTP endpoint (/test1/kpis).");
		}
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
    }


}
