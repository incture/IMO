package com.murphy.taskmgmt.ita;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murphy.integration.util.ServicesUtil;
import com.murphy.taskmgmt.util.DestinationUtil;
import com.murphy.taskmgmt.util.MurphyConstant;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

public abstract class RuleService {
	private static final Logger logger = LoggerFactory.getLogger(RuleService.class);

	public abstract RuleOutputDto getSingleResult(RuleInputDto input) throws ClientProtocolException, IOException;

	public abstract List<?> getResultList(RuleInputDto input) throws ClientProtocolException, IOException;

	protected JsonNode execute(RuleInputDto input, String rulesServiceId)
			throws ClientProtocolException, IOException {

		logger.error("[RuleService][execute]" + "[rulesServiceName]" + ": " + rulesServiceId);

		// get URL, User, Password from Destination
		//other than localhost
		DestinationConfiguration destinationConfiguration = DestinationUtil.getDest(MurphyConstant.ITARulesDestination);
		String rulesRuntimeUrl = destinationConfiguration.getProperty("URL");
		String user = destinationConfiguration.getProperty("User");
		String password = destinationConfiguration.getProperty("Password");
		String userpass = user + ":" + password;
		
		//local host
//		String rulesRuntimeUrl = "https://bpmrulesruntimerules-d998e5467.us2.hana.ondemand.com";
//		String userpass="test_roc16@gmail.com"+  ":" + "Init1234";
		//localhost
		logger.error("[RuleService][execute][destination properties]" + "[rulesRuntimeUrl]" + ": " + rulesRuntimeUrl
		+ "[user]" + ":" + userpass );
		String auth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());

		String xsrfTokenUrl = rulesRuntimeUrl + "/rules-service/rest/v2/xsrf-token";
		String invokeUrl = rulesRuntimeUrl + "/rules-service/rest/v2/workingset-rule-services";
		String ruleInputString = input.toRuleInputString(rulesServiceId);

		HttpContext httpContext = new BasicHttpContext();
		httpContext.setAttribute(HttpClientContext.COOKIE_STORE, new BasicCookieStore());
		HttpPost httpPost = null;
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		httpClient = getHTTPClient();
		httpPost = new HttpPost(invokeUrl);
		httpPost.addHeader("Content-type", "application/json");

