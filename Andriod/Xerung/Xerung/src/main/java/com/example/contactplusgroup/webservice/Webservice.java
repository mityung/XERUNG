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
import android.util.Log;
import android.widget.Toast;

/**
 * This class is used to call Webservice from any where of this project
 * only send the data . it automatically encrpyt and decrpyt the send and 
 * receive data.It send single data at a time and single response.
 *  Don't worry
 * @author mipanther
 *
 */
@SuppressWarnings("deprecation")
public class Webservice extends AsyncTask<String, Void, String> {

	Comman comman = null;
	private String method          	= "";
	/**
	 * It take input of Response interface from where you want to get 
	 * response of this web service 
	 * @param webServiceInterface WebServiceInterface Interface refernce 
	 */
	public Webservice(WebServiceInterface webServiceInterface,String method) {		
		webServiceInter = webServiceInterface;	
		comman    = new Comman();
		this.method   = method;
	}

	protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
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
				data = params[2]+"^"+params[0];
				data = comman.symmetricEncrypt(data, comman.secretKey);				
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
			String res = getResponse.toString();
			String out = comman.symmetricDecrypt(res, comman.secretKey);
			//Log.e("sss", "--DECRYPT------"+ out);
			return out;
		} catch (Exception e) {
			e.printStackTrace();
			//Log.e("sss", "--ERROR------" + e.getMessage());
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
		webServiceInter.onWebCompleteResult(result, method);
	}


	public WebServiceInterface webServiceInter = null; /** WebServiceInterface refrence used to send response*/

	/**
	 * WebServiceInterface Interface.
	 * Used where you want to get response of web servcie
	 */
	public interface WebServiceInterface{
		/**
		 * Provide web service result 
		 * @param result data receive from server
		 */
		public void onWebCompleteResult(String result, String processName);
	}	

}
