package dcc.ufmg.anthill.net;
/**
 * @author Rodrigo Caetano O. ROCHA
 * @date 26 July 2013
 */

import java.io.*;
import java.net.*;

public class WebClient {
	public static String getContent(String urlToRead) throws ConnectException, ProtocolException, IOException {
      URL url;
      HttpURLConnection conn;
      BufferedReader rd;
      String line;
      String result = "";

      url = new URL(urlToRead);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      while ((line = rd.readLine()) != null) {
         result += line;
      }
      rd.close();

      return result;
   }
	/*
	public static void main(String []args){
		try {
			System.out.println(WebClient.getContent("http://localhost:8080/host/rcor/"));
		}catch(Exception e){e.printStackTrace();}
	}
	*/
}

