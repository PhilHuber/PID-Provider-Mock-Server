package def.pkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


@WebService(name="PIDProviderMockWebServices")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class MyWebServices {
	
	public MyWebServices() {
		
	}
	
	@WebMethod(operationName = "next-Pid")
	@WebResult(name = "next-Pid-Result")
	public String nextPid(String serverIP, String databaseName, String databaseType) {
		
		String output = "";
		
		/**
         * if Database Management System is "BaseX"
         */
        if(databaseType.equalsIgnoreCase("BaseX")) {
        	
        	String queryToDatabase = "http://admin:admin@";

            if(serverIP.substring(0, 7).equalsIgnoreCase("http://")) {
              queryToDatabase += serverIP.substring(7) + "/rest/";
            }
            else {
              queryToDatabase += serverIP + "/rest/";
            }
        	        	
        	queryToDatabase += databaseName + "_Query_Store";
        	queryToDatabase += "?query=";
        	try {
				queryToDatabase += URLEncoder.encode("concat('PID for this Query: ', max(//pid) + 1, ' PIDNumber')","UTF-8");
			} 
        	catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
        	
        	//http://harryjoy.com/2012/09/08/simple-rest-client-in-java/
            HttpClient client = new DefaultHttpClient();
            
            HttpGet request = new HttpGet(queryToDatabase);
            String line = ""; 
            
            try {
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
                
                while ((line = reader.readLine()) != null) {
                    output += line + "\n";
                }
            }
            
            catch(IOException e) { 
                output = "<Error>Server is offline! Please try again later.</Error>";
            }
        }
        
        
        /**
         * if Database Management System is "EXistDB"
         */
        else if(databaseType.equalsIgnoreCase("EXistDB")) {
        	
        	String queryToDatabase = serverIP + "/exist/rest/";
        	
            if(!(databaseName.trim().substring(databaseName.trim().length() - 4).equalsIgnoreCase(".xml"))) {
            	databaseName = databaseName + ".xml";
            }
        	
            queryToDatabase += databaseName.trim().substring(0, databaseName.trim().length() - 4) + "_Query_Store.xml";
        	
            queryToDatabase += "?_query=";
        	try {
				queryToDatabase += URLEncoder.encode("concat('PID for this Query: ', max(//pid) + 1, ' PIDNumber')","UTF-8");
			} 
        	catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
            
        	//http://harryjoy.com/2012/09/08/simple-rest-client-in-java/
            HttpClient client = new DefaultHttpClient();
            
            HttpGet request = new HttpGet(queryToDatabase);
            String line = ""; 
            
            try {
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
                
                while ((line = reader.readLine()) != null) {
                    output += line + "\n";
                }
            }
            
            catch(IOException e) { 
                output = "<Error>Server is offline! Please try again later.</Error>";
            }
            
        }
        
        else {
        	// no actions to be taken.
        }
        
        output = output.substring(output.indexOf("PID for this Query: ") + 20, output.indexOf(" PIDNumber"));
		
		return output;
	}
}
