package com.lenin.monitor_video.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesRead {
	
	private static Properties prop = null;
	private static InputStream input = null;
	
	static {
		try {
			String path= System.getProperty("user.dir");
			prop = new Properties();
			input = new FileInputStream(path+"/config.properties");
			prop.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}	
	
	public static String getPortServer() {
		return prop.getProperty("port_server");
	}

	public static String getTopicServer() {
		return prop.getProperty("topic_server");
	}
	
	public static String getIpAddressServer() {
		return prop.getProperty("ip_address_server");
	}
	
	public static String getScriptFinishCameras() {
		return prop.getProperty("script_finish_cameras");
	}
	
}
