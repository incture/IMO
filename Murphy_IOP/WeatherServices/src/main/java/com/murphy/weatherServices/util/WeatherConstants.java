package com.murphy.weatherServices.util;

public interface WeatherConstants {

	// Open Weather API
	String WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/onecall?";
	String API_ID = "f5628ebe027e9ac29d998762ca94d035";
	String WEATHER_UNIT = "metric";
	
	/* Changes based on Production / Dev */
	String SOCKS_PORT = "20004";
	String SOCKS_PORT_NAME = "socksProxyPort";
	String SOCKS_HOST = "localhost";
	String SOCK_HOST_NAME = "socksProxyHost";

}
