package com.murphy.taskmgmt.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.KeyStore;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.account.TenantContext;
import com.sap.core.connectivity.api.configuration.ConnectivityConfiguration;
import com.sap.core.connectivity.api.configuration.DestinationConfiguration;

public class DestinationUtil {

	private static final Logger logger = LoggerFactory.getLogger(DestinationUtil.class);

	private static TenantContext tenantContext;

	public static DestinationConfiguration getDest(String destinationName) {

		if (!ServicesUtil.isEmpty(destinationName)) {

			try {
				Context ctx = new InitialContext();
				ConnectivityConfiguration configuration = (ConnectivityConfiguration) ctx
						.lookup("java:comp/env/connectivityConfiguration");
				tenantContext = (TenantContext) ctx.lookup("java:comp/env/tenantContext");
				// DestinationConfiguration destConfiguration =
				// configuration.getConfiguration(destinationName);
				Map<String, DestinationConfiguration> configurations = configuration
						.getConfigurations(tenantContext.getTenant().getAccount().getId());
				DestinationConfiguration destConfiguration = configurations.get(destinationName);
				return destConfiguration;
			} catch (Exception e) {
				logger.error("[PMC][DestinationUtil][getDest][error]" + e + "[getMessage]" + e.getMessage());
			}
		}
		return null;
	}

	public static String executeWithDest(String destinationName, String absoluteUrl, String httpMethod,
			String contentType, String tenantId, String payload, String proxyType, boolean hasCert) {

		// logger.error("[PMC][DestinationUtil][executeWithDest][init]");
		try {
			DestinationConfiguration destConfiguration = null;
			if (!ServicesUtil.isEmpty(destinationName)) {
				destConfiguration = getDest(destinationName);
			}
			HttpURLConnection connection = injectHeaders(destConfiguration, absoluteUrl, httpMethod, contentType,
					tenantId, payload, proxyType, hasCert);
			return getDataFromConnection(connection);
		} catch (Exception e) {
			logger.error("[PMC][DestinationUtil][executeWithDest][error]" + e.getMessage());
		}
		return null;
	}

	public static String getDataFromConnection(HttpURLConnection urlConnection) {

		try {
			StringBuffer jsonString = new StringBuffer();
			if (!ServicesUtil.isEmpty(urlConnection)) {
				BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					jsonString.append(line);
				}
				br.close();
				// logger.error("[connection.getResponseCode()]" +
				// urlConnection.getResponseCode() + "[jsonString.toString()]" +
				// jsonString.toString());
				return jsonString.toString();
			}
		} catch (Exception e) {
			logger.error("[PMC][DestinationUtil][getDataFromConnection][error]" + e.getMessage());
		}
		return null;
	}

	private static HttpURLConnection injectHeaders(DestinationConfiguration destConfiguration, String absoluteUrl,
			String httpMethod, String contentType, String tenantId, String payload, String proxyType, boolean hasCert) {

		// logger.error("[PMC][DestinationUtil][injectHeaders][init]");

		try {
			HttpURLConnection urlConnection = null;
			String authentication = "";
			Proxy proxy = null;

			if (!ServicesUtil.isEmpty(destConfiguration)) {

				if (!ServicesUtil.isEmpty(destConfiguration.getProperty("User"))
						&& !destConfiguration.getProperty("User").equals("Murphy_API_02")) {
					String user = destConfiguration.getProperty("User");
					String password = destConfiguration.getProperty("Password");
					authentication = ServicesUtil.getBasicAuth(user, password);
				}

				String baseUrl = destConfiguration.getProperty("URL");
				absoluteUrl = baseUrl + absoluteUrl;
				// logger.error("absoluteUrl:"+absoluteUrl+"url:"+url);
				proxyType = destConfiguration.getProperty("ProxyType");
				if (!ServicesUtil.isEmpty(proxyType)) {
					proxy = getProxy(proxyType);
				}
			}

			URL url = new URL(absoluteUrl);
			if (!ServicesUtil.isEmpty(proxy)) {
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}

			urlConnection.setRequestProperty("Accept", contentType);
			urlConnection.setRequestMethod(httpMethod);

			if (hasCert) {
				KeyStore keyStore = destConfiguration.getKeyStore();
				KeyStore trustStore = destConfiguration.getTrustStore();
				TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				tmf.init(trustStore);
				KeyManagerFactory keyManagerFactory = KeyManagerFactory
						.getInstance(KeyManagerFactory.getDefaultAlgorithm());
				String keyStorePassword = "Murphy$8090";
				keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
				SSLContext sslcontext = SSLContext.getInstance("TLS");
				sslcontext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
				SSLSocketFactory sslSocketFactory = sslcontext.getSocketFactory();
				((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslSocketFactory);
			}
			if (httpMethod.equals(MurphyConstant.HTTP_METHOD_POST)) {
				urlConnection.setDoOutput(true);
			}
			if (!ServicesUtil.isEmpty(authentication)) {

				// System.err.println("[authentication]"+authentication);
				urlConnection.setRequestProperty("Authorization", authentication);

			}
			if (MurphyConstant.ON_PREMISE_PROXY.equals(proxyType)) {
				// Insert header for on-premise connectivity with the consumer
				// account name
				urlConnection.setRequestProperty("SAP-Connectivity-ConsumerAccount",
						tenantContext.getTenant().getAccount().getId());
				urlConnection.setRequestProperty("SAP-Connectivity-SCC-Location_ID", MurphyConstant.LOCATION_ID);
			}
			if (!ServicesUtil.isEmpty(payload)) {
				OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
				wr.write(payload);
				wr.flush();
			}
			return urlConnection;

		} catch (Exception e) {
			logger.error("[PMC][DestinationUtil][injectHeaders][error]" + e.getMessage());
		}
		return null;
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

}
