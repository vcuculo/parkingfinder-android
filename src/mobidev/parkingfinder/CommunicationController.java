package mobidev.parkingfinder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class CommunicationController {
	
	private static final String PROTOCOL = "http";
	private static final String SERVER_ADDRESS = "2.230.140.44";
	private static final int SERVER_PORT = 8081;
	private static final String SERVICE_NAME = "geoparking";	

	public static String sendRequest(String command,String data) throws IOException{
		//example: http://localhost:8080/myService/oneCommand
		URL requestURL = new URL(PROTOCOL + "://" + SERVER_ADDRESS + ":" + SERVER_PORT + "/" + SERVICE_NAME + "/" + command);
		HttpURLConnection conn = (HttpURLConnection)requestURL.openConnection();
		Log.i("REQUEST", data);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);		

		//send request
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		
		out.writeBytes(data);
		out.flush();
		out.close();

		//get response			
		String result = null;
		try {
			result = readAll(conn.getInputStream());
		}
		catch (IOException e) {
			result = readAll(conn.getErrorStream());
		}
		conn.disconnect();
		return result;
	}
	
	private static String readAll(InputStream is) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuffer result;
		String line;
		result = new StringBuffer();
		while((line = rd.readLine())!=null){
			result.append(line);
			result.append('\r');
		}
		rd.close();
		Log.e("ResultServer", result.toString());
		return result.toString();
	}

}
