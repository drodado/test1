package net.drodado.vas.test1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Environment {

	private static Properties p = null;
		
	private static void loadFile() throws IOException {
		p = new Properties();
		InputStream propertiesStream = ClassLoader.getSystemResourceAsStream("service_config.properties");
		p.load(propertiesStream);
		propertiesStream.close();
	}

	public static String getUrlForMCPFile() throws IOException {
		return getProperty("mcpfile.url");
	}
	
	public static String getMCPFileExternsion() throws IOException {
		return getProperty("mcpfile.extension");
	}
	
	public static String getMCPFilenamePattern() throws IOException {
		return getProperty("mcpfile.filename.pttrn");
	}
	
	private static String getProperty(String property) throws IOException {
		if ( p == null ) {
			loadFile();
		}
		return p.getProperty(property);
	}
	
}
