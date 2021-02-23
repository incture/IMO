package com.murphy.downtime.util;

import java.net.Authenticator;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DowntimeServicesUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DowntimeServicesUtil.class);

	public static void setupSOCKS() {
		try {
			String auth = setSOCKS5ProxyAuthentication(DowntimeConstant.CLOUD_SUBACCOUNT_NAME,
					DowntimeConstant.CLOUD_LOCATION);
			System.setProperty(DowntimeConstant.SOCK_HOST_NAME, DowntimeConstant.SOCKS_HOST);
			System.setProperty( DowntimeConstant.SOCKS_PORT_NAME, DowntimeConstant.SOCKS_PORT);
			System.setProperty("java.net.socks.username", auth);
		} catch(Exception e){
			logger.error("Proxy Setup Exception : "+e.getMessage());
		}

	}

	private static String setSOCKS5ProxyAuthentication(String subaccount, String locationId) {
		final String encodedSubaccount = new String(Base64.getEncoder().encodeToString(subaccount.getBytes()));
		final String encodedLocationId = new String(Base64.getEncoder().encodeToString(locationId.getBytes()));
		Authenticator.setDefault(new Authenticator() {
			@Override
			protected java.net.PasswordAuthentication getPasswordAuthentication() {
				return new java.net.PasswordAuthentication("1." + encodedSubaccount + "." + encodedLocationId,
						new char[] {});
			}
		});
		return "1." + encodedSubaccount + "." + encodedLocationId;
	}
	
	public static void unSetupSOCKS() {
		try {
			System.setProperty(DowntimeConstant.SOCK_HOST_NAME, "");
			System.setProperty( DowntimeConstant.SOCKS_PORT_NAME, "");
			System.setProperty("java.net.socks.username", "");
		} catch (Exception e) {
			logger.error("Proxy Unsetup Exception : "+e.getMessage());
		}
	}
}
