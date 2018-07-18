package com.lenin.monitor_video;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.lenin.monitor_video.config.PropertiesRead;

/**
 * Hello world!
 *
 */
public class App 
{
	private final static Logger logger = Logger.getLogger(App.class.getName());
	
	private void inMessage(String contents){
		try {
			JSONObject object = (JSONObject) new JSONParser().parse(contents);
			String id = String.valueOf(object.get("id"));
			logger.info("ID : "+id);
			scriptExecute(id);
		} catch (NullPointerException e) {
			logger.error("NullPointerException "+e.getMessage());
		} catch (ParseException e) {
			logger.error("ParseException "+e.getMessage());
		}
	}
	
	private void scriptExecute(String nameFolder){

		Process process;
		try {
			nameFolder = nameFolder+"_"+System.currentTimeMillis();
			logger.info("Executing sh for copy and create folder "+nameFolder);
			process = Runtime.getRuntime().exec(PropertiesRead.getScriptFinishCameras() + " "+nameFolder);
			process.waitFor();
			
			BufferedReader reader = 
			         new BufferedReader(new InputStreamReader(process.getInputStream()));

			    String line = "";			
			    while ((line = reader.readLine())!= null) {
			    	logger.info(line + "\n");
			    }
		} catch (IOException e) {
			logger.error(e.getClass() +" : "+e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getClass() +" : "+e.getMessage());
		}
	}
	
    public static void main( String[] args )
    {
    	// Prepare our context and subscriber
        Context context = ZMQ.context(1);
        Socket subscriber = context.socket(ZMQ.SUB);

        subscriber.connect("tcp://"+PropertiesRead.getIpAddressServer()+":"+PropertiesRead.getPortServer());
        subscriber.subscribe(PropertiesRead.getTopicServer().getBytes());
        logger.info("Monitor Video waiting...");
        while (!Thread.currentThread ().isInterrupted ()) {
            // Read envelope with address
            String address = subscriber.recvStr ();
            // Read message contents
            String contents = subscriber.recvStr ();
            logger.info(address + " : " + contents);
            new App().inMessage(contents);
        }
        subscriber.close ();
        context.term ();
 
    }
}
