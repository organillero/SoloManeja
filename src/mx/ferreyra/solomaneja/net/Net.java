package mx.ferreyra.solomaneja.net;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.List;

import mx.ferreyra.solomaneja.exceptions.OfflineException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.GsonBuilder;


public class Net {

	private Context context;

	public Net(Context context){
		this.context = context;

	}




	public Object sendDataAndGetObject  (String url, List<NameValuePair> nameValuePairs, Type type) throws OfflineException,  IOException, Exception {

		Object o = null;
		InputStream is = sendData( url, nameValuePairs);

		if (is != null && is instanceof InputStream){
			String ansUrl = inputStreamToString (is);
			o = new GsonBuilder()
					.setDateFormat("yyyy-MM-dd HH:mm:ss")
					.create().
					fromJson(ansUrl, type);
			
			
		}
		return o;
	}

	public InputStream sendData(String url, List<NameValuePair> nameValuePairs) throws  OfflineException, IOException, Exception {

		if (isOnline()== false){
			throw new OfflineException();
		}

		HttpParams httpParameters = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
		HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);

		HttpRequestBase httpRequest;


		//Es un POST 
		if (nameValuePairs != null){

			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			for(int index=0; index < nameValuePairs.size(); index++) {
				if(nameValuePairs.get(index).getName().equalsIgnoreCase("avatar")) {
					// If the key equals to "image", we use FileBody to transfer the data
					entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File (nameValuePairs.get(index).getValue())));
				} else {
					// Normal string data
					entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
				}
			}

			httpRequest = new HttpPost(url);
			((HttpPost) httpRequest).setEntity(entity);
		}
		//ES UN GET
		else{
			httpRequest = new HttpGet(url);
		}

		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		HttpResponse response = httpClient.execute(httpRequest);


		return  response.getEntity().getContent();

	}



	protected String addParameter(String URL, String name, String value)
	{
		int qpos = URL.indexOf('?');
		int hpos = URL.indexOf('#');
		char sep = qpos == -1 ? '?' : '&';
		String seg = sep + encodeUrl(name) + '=' + encodeUrl(value);
		return hpos == -1 ? URL + seg : URL.substring(0, hpos) + seg+ URL.substring(hpos);
	}


	protected  boolean isOnline() {		
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean ans;
		final NetworkInfo network_info = cm.getActiveNetworkInfo();

		ans = network_info != null && network_info.isConnected() ? true : false; 
		return ans;
	}

//TODO
	public String inputStreamToString (InputStream in)  {		
		StringBuilder out = new StringBuilder();

		if (in == null) return null;

		byte[] b = new byte[4096];
		try {

			for (int n; (n = in.read(b)) != -1;) {
				out.append(new String(b, 0, n));
			}
			return out.toString();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}



	private  String encodeUrl(String url)
	{
		try{
			return URLEncoder.encode(url, "UTF-8");		}
		catch (UnsupportedEncodingException uee){
			throw new IllegalArgumentException(uee);	}
	}

}
