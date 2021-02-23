package com.murphy.taskmgmt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.murphy.taskmgmt.dao.ConfigDao;

public class RestUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(RestUtil.class);
	
	public static JSONObject callRest(String url, String entity, String method, String user, String pass) {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		StringEntity input = null;
		String json = null;
		JSONObject obj = null;
		if (!ServicesUtil.isEmpty(url)) {
			if (method.equalsIgnoreCase("GET")) {
				httpRequestBase = new HttpGet(url);
			} else if (method.equalsIgnoreCase("POST")) {
				httpRequestBase = new HttpPost(url);
				try {
					input = new StringEntity(entity);
					input.setContentType("application/json");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				((HttpPost) httpRequestBase).setEntity(input);
			}
			httpRequestBase.addHeader("accept", "application/json");
			if (!ServicesUtil.isEmpty(user) && !ServicesUtil.isEmpty(pass)) {
				String userPassword = user + ":" + pass;
				byte[] encodeBase64 = Base64.encodeBase64(userPassword.getBytes());
				httpRequestBase.addHeader("Authorization", "BASIC " + new String(encodeBase64));
			}
			try {
				httpResponse = httpClient.execute(httpRequestBase);
				json = EntityUtils.toString(httpResponse.getEntity());
//				System.err.println("JsonObject"+json);

			} catch (IOException e) {
				logger.error("IOException : " + e);
			}

			try {
				obj = new JSONObject(json);
			} catch (JSONException e) {
				logger.error("JSONException : " + e + "JSON Object : "+json);
			}

			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("Closing HttpClient Exception : " + e);
			}

		}
		return obj;
	}
	
	public static void main(String[] args) {
		System.out.println(RestUtil.callRestURL("http://houqgisprod:6080/arcgis/rest/services/Networks/Road_Network_TX/NAServer/Route/solve?stops=-99.0236,28.4644;-98.6424,28.4758;-99.0407,28.2402&returnDirections=true&returnRoutes=true&findBestSequence=true&preserveFirstStop=true&f=json", null, "GET", false));
		System.out.println(RestUtil.callRestURL("https://taskmanagementrestd998e5467.us2.hana.ondemand.com/TaskManagement_Rest/murphy/alarmFeed/getAlarm", "{ \"locationType\" : \"Field\", \"locations\" : \"'MUR-US-EFS-CT00'\", \"acknowledged\" : true, \"page\" : 1 }", "POST", false));
	}
	
	public static JSONObject callRestURL(String requestURL, String entity, String method, Boolean sapCC) {
		
		ConfigDao configDao = SpringContextBridge.services().getConfigDao();
		
		URL url = null;
		HttpURLConnection httpURLConnection = null;
		InputStream inputStream = null;
		BufferedReader reader = null;
		JSONObject jsonObjectResponse = null;
		StringBuilder responseStrBuilder = null;
		OutputStream outputStream = null;
		String line = "";
		try {
			url = new URL(requestURL);
			httpURLConnection = (HttpURLConnection) url.openConnection(getProxy(MurphyConstant.ON_PREMISE_PROXY));
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestMethod(method);
			httpURLConnection.setRequestProperty(MurphyConstant.HTTP_HEADER_CONTENT_TYPE, MurphyConstant.APPLICATION_JSON);
			if(sapCC) {
				httpURLConnection.setRequestProperty("SAP-Connectivity-ConsumerAccount",
						configDao.getConfigurationByRef(MurphyConstant.TENANT_ID_REF));
				httpURLConnection.setRequestProperty("SAP-Connectivity-SCC-Location_ID",
						configDao.getConfigurationByRef(MurphyConstant.LOCATION_ID_HOUST_REF));
			}
			if(MurphyConstant.HTTP_METHOD_POST.equals(method)) {
				httpURLConnection.setDoOutput(true);
				outputStream = httpURLConnection.getOutputStream();
				outputStream.write(entity.getBytes());
				outputStream.flush();
			}
			
			if (httpURLConnection != null) {
				inputStream = httpURLConnection.getInputStream();
			}
			reader = new BufferedReader(new InputStreamReader(inputStream));
			responseStrBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				responseStrBuilder.append(line);
			}
			reader.close();
			jsonObjectResponse = new JSONObject(responseStrBuilder.toString());
		} catch (Exception ex) {
			logger.error("Exception while calling URL : "+ex.getMessage());
		}
		
		return jsonObjectResponse;
	}
	
	private static Proxy getProxy(String proxyType) {
		Proxy proxy = Proxy.NO_PROXY;
		String proxyHost = null;
		String proxyPort = null;

		if (MurphyConstant.ON_PREMISE_PROXY.equals(proxyType)) {
			proxyHost = System.getenv("HC_OP_HTTP_PROXY_HOST");
			proxyPort = System.getenv("HC_OP_HTTP_PROXY_PORT");
		} else {
			proxyHost = System.getProperty("https.proxyHost");
			proxyPort = System.getProperty("https.proxyPort");
		}

		if (proxyPort != null && proxyHost != null) {
			int proxyPortNumber = Integer.parseInt(proxyPort);
			proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPortNumber));
		}
		return proxy;
	}
	
	public static JSONObject callRestParams(String url, List<NameValuePair> params, String method, String user, String pass) throws UnsupportedEncodingException {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		StringEntity input = null;
		String json = null;
		JSONObject obj = null;
		if (!ServicesUtil.isEmpty(url)) {
			if (method.equalsIgnoreCase("GET")) {
				httpRequestBase = new HttpGet(url);
			} else if (method.equalsIgnoreCase("POST")) {
				httpRequestBase = new HttpPost(url);
				try {
					input = new UrlEncodedFormEntity(params, "UTF-8");
					input.setContentType("application/form-data");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				((HttpPost) httpRequestBase).setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			}
//			httpRequestBase.addHeader("accept", "application/json");
			if (!ServicesUtil.isEmpty(user) && !ServicesUtil.isEmpty(pass)) {
				String userPassword = user + ":" + pass;
				byte[] encodeBase64 = Base64.encodeBase64(userPassword.getBytes());
				httpRequestBase.addHeader("Authorization", "BASIC " + new String(encodeBase64));
			}
			try {
				httpResponse = httpClient.execute(httpRequestBase);
				json = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				logger.error("IOException : " + e);
			}

			try {
				obj = new JSONObject(json);
			} catch (JSONException e) {
				logger.error("JSONException : " + e + "JSON Object : "+json);
			}

			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("Closing HttpClient Exception : " + e);
			}

		}
		return obj;
	}
	
	public static JSONObject callRestBearer(String url, String entity, String method, String token) {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		StringEntity input = null;
		String json = null;
		JSONObject obj = null;
		if (!ServicesUtil.isEmpty(url)) {
			if (method.equalsIgnoreCase("GET")) {
				httpRequestBase = new HttpGet(url);
			} else if (method.equalsIgnoreCase("POST")) {
				httpRequestBase = new HttpPost(url);
				try {
					input = new StringEntity(entity);
//					input.setContentType("application/json");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				((HttpPost) httpRequestBase).setEntity(input);
			}
			httpRequestBase.addHeader("accept", "application/json");
			httpRequestBase.addHeader("Content-Type", "application/json");
			if(!ServicesUtil.isEmpty(token)) {
				httpRequestBase.addHeader("Authorization", "Bearer " + token);
			}
			try {
				httpResponse = httpClient.execute(httpRequestBase);
				json = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				logger.error("IOException : " + e);
			}

			try {
				obj = new JSONObject(json);
			} catch (JSONException e) {
				logger.error("JSONException : " + e + "JSON Object : "+json);
			}

			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("Closing HttpClient Exception : " + e);
			}

		}
		return obj;
	}
	
