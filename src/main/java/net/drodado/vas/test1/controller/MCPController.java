package net.drodado.vas.test1.controller;

import org.springframework.http.ResponseEntity;

public interface MCPController {

	public ResponseEntity<String> mcpFileTreatment(String date);
	
	public ResponseEntity<String> metrics();
	
	public ResponseEntity<String> kpis();
	
}
