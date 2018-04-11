package net.drodado.vas.test1.controller;

import static net.drodado.vas.test1.util.MCPUtils.prettyGson;

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
     * HTTP endpoint (/mcpfile/{date}) to process a MCP JSON file.
     * 
     * @return 
     */
    @RequestMapping(value = "/mcpfile/{date}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> mcpFileTreatment(@PathVariable("date") String date) {
    	
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("Entering HTTP endpoint (/mcpfile/{date})...");
    	}
    	
    	String filename;
    	try {
    		filename = mcpService.mcpFileTreatment(date);
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
    	
		if ( logger.isDebugEnabled() ) {
			logger.debug("Exiting HTTP endpoint (/mcpfile/{date}).");
		}
		
		return new ResponseEntity<String>(String.format("File %s processed.", filename), filename != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
    
    /**
     * HTTP endpoint (/metrics) that returns a set of counters related with the processed JSON file.
     * 
     * @return Metrics in JSON format.
     */
    @RequestMapping(value = "/metrics", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> metrics() {
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("Entering HTTP endpoint (/metrics)...");
    	}
    	
        final Metrics metrics = mcpService.metrics();
		final String response = prettyGson(metrics);
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("Exiting HTTP endpoint (/metrics).");
		}
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
    }
	
    /**
     * HTTP endpoint (/kpis) that returns a set of counters related with the service.
     * 
     * @return Metrics in JSON format.
     */
    @RequestMapping(value = "/kpis", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> kpis() {
    	if ( logger.isDebugEnabled() ) {
    		logger.debug("Entering HTTP endpoint (/kpis)...");
    	}
    	
        final KPI kpis = mcpService.kpis();
		final String response = prettyGson(kpis);
		
		if ( logger.isDebugEnabled() ) {
			logger.debug("Exiting HTTP endpoint (/kpis).");
		}
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
    }

}
