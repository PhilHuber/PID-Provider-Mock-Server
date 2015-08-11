package def.pkg;

import javax.swing.JOptionPane;
import javax.xml.ws.Endpoint;

public class PublishWsOnServer {
	
  public static void main( String[] args )
  {
	  PublishWsOnServer publishWsOnServer = new PublishWsOnServer();
	  publishWsOnServer.start();
  }
  
  public void start() {

	  System.out.println("");
	  System.out.println("----------------------------------------------------");
	  System.out.println("Welcome to the PID Provider Mock!");
	  System.out.println("The PID Provider Mock Server is running until you press 'OK'.");
	  System.out.println("----------------------------------------------------");
	  
	  Endpoint endpoint = Endpoint.publish( "http://localhost:8081/services", new MyWebServices() );
	  JOptionPane.showMessageDialog(null, "To stop the PID Provider Mock Server please press 'OK'.", "PID Provider Mock Server running", 2);
	  endpoint.stop();
	  
  }
  
}