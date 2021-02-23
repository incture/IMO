package com.murphy.integration.util;

import java.net.Authenticator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class ServicesUtil {

	private static final Logger logger = LoggerFactory.getLogger(ServicesUtil.class);
	
	 public static String EFS_CODE="EFS";
	 public static  String CA_CODE="CA";
	 public static String EFS_BASE_MUWI="926";
	 public static String CANADA_BASE_MUWI="CAN";
	 public static String EFS_BASE_LOC_CODE="MUR-US-EFS";
	 public static  String CANADA_BASE_LOC_CODE="MUR-CA";

	public static void setupSOCKS(String location) {
		if (location != null) {
			try {
				// String auth =
				// setSOCKS5ProxyAuthentication(ApplicationConstant.CLOUD_SUBACCOUNT_NAME,
				// location);

				// logger.error("[setupSOCKS] Info - Subaccount" +
				// ApplicationConstant.CLOUD_SUBACCOUNT_NAME);
				// logger.error("[setupSOCKS] Info - Location" + location);

				System.setProperty(ApplicationConstant.SOCK_HOST_NAME, ApplicationConstant.SOCKS_HOST);
				System.setProperty(ApplicationConstant.SOCKS_PORT_NAME, ApplicationConstant.SOCKS_PORT);
				setSOCKS5ProxyAuthentication(ApplicationConstant.CLOUD_SUBACCOUNT_NAME, location);

				System.setProperty("socksNonProxyHosts", ApplicationConstant.SOCKS_NONPROXY_HOST
						+ "|my87.geotab.com|login.microsoftonline.com|api.powerbi.com|murphy.canarylabs.online|murphyidp.accounts.ondemand.com|houqgisprod|vsa002536|vsa002537|vsa4733354|domaindb.svc.us2.hana.ondemand.com");
				// System.err.println("socksNonProxyHosts :
				// "+System.getProperty("socksNonProxyHosts"));
				// System.setProperty("java.net.socks.username", auth);
			} catch (Exception e) {
				System.err.println("Proxy Setup Exception : " + e.getMessage());
			}
		} else {
			unSetupSOCKS();
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
			System.setProperty(ApplicationConstant.SOCK_HOST_NAME, "");
			System.setProperty(ApplicationConstant.SOCKS_PORT_NAME, "");
			System.setProperty("java.net.socks.username", "");
		} catch (Exception e) {
			logger.error("Proxy Unsetup Exception : " + e.getMessage());
		}
	}

	public static String getQueryString(Set<String> inputSet, String initialQuery, List<String> inputList) {
		if (inputSet != null && inputSet.size() > 0) {
			inputList.addAll(inputSet);
		}
		String responseString = "";
		if (inputList != null && inputList.size() > 0) {
			if (inputList.size() < 1000) {
				String tempString = "";
				for (int i = 0; i < inputList.size(); i++) {
					if (i == 0) {
						tempString = "'" + inputList.get(i) + "'";
					} else
						tempString = tempString + ", '" + inputList.get(i) + "'";
				}
				responseString = (initialQuery + " IN (" + tempString + ")");
			} else {
				double size = inputList.size();
				double chunkSize = 999;
				StringBuffer inputListBuffer = new StringBuffer("");
				int numOfChunks = (int) Math.ceil((double) size / chunkSize);
				for (int i = 0; i < numOfChunks; ++i) {
					int start = (int) (i * chunkSize);
					int countSize = (int) Math.min(size - start, chunkSize);
					String tempString = "";
					for (int j = start; j < start + countSize; j++) {
						if (j == start) {
							tempString = "'" + inputList.get(j) + "'";
						} else
							tempString = tempString + ", '" + inputList.get(j) + "'";
					}
					if (i == 0)
						inputListBuffer.append("(" + initialQuery + " IN (" + tempString + ") ");
					else
						inputListBuffer.append("OR " + initialQuery + " IN (" + tempString + ") ");

				}
				responseString = inputListBuffer.toString() + ")";
			}
		}
		return responseString;
	}

	public static boolean isEmpty(Object[] objs) {
		if (objs == null || objs.length == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		} else if (o.toString().equals("")) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Collection<?> o) {
		if (o == null || o.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(StringBuffer sb) {
		if (sb == null || sb.length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(StringBuilder sb) {
		if (sb == null || sb.length() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Element nd) {
		if (nd == null) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(NamedNodeMap nd) {
		if (nd == null || nd.getLength() == 0) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Node nd) {
		if (nd == null) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(NodeList nd) {
		if (nd == null || nd.getLength() == 0) {
			return true;
		}
		return false;
	}

	public static Date convertFromZoneToZone(Date fromDate, Object fromDateObject, String fromZone, String toZone,
			String fromFormat, String toFormat) {

		try {
			if (!ServicesUtil.isEmpty(fromDateObject)) {
				DateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
				if (!isEmpty(fromZone)) {
					fromDateFormat.setTimeZone(TimeZone.getTimeZone(fromZone));
				}
				fromDate = fromDateFormat.parse(fromDateObject.toString());
			} else if (ServicesUtil.isEmpty(fromDate)) {
				fromDate = new Date();
			}
			DateFormat toDateFormat = new SimpleDateFormat(toFormat);
			if (!ServicesUtil.isEmpty(toZone)) {
				toDateFormat.setTimeZone(TimeZone.getTimeZone(toZone));
			}
			String returnValueInString = toDateFormat.format(fromDate.getTime());
			// System.err.println("[Murphy][ServicesUtil][convertFromZoneToZone][returnValueInString]
			// : " + returnValueInString +"\n fromDate :"+fromDate +
			// "fromDateObject :"+fromDateObject +"fromZone :"+fromZone +"
			// toZone : "+toZone+ "fromFormat : "+fromFormat + " toFormat :"
			// +toFormat);
			return toDateFormat.parse(returnValueInString);

		} catch (Exception e) {
			System.err.println("[Murphy][ServicesUtil][convertFromZoneToZone][error] : " + e.getMessage()
					+ "\n fromDate :" + fromDate + "fromDateObject :" + fromDateObject + "fromZone :" + fromZone
					+ " toZone : " + toZone + "fromFormat : " + fromFormat + " toFormat :" + toFormat);
		}
		return null;
	}

	public static String convertFromZoneToZoneString(Date fromDate, Object fromDateObject, String fromZone,
			String toZone, String fromFormat, String toFormat) {
		try {
			if (!ServicesUtil.isEmpty(fromDateObject)) {
				DateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
				if (!isEmpty(fromZone)) {
					fromDateFormat.setTimeZone(TimeZone.getTimeZone(fromZone));
				}				fromDate = fromDateFormat.parse(fromDateObject.toString());
			} else if (ServicesUtil.isEmpty(fromDate)) {
				fromDate = new Date();
			}
			// System.err.println("[Murphy][ServicesUtil][convertFromZoneToZoneString]\n
			// fromDate :"+fromDate + "fromDateObject :"+fromDateObject
			// +"fromZone :"+fromZone +" toZone : "+toZone+ "fromFormat :
			// "+fromFormat + " toFormat :" +toFormat);
			DateFormat toDateFormat = new SimpleDateFormat(toFormat);
			if (!ServicesUtil.isEmpty(toZone)) {
				toDateFormat.setTimeZone(TimeZone.getTimeZone(toZone));
			}
			return toDateFormat.format(fromDate.getTime());

		} catch (Exception e) {
			System.err.println("[Murphy][ServicesUtil][convertFromZoneToZoneString][error] : " + e.getMessage());
		}
		return null;
	}

	public static String getStringFromList(List<String> stringList) {
		String returnString = "";
		for (String st : stringList) {
			returnString = returnString + "'" + st.trim() + "',";
		}
		return returnString.substring(0, returnString.length() - 1);
	}

	public static String getStringFromList(String[] stringList) {
		String returnString = "";
		for (String st : stringList) {
			returnString = returnString + "'" + st.trim() + "',";
		}
		return returnString.substring(0, returnString.length() - 1);
	}
	
	public static String getStringForInQuery(String inputString) {
		String returnString = "";
		if (!ServicesUtil.isEmpty(inputString)) {
			if (inputString.contains(",")) {
				returnString = getStringFromList(inputString.split(","));
			} else {
				returnString = "'" + inputString.trim() + "'";
			}
		}
		return returnString;
	}
	
	 public static String getLikeQueryForGasBlowBy(String meterName)
	    {
	        String input[]=meterName.split(",");
	        String returnString="";
	        int c=0;
	        for(String s:input)
	        {
	            c++;
	            if(c<=input.length-1)
	            returnString=returnString+"metername not like '%"+s+"%' and ";
	            else
	            returnString=returnString+"metername not like '%"+s+"%'";
	        }
	        
	        return returnString.substring(0,returnString.length());
	    }
	    

	// DateTime Format
	public static String parseDateTimeFormatString(Date fromDate, Object fromDateObject, String fromZone, String toZone,
			String fromFormat, String type) {
		DateFormat toDateFormat = null;
		try {
			if (!ServicesUtil.isEmpty(fromDateObject)) {
				DateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
				fromDateFormat.setTimeZone(TimeZone.getTimeZone(fromZone));
				fromDate = fromDateFormat.parse(fromDateObject.toString());
			} else if (ServicesUtil.isEmpty(fromDate)) {
				fromDate = new Date();
			}
			if (type.equalsIgnoreCase(ProcountConstant.APP_DISPLAY)) {
				toDateFormat = new SimpleDateFormat(ProcountConstant.DISPLAY_DATETIME_FORMAT);
			} else if (type.equalsIgnoreCase(ProcountConstant.Extract)) {
				toDateFormat = new SimpleDateFormat(ProcountConstant.EXTRACT_DATE_TIME_FORMAT);
			}
			if (!ServicesUtil.isEmpty(toZone)) {
				toDateFormat.setTimeZone(TimeZone.getTimeZone(toZone));
			}
			return toDateFormat.format(fromDate.getTime());

		} catch (Exception e) {
			System.err.println("[Murphy][ServicesUtil][parseDateFormatString][error] : " + e.getMessage());
		}
		return null;
	}

	// Date Format
	public static String parseDateFormatString(Date fromDate, Object fromDateObject, String fromZone, String toZone,
			String fromFormat, String type) {
		DateFormat toDateFormat = null;
		try {
			if (!ServicesUtil.isEmpty(fromDateObject)) {
				DateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
				fromDateFormat.setTimeZone(TimeZone.getTimeZone(fromZone));
				fromDate = fromDateFormat.parse(fromDateObject.toString());
			} else if (ServicesUtil.isEmpty(fromDate)) {
				fromDate = new Date();
			}
			if (type.equalsIgnoreCase(ProcountConstant.APP_DISPLAY)) {
				toDateFormat = new SimpleDateFormat(ProcountConstant.DISPLAY_DATE_FORMAT);
			} else if (type.equalsIgnoreCase(ProcountConstant.Extract)) {
				toDateFormat = new SimpleDateFormat(ProcountConstant.EXTRACT_DATE_FORMAT);
			}
			if (!ServicesUtil.isEmpty(toZone)) {
				toDateFormat.setTimeZone(TimeZone.getTimeZone(toZone));
			}
			return toDateFormat.format(fromDate.getTime());

		} catch (Exception e) {
			System.err.println("[Murphy][ServicesUtil][parseDateFormatString][error] : " + e.getMessage());
		}
		return null;
	}
	
	// for Epoch Conversion
	public static String convertToEpoch(Object fromDateObject, String fromFormat) {
		try {

			DateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
			// fromDateFormat.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
			String epochTime = String.valueOf(fromDateFormat.parse(fromDateObject.toString()).getTime());

			return epochTime;
		} catch (Exception e) {
			System.err.println("[Murphy][ServicesUtil][convertToEpoch][error] : " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
	
	public static String getCountryCode(String muwi, String locationCode) {
		String countryCode = null;
		try {
			if (!ServicesUtil.isEmpty(muwi)) {
				if (muwi.substring(0, 3).equalsIgnoreCase(EFS_BASE_MUWI))
					countryCode = EFS_CODE;
				else
					countryCode = CA_CODE;
			} else if (!ServicesUtil.isEmpty(locationCode)) {
				if (locationCode.substring(0, 10).equalsIgnoreCase(EFS_BASE_LOC_CODE))
					countryCode = EFS_CODE;
				else
					countryCode = CA_CODE;
			} else {
				throw new Exception("ServicesUtil.getCountryCode()");
			}
		} catch (Exception e) {
			logger.error("[Murphy][ServicesUtil][getCountryCode][error] : " + e.getMessage());
		}
		return countryCode;

	}
	
	
	public static String getCountryCodeByMuwi(String muwi) throws Exception {
		enforceMandatory("muwi", muwi);
		if (muwi.substring(0, 3).equalsIgnoreCase(EFS_BASE_MUWI)) {
			return EFS_CODE;
		} else if (muwi.substring(0, 3).equalsIgnoreCase(CANADA_BASE_MUWI)) {

			return CA_CODE;
		}
		throw new Exception("ServicesUtil.getCountryCodeByMuwi() invalid muwi");
	}

	public static String getCountryCodeByLocation(String locationCode) throws Exception {

		enforceMandatory("locationCode", locationCode);

		if (locationCode.substring(0, 10).equalsIgnoreCase(EFS_BASE_LOC_CODE)) {
			return EFS_CODE;
		} else if (locationCode.substring(0, 6).equalsIgnoreCase(CANADA_BASE_LOC_CODE)) {
			return CA_CODE;
		}

		throw new Exception("ServicesUtil.getCountryCodeByLocation() invalid location code ");
	}

	public static void enforceMandatory(String field, Object value) throws Exception {
		if (ServicesUtil.isEmpty(value)) {
			String message = "Field=" + field + " can't be empty";
			throw new Exception(message, null);
		}
	}
}
