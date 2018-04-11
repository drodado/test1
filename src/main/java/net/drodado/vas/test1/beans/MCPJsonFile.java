package net.drodado.vas.test1.beans;

import java.util.List;

public class MCPJsonFile {

	private String[] contentFile;
	
	private String filename;
	
	private List<String> validJSONLines;
	
	private List<String> wrongJSONLines;
	
	
	public MCPJsonFile() {
		super();
	}


	public MCPJsonFile(final String[] lines, final String filename, final List<String> validJSONLines, final List<String> wrongJSONLines) {
		super();
		this.contentFile = lines;
		this.filename = filename;
		this.validJSONLines = validJSONLines;
		this.wrongJSONLines = wrongJSONLines;
	}


	public String[] getContentFile() {
		return contentFile;
	}

	
	public String getFilename() {
		return filename;
	}


	public List<String> getValidJSONLines() {
		return validJSONLines;
	}


	public List<String> getWrongJSONLines() {
		return wrongJSONLines;
	}


	@Override
	public String toString() {
		return "MCPJsonFile [contentFile=" + contentFile + ", filename=" + filename + "]";
	}

	public String toHttpResponse() {
		StringBuilder body = new StringBuilder(String.format("\nFile %s processed.\n\n", filename));	
		for (int i = 0; i < contentFile.length; i++) {
			body.append(String.format("%s\n", contentFile[i]));
		}
		String resume = String.format("\n\nProcessed lines resume:\n")
				.concat("----------------------------------------------------------\n")
				.concat(String.format("[%d] lines received.\n", contentFile.length))
				.concat(String.format("[%d] lines were json valid.\n", validJSONLines.size()))
				.concat(String.format("[%d] lines were wrong and will not processed.\n", wrongJSONLines.size()));
		body.append(resume);
		return body.toString();
	}

}
