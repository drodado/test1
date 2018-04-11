package net.drodado.vas.test1.service;

import net.drodado.vas.test1.beans.KPI;
import net.drodado.vas.test1.beans.Metrics;
import net.drodado.vas.test1.exceptions.MCPServiceException;

public interface MCPService {

	public Metrics metrics();
	
	public KPI kpis();
	
	public String mcpFileTreatment(String date) throws MCPServiceException;
	
}