public HttpResponse callDocService(String serviceURL, String entity) {
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpPut httpPut=new HttpPut(serviceURL);
		HttpResponse httpResponse = null;
		
				httpPut.setHeader("Content-Type","application/json");
				StringEntity params;
				try {
					params = new StringEntity(entity);
					httpPut.setEntity(params);
				} catch (UnsupportedEncodingException e) {
					logger.error("Unable to convert input entity : " + e.getMessage());
				}
				try {
					httpResponse = httpClient.execute(httpPut);
				}catch (Exception ex) {
					logger.error("Exception encountered while fetching response : "+ex.getMessage());
					return null;
				}
				return httpResponse;
		
	}



	//For AutoTaskScheduling 
	public static JSONObject callRestforATS(String url, String browseNodesGetPayLoad, String httpMethod) {
	
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		StringEntity input = null;
		String json = null;
		JSONObject obj = null;
	
		if (!ServicesUtil.isEmpty(url)) {
	
			if (httpMethod.equalsIgnoreCase(MurphyConstant.HTTP_METHOD_GET)) {
				httpRequestBase = new HttpGet(url);
			} else if (httpMethod.equalsIgnoreCase(MurphyConstant.HTTP_METHOD_POST)) {
	
				httpRequestBase = new HttpPost(url);
	
				try {
					input = new StringEntity(browseNodesGetPayLoad);
				} catch (UnsupportedEncodingException e) {
					logger.error("UnsupportedEncodingException : " + e.getMessage());
				}
				((HttpPost) httpRequestBase).setEntity(input);
			}
	
			httpRequestBase.addHeader("accept", "application/json");
			httpRequestBase.addHeader("Content-Type", "application/json");
	
			try {
				httpResponse = httpClient.execute(httpRequestBase);
				json = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				logger.error("IOException : " + e.getMessage());
			}
	
			try {
				obj = new JSONObject(json);
			} catch (JSONException e) {
				logger.error("JSONException : " + e + "JSON Object : "+json);
			}
	
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("Closing HttpClient Exception : " + e.getMessage());
			}
	
		}
	
		return obj;
	
	}
	
	
	//Canary Rest Call Function
	public static JSONObject callRest(String url, String browseNodesGetPayLoad, String httpMethod) {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		StringEntity input = null;
		String json = null;
		JSONObject obj = null;

		com.murphy.integration.util.ServicesUtil.unSetupSOCKS();
		if (!ServicesUtil.isEmpty(url)) {

			if (httpMethod.equalsIgnoreCase(MurphyConstant.HTTP_METHOD_GET)) {
				httpRequestBase = new HttpGet(url);
			} else if (httpMethod.equalsIgnoreCase(MurphyConstant.HTTP_METHOD_POST)) {

				httpRequestBase = new HttpPost(url);

				try {
					input = new StringEntity(browseNodesGetPayLoad);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				((HttpPost) httpRequestBase).setEntity(input);
			}

			httpRequestBase.addHeader("accept", "application/json");
			httpRequestBase.addHeader("Content-Type", "application/json");

			try {
				httpResponse = httpClient.execute(httpRequestBase);
				json = EntityUtils.toString(httpResponse.getEntity());
			} catch (IOException e) {
				logger.error("IOException : " + e);
			}

			try {
				obj = new JSONObject(json);
			} catch (JSONException e) {
				logger.error("JSONException : " + e + "JSON Object : "+json);
			}

			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("Closing HttpClient Exception : " + e);
			}

		}

		return obj;

	}
	
