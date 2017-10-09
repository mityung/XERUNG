package com.example.contactplusgroup.webservice;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.http.HttpEntity;
import com.example.contactplusgroup.common.Comman;
import android.os.AsyncTask;

/**
 * This class is used to call Webservice from any where of this project
 * only send the data . it automatically encrpyt and decrpyt the send and 
 * receive data.It send single data at a time and single response.
 *  Don't worry
 * @author mipanther
 *
 */
@SuppressWarnings("deprecation")
public class CallWebService extends AsyncTask<String, Void, String> {

	Comman comman = null;
	
	/**
	 * It take input of Response interface from where you want to get 
	 * response of this web service 
	 * @param webServiceInterface WebServiceInterface Interface refernce 
	 */
	public CallWebService(WebServiceInterfaceSMS webServiceInterface) {		
		webServiceInter = webServiceInterface;	
		comman = new Comman();
	}

	protected String getASCIIContentFromEntity(org.apache.http.HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();
		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n > 0) {
			byte[] b = new byte[4096];
			n = in.read(b);
			if (n > 0)
				out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	@Override
	protected String doInBackground(String... params) {

		URL url;
		HttpURLConnection connection = null;

		try {	

			String data = "";				
			try {				
				data = params[0];			
			} catch (Exception e) {
				// TODO: handle exception
				data = params[0];
			}

			url        = new URL(params[1]);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text/plain");
			// application/x-www-form-urlencoded
			//connection.setRequestProperty("Content-Length",
			//		"" + Integer.toString(params[0].getBytes().length));
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();
			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder getResponse = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				getResponse.append(line);
				getResponse.append('\r');
			}
			
			rd.close();

			return getResponse.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub		
		webServiceInter.onWebCompleteResult(result);
	}


	public WebServiceInterfaceSMS webServiceInter = null; /** WebServiceInterface refrence used to send response*/

	/**
	 * WebServiceInterface Interface.
	 * Used where you want to get response of web servcie
	 */
	public interface WebServiceInterfaceSMS{
		/**
		 * Provide web service result 
		 * @param result data receive from server
		 */
		public void onWebCompleteResult(String result);
	}	

}