		//SOC
		//SOCKS connection issue 
		ServicesUtil.unSetupSOCKS();
		//EOC
		// get xsrfToken
		String xsrfToken = getXSRFToken(xsrfTokenUrl, httpClient, httpContext, auth);
		if (xsrfToken != null) {
			logger.error("[RuleService][execute] after fetching" + xsrfToken);
			httpPost.addHeader("X-CSRF-Token", xsrfToken); // header

			httpPost.addHeader("Authorization", auth); // header
			logger.error("[RuleService][execute] ruleInputString" + ruleInputString);
			StringEntity stringEntity = new StringEntity(ruleInputString);

			httpPost.setEntity(stringEntity);
			response = httpClient.execute(httpPost, httpContext);

			// process your response here
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			InputStream inputStream = response.getEntity().getContent();
			byte[] data = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(data)) > 0) {
				bytes.write(data, 0, length);
			}
			String respBody = new String(bytes.toByteArray(), "UTF-8");
			logger.error("[RuleService][execute] respBody" + respBody);

			// clean-up sessions
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
			JsonNode node = new ObjectMapper().readTree(respBody);
			Iterator<JsonNode> iterator = node.get("Result").get(0).elements();
			while (iterator.hasNext()) {
				logger.error("[RuleService][execute] JsonNode before returning");
				return iterator.next();
			}
		}
		return null;
	}

	private static String getXSRFToken(String requestURL, CloseableHttpClient client, HttpContext httpContext,
			String auth) {
		HttpGet httpGet = null;
		CloseableHttpResponse response = null;
		String xsrfToken = null;

		try {
			httpGet = new HttpGet(requestURL);
			logger.error("[RuleService][getXSRFToken]  auth" + auth);
			httpGet.addHeader("Authorization", auth);
			httpGet.addHeader("X-CSRF-Token", "Fetch");
			response = client.execute(httpGet, httpContext);
			Header xsrfTokenheader = response.getFirstHeader("X-CSRF-Token");
			if (xsrfTokenheader != null) {
				logger.error("[RuleService][getXSRFToken]  xsrfToken" + xsrfToken);
				xsrfToken = xsrfTokenheader.getValue();
			}
		} catch (ClientProtocolException e) {
			logger.error("[RuleService][getXSRFToken] ClientProtocolException" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("[RuleService][getXSRFToken] IOException" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					logger.error("[RuleService][getXSRFToken] IOException while closing response" + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		return xsrfToken;
	}

	private static CloseableHttpClient getHTTPClient() {

		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		CloseableHttpClient httpClient = clientBuilder.build();
		return httpClient;
	}

	/*
	 * private static AuthenticationHeader refreshAppToAppSSOHeader(String
	 * requestURL) { System.err.println("request url" + requestURL); Context
	 * ctx; AuthenticationHeader authenticationHeader = null;
	 * AuthenticationHeaderProvider authenticationHeaderProvider = null; try {
	 * ctx = new InitialContext(); authenticationHeaderProvider =
	 * (AuthenticationHeaderProvider) ctx
	 * .lookup("java:comp/env/AuthHeaderProvider"); authenticationHeader =
	 * authenticationHeaderProvider.getAppToAppSSOHeader(requestURL); } catch
	 * (Exception e) { System.err.println("message" + e.getMessage()); }
	 * 
	 * return authenticationHeader; }
	 */

	/*
	 * public String executeWithDest(String destinationName, String absoluteUrl,
	 * String httpMethod, String contentType, String tenantId, String payload,
	 * String proxyType, boolean hasCert) {
	 * 
	 * logger.error("[RuleService][executeWithDest]" + destinationName +
	 * absoluteUrl + httpMethod + contentType + tenantId + payload + proxyType +
	 * hasCert); try { DestinationConfiguration destConfiguration = null; if
	 * (!ServicesUtil.isEmpty(destinationName)) { // destConfiguration =
	 * getDest(destinationName); } HttpURLConnection connection =
	 * injectHeaders(destConfiguration, absoluteUrl, httpMethod, contentType,
	 * tenantId, payload, proxyType, hasCert); return
	 * getDataFromConnection(connection); } catch (Exception e) { } return null;
	 * }
	 */

	/*
	 * public Map<String, String> getDest(String destinationName) {
	 * 
	 * Map<String, String> configMap = new HashMap<String, String>(); if
	 * (!ServicesUtil.isEmpty(destinationName)) {
	 * logger.error("[RuleService][getDest]" + destinationName); try {
	 * DestinationConfiguration destConfiguration =
	 * DestinationUtil.getDest(destinationName); // tenantContext =
	 * (TenantContext) // ctx.lookup("java:comp/env/tenantContext"); //
	 * DestinationConfiguration destConfiguration = //
	 * configuration.getConfiguration(destinationName);
	 * logger.error("[RuleService][getDest]" + " destConfiguration not null");
	 * 
	 * logger.error("[RuleService][getDest] user" +
	 * destConfiguration.getProperty("User"));
	 * logger.error("[RuleService][getDest] password" +
	 * destConfiguration.getProperty("Password"));
	 * 
	 * configMap = destConfiguration.getAllProperties();
	 * logger.error("[RuleService][getDest] map" + configMap); for
	 * (Map.Entry<String, String> keySet : configMap.entrySet()) {
	 * logger.error("[RuleService][getDest] key" + keySet.getKey());
	 * logger.error("[RuleService][getDest] key" + keySet.getValue());
	 * 
	 * } // return destConfiguration; } catch (Exception e) {
	 * System.err.println(e); logger.error("[RuleService][getDest message] key"
	 * + e.getMessage()); } } return configMap; }
	 */

	/*
	 * public static String getDataFromConnection(HttpURLConnection
	 * urlConnection) {
	 * 
	 * try { StringBuffer jsonString = new StringBuffer(); if
	 * (!ServicesUtil.isEmpty(urlConnection)) {
	 * 
	 * logger.error("[RuleService][executeWithDest]" + "not null connection");
	 * BufferedReader br = new BufferedReader(new
	 * InputStreamReader(urlConnection.getInputStream())); String line; while
	 * ((line = br.readLine()) != null) { jsonString.append(line); } br.close();
	 * logger.error("[RuleService][getDataFromConnection]" + "final result" +
	 * jsonString.toString()); return jsonString.toString(); } } catch
	 * (Exception e) { } return null; }
	 */

	/*
	 * private static HttpURLConnection injectHeaders(DestinationConfiguration
	 * destConfiguration, String absoluteUrl, String httpMethod, String
	 * contentType, String tenantId, String payload, String proxyType, boolean
	 * hasCert) {
	 * 
	 * logger.error("[PMC][DestinationUtil][injectHeaders][init]");
	 * 
	 * try { HttpURLConnection urlConnection = null; String authentication = "";
	 * Proxy proxy = null;
	 * 
	 * if (!ServicesUtil.isEmpty(destConfiguration)) {
	 * logger.error("[PMC][DestinationUtil][injectHeaders][init]" +
	 * " not null"); if
	 * (!ServicesUtil.isEmpty(destConfiguration.getProperty("User"))) { String
	 * user = destConfiguration.getProperty("User"); String password =
	 * destConfiguration.getProperty("Password"); authentication =
	 * ServicesUtil.getBasicAuth(user, password); }
	 * 
	 * authentication = destConfiguration.getProperty("Authentication"); String
	 * baseUrl = destConfiguration.getProperty("URL"); absoluteUrl = baseUrl +
	 * absoluteUrl; logger.error("absoluteUrl:" + absoluteUrl + "baseUrl:" +
	 * baseUrl); proxyType = destConfiguration.getProperty("ProxyType"); if
	 * (!ServicesUtil.isEmpty(proxyType)) { proxy = getProxy(proxyType); } }
	 * 
	 * URL url = new URL(absoluteUrl); if (!ServicesUtil.isEmpty(proxy)) {
	 * urlConnection = (HttpURLConnection) url.openConnection(proxy); } else {
	 * urlConnection = (HttpURLConnection) url.openConnection(); }
	 * 
	 * urlConnection.setRequestProperty("Accept", contentType);
	 * urlConnection.setRequestMethod(httpMethod);
	 * 
	 * if (hasCert) { KeyStore keyStore = destConfiguration.getKeyStore();
	 * KeyStore trustStore = destConfiguration.getTrustStore();
	 * TrustManagerFactory tmf =
	 * TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()
	 * ); tmf.init(trustStore); KeyManagerFactory keyManagerFactory =
	 * KeyManagerFactory .getInstance(KeyManagerFactory.getDefaultAlgorithm());
	 * String keyStorePassword = "Murphy$8090"; keyManagerFactory.init(keyStore,
	 * keyStorePassword.toCharArray()); SSLContext sslcontext =
	 * SSLContext.getInstance("TLS");
	 * sslcontext.init(keyManagerFactory.getKeyManagers(),
	 * tmf.getTrustManagers(), null); SSLSocketFactory sslSocketFactory =
	 * sslcontext.getSocketFactory(); ((HttpsURLConnection)
	 * urlConnection).setSSLSocketFactory(sslSocketFactory); } if
	 * (httpMethod.equals(MurphyConstant.HTTP_METHOD_POST)) {
	 * urlConnection.setDoOutput(true); } if
	 * (!ServicesUtil.isEmpty(authentication)) {
	 * 
	 * System.err.println("[authentication]" + authentication);
	 * urlConnection.setRequestProperty("Authorization", authentication);
	 * 
	 * } if (MurphyConstant.ON_PREMISE_PROXY.equals(proxyType)) { // Insert
	 * header for on-premise connectivity with the consumer // account name
	 * urlConnection.setRequestProperty("SAP-Connectivity-ConsumerAccount",
	 * tenantContext.getTenant().getAccount().getId());
	 * urlConnection.setRequestProperty("SAP-Connectivity-SCC-Location_ID",
	 * MurphyConstant.LOCATION_ID); } if (!ServicesUtil.isEmpty(payload)) {
	 * OutputStreamWriter wr = new
	 * OutputStreamWriter(urlConnection.getOutputStream()); wr.write(payload);
	 * wr.flush(); } return urlConnection;
	 * 
	 * } catch (Exception e) { System.err.println("injectHeaders message " +
	 * e.getMessage()); } return null; }
	 * 
	 * private static Proxy getProxy(String proxyType) { Proxy proxy =
	 * Proxy.NO_PROXY; String proxyHost = null; String proxyPort = null;
	 * 
	 * if (MurphyConstant.ON_PREMISE_PROXY.equals(proxyType)) { proxyHost =
	 * System.getenv("HC_OP_HTTP_PROXY_HOST"); proxyPort =
	 * System.getenv("HC_OP_HTTP_PROXY_PORT"); } else { proxyHost =
	 * System.getProperty("https.proxyHost"); proxyPort =
	 * System.getProperty("https.proxyPort"); }
	 * 
	 * if (proxyPort != null && proxyHost != null) { int proxyPortNumber =
	 * Integer.parseInt(proxyPort); proxy = new Proxy(Proxy.Type.HTTP, new
	 * InetSocketAddress(proxyHost, proxyPortNumber)); } return proxy; }
	 */
}