public static JSONObject callUserTokenRest(String url, String entity, String method, String user, String pass) {
		
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpRequestBase httpRequestBase = null;
		HttpResponse httpResponse = null;
		StringEntity input = null;
		String json = null;
		JSONObject obj = null;
		
		com.murphy.integration.util.ServicesUtil.unSetupSOCKS();

		
		if (!ServicesUtil.isEmpty(url)) {
			
			if (method.equalsIgnoreCase("GET")) {
				httpRequestBase = new HttpGet(url);
			} else if (method.equalsIgnoreCase("POST")) {
				httpRequestBase = new HttpPost(url);
				
				try {
					input = new StringEntity(entity);
					input.setContentType("application/json");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				((HttpPost) httpRequestBase).setEntity(input);
			}
			
			httpRequestBase.addHeader("accept", "application/json");
			
			if (!ServicesUtil.isEmpty(user) && !ServicesUtil.isEmpty(pass)) {
				String userPassword = user + ":" + pass;
				byte[] encodeBase64 = Base64.encodeBase64(userPassword.getBytes());
				httpRequestBase.addHeader("Authorization", "BASIC " + new String(encodeBase64));
			}
			
			try {
				httpResponse = httpClient.execute(httpRequestBase);
				json = EntityUtils.toString(httpResponse.getEntity());
				//System.err.println("JsonObject: "+json);

			} catch (IOException e) {
				logger.error("IOException : " + e);
			}

			try {
				obj = new JSONObject(json);
			} catch (JSONException e) {
				logger.error("JSONException : " + e + "JSON Object : "+json);
			}

			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("Closing HttpClient Exception : " + e);
			}

		}
		return obj;

	}


}
