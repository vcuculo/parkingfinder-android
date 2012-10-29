package mobidev.parkingfinder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class CommunicationController {

	private static final String PROTOCOL = "http";
	private static final String SERVER_ADDRESS = "parking.findu.pl";
	private static final int SERVER_PORT = 80;
	private static final String SERVICE_NAME = "geoparking";

	public static String sendRequest(String command, String data)
			throws IOException {

		String result = null;
		// example: http://localhost:8080/myService/oneCommand
		URL requestURL = new URL(PROTOCOL + "://" + SERVER_ADDRESS + ":"
				+ SERVER_PORT + "/" + SERVICE_NAME + "/" + command);
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) requestURL.openConnection();
			Log.i("REQUEST", data);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(120 * 1000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// send request
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());

			out.writeBytes(data);
			out.flush();
			out.close();

			// get response
			try {
				result = readAll(conn.getInputStream());
			} catch (IOException e) {
				result = readAll(conn.getErrorStream());
			}
			conn.disconnect();
		} catch (ConnectException e1) {
			e1.printStackTrace();
			return "ServerError";
		}
		return result;
	}

	public static String sendFourSquareRequest(String command, boolean post,
			String data) throws IOException {
		String url = "https://api.foursquare.com/v2/" + command;
		if (!post)
			url += data;

		URL requestURL = new URL(url);

		HttpURLConnection conn;
		String result = "";
		conn = (HttpURLConnection) requestURL.openConnection();
		conn.setConnectTimeout(120 * 1000);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		if (!post)
			conn.setRequestMethod("GET");
		else {
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			DataOutputStream out = new DataOutputStream(conn.getOutputStream());
			out.writeBytes(data);
			out.flush();
			out.close();
		}

		// get response
		try {
			result = readAll(conn.getInputStream());
		} catch (IOException e) {
			result = readAll(conn.getErrorStream());
		}
		conn.disconnect();
		return result;
	}

	public static String readAll(InputStream is) throws IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuffer result;
		String line;
		result = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			result.append(line);
			result.append('\r');
		}
		rd.close();
		Log.e("ResultServer", result.toString());
		return result.toString();
	}

}
