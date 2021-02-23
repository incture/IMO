package com.murphy.taskmgmt.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.murphy.taskmgmt.exception.InvalidInputFault;

/**
 * Contains utility functions to be used by Services
 * 
 * @version R1
 */
public class ServicesUtil {

	private static final Logger logger = LoggerFactory.getLogger(ServicesUtil.class);

	public static final String NOT_APPLICABLE = "N/A";
	public static final String SPECIAL_CHAR = "∅";
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

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

	public static String getStringFromList(List<String> stringList) {
		String returnString = "";
		for (String st : stringList) {
			returnString = returnString + "'" + st.trim() + "',";
		}
		return returnString.substring(0, returnString.length() - 1);
	}

	public static String getStringFromListForAls(List<String> stringList) {
		String returnString = "";
		if (!isEmpty(stringList)) {
			for (String st : stringList) {
				returnString = returnString + "" + st.trim() + ",";
			}
			returnString = returnString.substring(0, returnString.length() - 1);
		}
		return returnString;
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

	public static String getCSV(Object... objs) {
		if (!isEmpty(objs)) {
			if (objs[0] instanceof Collection<?>) {
				return getCSVArr(((Collection<?>) objs[0]).toArray());
			} else {
				return getCSVArr(objs);
			}

		} else {
			return "";
		}
	}

	private static String getCSVArr(Object[] objs) {
		if (!isEmpty(objs)) {
			StringBuffer sb = new StringBuffer();
			for (Object obj : objs) {
				sb.append(',');
				if (obj instanceof Field) {
					sb.append(extractFieldName((Field) obj));
				} else {
					sb.append(extractStr(obj));
				}
			}
			sb.deleteCharAt(0);
			return sb.toString();
		} else {
			return "";
		}
	}

	public static String buildNoRecordMessage(String queryName, Object... parameters) {
		StringBuffer sb = new StringBuffer("No Record found for query: ");
		sb.append(queryName);
		if (!isEmpty(parameters)) {
			sb.append(" for params:");
			sb.append(getCSV(parameters));
		}
		return sb.toString();
	}

	public static String extractStr(Object o) {
		return o == null ? "" : o.toString();
	}

	public static String extractFieldName(Field o) {
		return o == null ? "" : o.getName();
	}

	public static String appendLeadingCharsToInt(int input, char c, int finalSize) throws InvalidInputFault {
		if (finalSize > 0 && !isEmpty(input)) {
			return String.format("%" + c + finalSize + "d", input);
		}
		return String.valueOf(input);
	}

	public static String appendTrailingCharsToStr(String input, char c, int count) throws InvalidInputFault {

		String st = String.format("%0$-" + count + "s", input).replace(" ", MurphyConstant.CODE_SUCCESS);
		return st;
	}

	public static void enforceMandatory(String field, Object value) throws InvalidInputFault {
		if (ServicesUtil.isEmpty(value)) {
			String message = "Field=" + field + " can't be empty";
			throw new InvalidInputFault(message, null);
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/*
	 * public static Date setInitialTime(Date currentDate) { Calendar calendar =
	 * Calendar.getInstance(); calendar.setTime(currentDate);
	 * calendar.set(Calendar.HOUR_OF_DAY, 0); calendar.set(Calendar.MINUTE, 0);
	 * calendar.set(Calendar.SECOND, 0); calendar.set(Calendar.MILLISECOND, 0);
	 * return calendar.getTime(); }
	 * 
	 * public static Date setEndTime(Date currentDate) { Calendar calendar =
	 * Calendar.getInstance(); calendar.setTime(currentDate);
	 * calendar.set(Calendar.HOUR_OF_DAY, 23); calendar.set(Calendar.MINUTE,
	 * 59); calendar.set(Calendar.SECOND, 59);
	 * calendar.set(Calendar.MILLISECOND, 999); return calendar.getTime(); }
	 */

	public static Date getDate(int i) throws ParseException {
		int x = -i;
		SimpleDateFormat format = new SimpleDateFormat(MurphyConstant.Murphy_DATE_FORMATE);
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, x);
		Date tempdate = cal.getTime();
		String formattedDate = format.format(tempdate);
		Date date = format.parse(formattedDate);
		return date;
	}

	/*
	 * public static List<Date> getMonthIntervalDates() throws ParseException {
	 * SimpleDateFormat sdf = new
	 * SimpleDateFormat(MurphyConstant.Murphy_DATE_FORMATE); List<Date>
	 * dateInterval = new ArrayList<Date>(); int count =
	 * MurphyConstant.MONTH_RANGE; while (count >= 0) { Calendar calendar =
	 * Calendar.getInstance(); Date date = null; if (count == 0) {
	 * calendar.add(Calendar.DAY_OF_MONTH, -(count)); date = calendar.getTime();
	 * } else { calendar.add(Calendar.DAY_OF_MONTH, -(count - 1)); date =
	 * sdf.parse(sdf.format(calendar.getTime())); } count = count -
	 * MurphyConstant.MONTH_INTERVAL; dateInterval.add(date); } return
	 * dateInterval; }
	 */

	public static String getDateDifferenceInHours(Date date) {
		long t1 = new Date().getTime();
		long t2 = date.getTime();
		long diffinHrs = (t1 - t2) / (60 * 60 * 1000) % 60;
		return String.valueOf(diffinHrs);

	}

	public static Calendar getSLADueDate(Calendar created, String slaString) {
		String[] sla = ((String) slaString).split("\\s+");
		System.err.println("sla[0]" + sla[0] + "sla[1]" + sla[1]);
		int slaCount = Integer.parseInt(sla[0]);
		if (MurphyConstant.DAYS.equalsIgnoreCase(sla[1])) {
			created.add(Calendar.DATE, slaCount);
		} else if (MurphyConstant.HOURS.equalsIgnoreCase(sla[1])) {
			created.add(Calendar.HOUR, slaCount);
		} else if (MurphyConstant.MINUTES.equalsIgnoreCase(sla[1])) {
			created.add(Calendar.MINUTE, slaCount);
		}
		return created;
	}

	public static Calendar getNotifyByDate(Calendar created, String threshold) {
		String[] sla = ((String) threshold).split("\\s+");
		int thresCount = Integer.parseInt(sla[0]);
		if (MurphyConstant.DAYS.equalsIgnoreCase(sla[1])) {
			created.add(Calendar.DATE, -thresCount);
		} else if (MurphyConstant.HOURS.equalsIgnoreCase(sla[1])) {
			created.add(Calendar.HOUR, -thresCount);
		} else if (MurphyConstant.MINUTES.equalsIgnoreCase(sla[1])) {
			created.add(Calendar.MINUTE, -thresCount);
		}
		return created;
	}

	/*
	 * public static Date getEarlierDate(int noOfDays) { Calendar calendar =
	 * Calendar.getInstance(); System.out.println(calendar.getTime());
	 * calendar.add(Calendar.DAY_OF_MONTH, -noOfDays);
	 * calendar.set(Calendar.HOUR_OF_DAY, 0); calendar.set(Calendar.MINUTE, 0);
	 * calendar.set(Calendar.SECOND, 0); calendar.set(Calendar.MILLISECOND, 0);
	 * return calendar.getTime(); }
	 */

	public static String getBasicAuth(String userName, String password) {
		String userpass = userName + ":" + password;
		return "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
	}

	public static String convertInputStreamToString(InputStream inputStream) {
		try {

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int nRead;
			byte[] data = new byte[10000];
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();
			byte[] byteArray = buffer.toByteArray();
			return new String(byteArray);

		} catch (Exception e) {
			logger.error("[Murphy][ServicesUtil][convertInputStreamToString][error]" + e.getMessage());
		}
		return null;
	}

	/* Setting and Unsetting SOCK Properties for Downtime */

	/*
	 * public static void unSetupSOCKS() { try {
	 * logger.error("before"+System.getProperty("socksProxyHost"));
	 * logger.error("before"+System.getProperty("socksProxyPort"));
	 * logger.error("before"+System.getProperty("java.net.socks.username"));
	 * System.setProperty("socksProxyHost", "");
	 * System.setProperty("socksProxyPort", "");
	 * System.setProperty("java.net.socks.username", "");
	 * logger.error("After"+System.getProperty("socksProxyHost"));
	 * logger.error("After"+System.getProperty("socksProxyPort"));
	 * logger.error("After"+System.getProperty("java.net.socks.username")); }
	 * catch (Exception e) {
	 * logger.error("Proxy Unsetup Exception : "+e.getMessage()); } }
	 */

	// public static String getPreviousDateAtZoneInString(String format ,String
	// zone ,int count ,String value){
	// DateFormat df = new SimpleDateFormat(format);
	// df.setTimeZone(TimeZone.getTimeZone("CST"));
	// Calendar calendar = Calendar.getInstance();
	// int minute = calendar.get(Calendar.MINUTE);
	//// minute -= minute%delayRoundValue;
	// calendar.set(Calendar.MINUTE, minute);
	//
	// return df.format(new Date(getNotifyByDate(calendar, count +"
	// "+value).getTimeInMillis()));
	// }

	public static Date resultAsDateDowntime(Object o) {
		// logger.error("[Murphy][WorkBoxFacade][resultAsDate][o]" + o);
		String template = "";
		if (o instanceof Object[]) {
			template = Arrays.asList((Object[]) o).toString();
		} else {
			template = String.valueOf(o);
		}
		Date date = null;
		try {
			if (!isEmpty(template)) {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// logger.error("[Murphy][WorkBoxFacade][resultAsDate][o][template]"
				// + template + "[date]");
				date = formatter.parse(template);
			}
			// logger.error("[Murphy][WorkBoxFacade][resultAsDate][o]" + o +
			// "[template]" + template + "[date]" + date
			// + "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			logger.error("resultAsDateDowntime ParseException" + e.getMessage());
		}
		return date;
	}

	public static String resultDateAsStringDowntime(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat(MurphyConstant.DATE_DISPLAY_FORMAT);
		return sf.format(date);
	}

	public static String resultDateAsStringDowntimeCygnate(Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}

	public static Date getNextDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return date = c.getTime();
	}

	public static Date getPrevDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, -1);
		return date = c.getTime();
	}

	public static String getDurationBreakdown(Long millis) {
		if (millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		// Long days = TimeUnit.MILLISECONDS.toDays(millis);
		// millis -= TimeUnit.DAYS.toMillis(days);
		Long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		Long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		// Long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		return (hours + "hr " + minutes + "min");
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
				}
				fromDate = fromDateFormat.parse(fromDateObject.toString());
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

	public static String convertFromZoneToZoneString1(Date fromDate, Object fromDateObject, String fromZone,
			String toZone, String fromFormat, String toFormat, int roundValue) {
		try {
			if (!ServicesUtil.isEmpty(fromDateObject)) {
				DateFormat fromDateFormat = new SimpleDateFormat(fromFormat);
				fromDateFormat.setTimeZone(TimeZone.getTimeZone(fromZone));
				fromDate = fromDateFormat.parse(fromDateObject.toString());
			} else if (ServicesUtil.isEmpty(fromDate)) {
				Calendar calendar = Calendar.getInstance();
				int minute = calendar.get(Calendar.MINUTE);
				// roundValue is 15 for alarm trends, 60 for als trends
				minute -= minute % roundValue;
				calendar.set(Calendar.MINUTE, minute);
				fromDate = calendar.getTime();
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

	public static Object getProperty(String propertyName, String propertyFile) throws ConfigurationException {
		PropertiesConfiguration configuration = new PropertiesConfiguration(propertyFile);
		return configuration.getProperty(propertyName);
	}

	public static void setProperty(String propertyName, Object propertyValue, String propertyFile)
			throws ConfigurationException {
		PropertiesConfiguration configuration = new PropertiesConfiguration(propertyFile);
		configuration.setProperty(propertyName, propertyValue);
		configuration.save();
	}

	public static Map<String, Object> getAllProperties(String propertyFile) throws ConfigurationException {
		Map<String, Object> properties = null;
		String configKey = null;
		PropertiesConfiguration configuration = new PropertiesConfiguration(propertyFile);
		Iterator<String> configKeys = configuration.getKeys();
		if (!ServicesUtil.isEmpty(configKeys)) {
			properties = new HashMap<>();
			while (configKeys.hasNext()) {
				configKey = configKeys.next();
				properties.put(configKey, ServicesUtil.getProperty(configKey, propertyFile));
			}
		}
		return properties;
	}

	public static String getServerUrl(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		String url = request.getRequestURL().toString();
		url = url.substring(0, url.indexOf(contextPath));
		url += contextPath;
		return url;
	}

	public static BigDecimal getBigDecimal(Object value) {
		BigDecimal ret = null;
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass()
						+ " into a BigDecimal.");
			}
		}
		return ret;
	}

	public static String checkIfTimeExistsInInterval(Date initialTime, Date currentTime, Date finalTime) {
		String response = "";
		// System.err.println("initialTime"+initialTime+"currentTime"+currentTime+"finalTime"+finalTime);
		if (!ServicesUtil.isEmpty(initialTime) && !ServicesUtil.isEmpty(currentTime)
				&& !ServicesUtil.isEmpty(finalTime)) {
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(initialTime);
			Calendar calendar3 = Calendar.getInstance();
			calendar3.setTime(currentTime);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(finalTime);

			if (finalTime.compareTo(initialTime) < 0) {
				calendar2.add(Calendar.DATE, 1);
				calendar3.add(Calendar.DATE, 1);
			}

			Date actualTime = calendar3.getTime();
			if ((actualTime.after(calendar1.getTime()) || actualTime.compareTo(calendar1.getTime()) == 0)
					&& actualTime.before(calendar2.getTime())) {
				response = MurphyConstant.CURRENT;
			} else if (actualTime.after(calendar1.getTime()) && actualTime.after(calendar2.getTime())) {
				response = MurphyConstant.BEFORE;
			} else if (actualTime.before(calendar1.getTime()) && actualTime.before(calendar2.getTime())) {
				response = MurphyConstant.AFTER;
			}
		}
		return response;
	}

	public static float getDiffBtwnTwoDates(Date fromDate, Date toDate, String returnIn) {
		int response = 0;
		if (!ServicesUtil.isEmpty(toDate)) {
			if (ServicesUtil.isEmpty(fromDate)) {
				fromDate = new Date();
			}
			float divideBy = 1;
			if (MurphyConstant.DAYS.equals(returnIn)) {
				divideBy = 86400000;
			} else if (MurphyConstant.HOURS.equals(returnIn)) {
				divideBy = 3600000;
			} else if (MurphyConstant.MINUTES.equals(returnIn)) {
				divideBy = 60000;
			} else if (MurphyConstant.SECONDS.equals(returnIn)) {
				divideBy = 1000;
			}
			response = (int) ((fromDate.getTime() - toDate.getTime()) / divideBy);
		}
		return response;
	}

	public static Date getDateWithInterval(Date date, int intervalValue, String intervalType) {
		Calendar cal = Calendar.getInstance();
		if (!ServicesUtil.isEmpty(date)) {
			cal.setTime(date);
		}

		if (MurphyConstant.DAYS.equalsIgnoreCase(intervalType)) {
			cal.add(Calendar.DATE, intervalValue);
		} else if (MurphyConstant.HOURS.equalsIgnoreCase(intervalType)) {
			cal.add(Calendar.HOUR, intervalValue);
		} else if (MurphyConstant.MINUTES.equalsIgnoreCase(intervalType)) {
			cal.add(Calendar.MINUTE, intervalValue);
		} else if (MurphyConstant.SECONDS.equalsIgnoreCase(intervalType)) {
			cal.add(Calendar.SECOND, intervalValue);
		}
		// System.err.println("[ServicesUtil][getDateWithInterval]"+intervalValue
		// + "[intervalType]"+intervalType +"[date]"+ date+" cal.getTime()"+
		// cal.getTime());

		return cal.getTime();
	}

	public static Date getDateWithIntervalForCalendar(Calendar cal, int intervalValue, String intervalType) {

		if (ServicesUtil.isEmpty(cal)) {
			cal = Calendar.getInstance();
		}

		if (MurphyConstant.DAYS.equalsIgnoreCase(intervalType)) {
			cal.add(Calendar.DATE, intervalValue);
		} else if (MurphyConstant.HOURS.equalsIgnoreCase(intervalType)) {
			cal.add(Calendar.HOUR, intervalValue);
		} else if (MurphyConstant.MINUTES.equalsIgnoreCase(intervalType)) {
			cal.add(Calendar.MINUTE, intervalValue);
		} else if (MurphyConstant.SECONDS.equalsIgnoreCase(intervalType)) {
			cal.add(Calendar.SECOND, intervalValue);
		}
		// System.err.println("[ServicesUtil][getDateWithInterval]"+intervalValue
		// + "[intervalType]"+intervalType +"[date]"+ date+" cal.getTime()"+
		// cal.getTime());

		return cal.getTime();
	}

	/* Not Reuseable Specific to task Scheduling */
	public static float fetchAvailableTime(int shiftEndTime, int timeZoneOffSet) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, timeZoneOffSet);
		Date current = cal.getTime();
		int currentHr = cal.get(Calendar.HOUR_OF_DAY);
		if (((shiftEndTime == 7) && (currentHr >= 7 && currentHr <= 18))
				|| ((shiftEndTime == 19) && (currentHr >= 19 && currentHr <= 6))) {
			return 0;
		}

		if (shiftEndTime == 7 && (currentHr >= 19 && currentHr < 0)) {
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		cal.set(Calendar.HOUR_OF_DAY, shiftEndTime);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// System.err.println("[ServiceUtil][fetchAvailableTime]cal"
		// +cal.getTime() +"new Date()"+ new Date()
		// +"timeZoneOffSet"+timeZoneOffSet);
		return getDiffBtwnTwoDates(cal.getTime(), current, MurphyConstant.MINUTES);
	}

	public static float changeTimeUnits(int insertInterval, String insertIntervalType, String convertIntoType) {
		float returnValue = 0;

		if (MurphyConstant.DAYS.equalsIgnoreCase(insertIntervalType)) {
			returnValue = insertInterval * 86400000;
		} else if (MurphyConstant.HOURS.equalsIgnoreCase(insertIntervalType)) {
			returnValue = insertInterval * 3600000;
		} else if (MurphyConstant.MINUTES.equalsIgnoreCase(insertIntervalType)) {
			returnValue = insertInterval * 60000;
		} else if (MurphyConstant.SECONDS.equals(insertIntervalType)) {
			returnValue = insertInterval * 1000;
		} else if (MurphyConstant.MILLISEC.equals(insertIntervalType)) {
			returnValue = insertInterval;
		}

		if (returnValue != 0) {
			if (MurphyConstant.DAYS.equalsIgnoreCase(convertIntoType)) {
				returnValue = returnValue / 86400000;
			} else if (MurphyConstant.HOURS.equalsIgnoreCase(convertIntoType)) {
				returnValue = returnValue / 3600000;
			} else if (MurphyConstant.MINUTES.equalsIgnoreCase(convertIntoType)) {
				returnValue = returnValue / 60000;
			} else if (MurphyConstant.SECONDS.equals(convertIntoType)) {
				returnValue = returnValue / 1000;
			}
		}
		return returnValue;
	}

	public static Date roundDateToNearInterval(Date date, int interval, String intervalType) {

		Calendar calendar = Calendar.getInstance();
		int value = 0;
		if (MurphyConstant.DAYS.equals(intervalType)) {
			value = calendar.get(Calendar.DATE);
			value -= value % interval;
			calendar.set(Calendar.DATE, value);
		} else if (MurphyConstant.HOURS.equals(intervalType)) {
			value = calendar.get(Calendar.HOUR);
			value -= value % interval;
			calendar.set(Calendar.HOUR, value);
		} else if (MurphyConstant.MINUTES.equals(intervalType)) {
			value = calendar.get(Calendar.MINUTE);
			value -= value % interval;
			calendar.set(Calendar.MINUTE, value);
		} else if (MurphyConstant.SECONDS.equals(intervalType)) {
			value = calendar.get(Calendar.SECOND);
			value -= value % interval;
			calendar.set(Calendar.SECOND, value);
		}
		return calendar.getTime();

	}

	public static int getDayOfTheDate(Date date, String zone) {

		Calendar c = Calendar.getInstance();
		if (ServicesUtil.isEmpty(date)) {
			date = ServicesUtil.convertFromZoneToZone(null, null, MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, "",
					"");
		}
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	public static int getHourOfTheDate(Date date, String zone) {

		Calendar c = Calendar.getInstance();
		if (ServicesUtil.isEmpty(date)) {
			date = ServicesUtil.convertFromZoneToZone(null, null, MurphyConstant.UTC_ZONE, MurphyConstant.CST_ZONE, "",
					"");
		}
		c.setTime(date);
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public static String getCstTime() {
		// SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss
		// a");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		format1.setTimeZone(TimeZone.getTimeZone("CST"));
		String dateStr = format1.format(new Date());
		return dateStr;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static List<List<String>> divideIn2EqualSums(Double[] arr) {
		Double[] set1 = new Double[(1 + arr.length) / 2];
		Double[] set2 = new Double[(1 + arr.length) / 2];
		List<String> elt1 = new ArrayList<String>();
		List<String> elt2 = new ArrayList<String>();
		List<List<String>> response = new ArrayList<List<String>>();
		int setSize = set1.length;
		Arrays.sort(arr);

		int pos1 = 0;
		int pos2 = 0;

		int i = arr.length - 1;

		Double sum1 = 0.0;
		Double sum2 = 0.0;
		while (pos1 < setSize && pos2 < setSize) {
			if (sum1 < sum2) {
				set1[pos1] = arr[i];
				elt1.add("" + i);
				pos1++;
				sum1 += arr[i];
			} else {
				set2[pos2] = arr[i];
				elt2.add("" + i);
				pos2++;
				sum2 += arr[i];
			}
			i--;
		}

		while (i >= 0) {
			if (pos1 < setSize) {
				set1[pos1] = arr[i];
				elt1.add("" + i);

				pos1++;
			} else {
				set2[pos2] = arr[i];
				elt2.add("" + i);
				pos2++;
			}
			i--;
		}

		response.add(elt1);
		response.add(elt2);
		return response;
	}

	// public static List<Integer> getShortestRoute(int adjacencyMatrix[][]) {
	// List<Integer> shortestRoute = new ArrayList<>();
	// Stack<Integer> stack = new Stack<Integer>();
	// int numberOfNodes = adjacencyMatrix[0].length;
	// int[] visited = new int[numberOfNodes];
	// visited[0] = 1;
	// stack.push(0);
	// int element, dst = 0, i;
	// int min = Integer.MAX_VALUE;
	// boolean minFlag = false;
	//// System.out.print(0 + "\t");
	// shortestRoute.add(0);
	//
	// while (!stack.isEmpty()) {
	// element = stack.peek();
	// i = 0;
	// min = Integer.MAX_VALUE;
	// while (i < numberOfNodes) {
	// if (adjacencyMatrix[element][i] > 1 && visited[i] == 0) {
	// if (min > adjacencyMatrix[element][i]) {
	// min = adjacencyMatrix[element][i];
	// dst = i;
	// minFlag = true;
	// }
	// }
	// i++;
	// }
	// if (minFlag) {
	// visited[dst] = 1;
	// stack.push(dst);
	//// System.out.print(dst + "\t");
	// shortestRoute.add(dst);
	// minFlag = false;
	// continue;
	// }
	// stack.pop();
	// }
	// return shortestRoute;
	// }
	public static List<Integer> getShortestRoute(Double adjacencyMatrix[][]) {
		List<Integer> shortestRoute = new ArrayList<>();
		Stack<Integer> stack = new Stack<Integer>();
		int numberOfNodes = adjacencyMatrix[0].length;
		int[] visited = new int[numberOfNodes];
		visited[0] = 1;
		stack.push(0);
		int element, dst = 0, i;
		Double min = Double.MAX_VALUE;
		boolean minFlag = false;
		// System.out.print(0 + "\t");
		shortestRoute.add(0);

		while (!stack.isEmpty()) {
			element = stack.peek();
			i = 0;
			min = Double.MAX_VALUE;
			while (i < numberOfNodes) {
				if (adjacencyMatrix[element][i] > 1 && visited[i] == 0) {
					if (min > adjacencyMatrix[element][i]) {
						min = adjacencyMatrix[element][i];
						dst = i;
						minFlag = true;
					}
				}
				i++;
			}
			if (minFlag) {
				visited[dst] = 1;
				stack.push(dst);
				// System.out.print(dst + "\t");
				shortestRoute.add(dst);
				minFlag = false;
				continue;
			}
			stack.pop();
		}
		return shortestRoute;
	}

	public static BigDecimal round(BigDecimal value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		value = value.setScale(places, RoundingMode.HALF_UP);
		return value;
	}

	// Datetime format
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
			if (type.equalsIgnoreCase(MurphyConstant.APP_DISPLAY)) {
				toDateFormat = new SimpleDateFormat(MurphyConstant.DISPLAY_DATETIME_FORMAT);
			} else if (type.equalsIgnoreCase(MurphyConstant.EXTRACT)) {
				toDateFormat = new SimpleDateFormat(MurphyConstant.EXTRACT_DATE_TIME_FORMAT);
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
			if (type.equalsIgnoreCase(MurphyConstant.APP_DISPLAY)) {
				toDateFormat = new SimpleDateFormat(MurphyConstant.DISPLAY_DATE_FORMAT);
			} else if (type.equalsIgnoreCase(MurphyConstant.EXTRACT)) {
				toDateFormat = new SimpleDateFormat(MurphyConstant.EXTRACT_DATE_FORMAT);
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
	
	
	public static String getDateString(Date date) {
		if (!ServicesUtil.isEmpty(date)) {
			DateFormat dateFormat = new SimpleDateFormat(MurphyConstant.DATE_STANDARD);
			return dateFormat.format(date);

		}
		return null;
	}
	
	
	
	
	public static String getLocationByLocationCode(String locationCode) throws Exception {
		enforceMandatory("locationCode", locationCode);

		if (locationCode.substring(0, 10).equalsIgnoreCase(MurphyConstant.EFS_BASE_LOC_CODE)) {
			return MurphyConstant.EFS_BASE_LOC_CODE;
		} else if (locationCode.substring(0, 10).equalsIgnoreCase(MurphyConstant.KAY_BASE_LOC_CODE)) {
			return MurphyConstant.KAY_BASE_LOC_CODE;
		} else if (locationCode.substring(0, 10).equalsIgnoreCase(MurphyConstant.MTM_BASE_LOC_CODE)) {
			return MurphyConstant.MTM_BASE_LOC_CODE;
		} else if (locationCode.substring(0, 10).equalsIgnoreCase(MurphyConstant.MTW_BASE_LOC_CODE)
				|| locationCode.substring(0, 10).equalsIgnoreCase(MurphyConstant.TWP_LOC_CODE)) {
			return MurphyConstant.MTW_BASE_LOC_CODE;
		}

		throw new Exception("ServicesUtil.getCountryCodeByLocation() invalid location code ");

	}

	public static String getCountryCode(String muwi, String locationCode) {
		String countryCode = null;
		try {
			if (!ServicesUtil.isEmpty(muwi)) {
				if (muwi.substring(0, 3).equalsIgnoreCase(MurphyConstant.EFS_BASE_MUWI))
					countryCode = MurphyConstant.EFS_CODE;
				else
					countryCode = MurphyConstant.CA_CODE;
			} else if (!ServicesUtil.isEmpty(locationCode)) {
				if (locationCode.substring(0, 10).equalsIgnoreCase(MurphyConstant.EFS_BASE_LOC_CODE))
					countryCode = MurphyConstant.EFS_CODE;
				else
					countryCode = MurphyConstant.CA_CODE;
			} else {
				return MurphyConstant.MAND_MISS;
			}
		} catch (Exception e) {
			logger.error("[Murphy][ServicesUtil][getCountryCode][error] : " + e.getMessage());
		}
		return countryCode;

	}
	public static String getCountryCodeByMuwi(String muwi) throws Exception {
		enforceMandatory("muwi", muwi);
		if (muwi.substring(0, 3).equalsIgnoreCase(MurphyConstant.EFS_BASE_MUWI)) {
			return MurphyConstant.EFS_CODE;
		} else if (muwi.substring(0, 3).equalsIgnoreCase(MurphyConstant.CANADA_BASE_MUWI)) {

			return MurphyConstant.CA_CODE;
		}
		throw new Exception("ServicesUtil.getCountryCodeByMuwi() invalid muwi");
	}

	public static String getCountryCodeByLocation(String locationCode) throws Exception {

		enforceMandatory("locationCode", locationCode);

		if (locationCode.substring(0, 10).equalsIgnoreCase(MurphyConstant.EFS_BASE_LOC_CODE)) {
			return MurphyConstant.EFS_CODE;
		} else if (locationCode.substring(0, 6).equalsIgnoreCase(MurphyConstant.CANADA_BASE_LOC_CODE)) {
			return MurphyConstant.CA_CODE;
		}

		throw new Exception("ServicesUtil.getCountryCodeByLocation() invalid location code ");
	}


	public static java.sql.Date getDateFromString(String epochTime)
	{   java.sql.Date date =null;
		
		try {
//			Date utilDate = new SimpleDateFormat("yyyy-mm-dd").parse(dateValue);
//			System.err.println("outgoing date : "+utilDate.getTime());
//			return new java.sql.Date(utilDate.getTime());
			String stringdate = "";
			
			if(!ServicesUtil.isEmpty(epochTime))
			{
//			   long unix_seconds = Long.parseLong(epochTime);
//				stringdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(unix_seconds*1000)); 
//				date=java.sql.Date.valueOf(stringdate); 
				long millis= Long.parseLong(epochTime);
				System.err.println("epochTime : "+epochTime);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				sdf.setTimeZone(TimeZone.getTimeZone(MurphyConstant.UTC_ZONE));
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(millis);
				stringdate =sdf.format(cal.getTime());
				date=java.sql.Date.valueOf(stringdate); 
			}
			
			System.err.println("outgoing date "+date);
			return date;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
	
	}
	
	public static Blob getBlobFromString(String value)
	{
		byte[] buff = value.getBytes();
		Blob blob = null;
		try {
			blob = new SerialBlob(buff);
		} catch (SerialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blob;
	}

	public static String getStringFromBlob(Blob note) {
		
		byte[] bdata = null;
		String text = null;
		try {
			bdata = note.getBytes(1, (int) note.length());
			text = new String(bdata);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return text;
	
	}
	
	//List to set
	public static <T> Set<T> convertListToSet(List<T> list) {
		return list.stream().collect(Collectors.toSet());
	}

	//Set to List
	public static <T> List<T> convertSetToList(Set<T> set) {
		return set.stream().collect(Collectors.toList());
	}
	
	
	public static Date getDate(String date) throws ParseException {
		if (!ServicesUtil.isEmpty(date)) {

			return new SimpleDateFormat(MurphyConstant.DATE_STANDARD).parse(date);
		}
		return null;
	}
}
