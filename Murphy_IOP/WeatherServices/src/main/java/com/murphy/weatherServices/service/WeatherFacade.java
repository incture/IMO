package com.murphy.weatherServices.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.murphy.weatherServices.dto.WeatherDto;
import com.murphy.weatherServices.service.interfaces.WeatherFacadeLocal;
import com.murphy.weatherServices.util.ServicesUtil;
import com.murphy.weatherServices.util.WeatherConstants;

@Service("weatherFacade")
public class WeatherFacade implements WeatherFacadeLocal {

	private static final Logger logger = LoggerFactory.getLogger(WeatherFacade.class);

	@Override
	public List<WeatherDto> getHourlyWeather(Double lat, Double lon) {
		List<WeatherDto> weatherDtos = new ArrayList<WeatherDto>();
		try {
			String response = getWeatherData(lat, lon);
			JSONObject json = new JSONObject(response);
			if (!ServicesUtil.isEmpty(json)) {
				JSONArray hourlyList = (JSONArray) json.get("hourly");
				for (Object o : hourlyList) {
					WeatherDto dto = new WeatherDto();
					dto.setLat(lat);
					dto.setLon(lon);
					dto.setTime(Long.parseLong(((JSONObject) o).get("dt").toString()));
					JSONArray hourly = (JSONArray) ((JSONObject) o).get("weather");
					if (!ServicesUtil.isEmpty(hourly)) {
						dto.setId(Integer.parseInt(((JSONObject) hourly.get(0)).get("id").toString()));
						dto.setIcon(((JSONObject) hourly.get(0)).get("icon").toString());
					}
					weatherDtos.add(dto);
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][WeatherFacade][getHourlyWeather] error : " + e.getMessage());
		}

		return weatherDtos;
	}

	@Override
	public List<WeatherDto> getDailyWeather(Double lat, Double lon) {
		List<WeatherDto> weatherDtos = new ArrayList<WeatherDto>();
		try {
			String response = getWeatherData(lat, lon);
			JSONObject json = new JSONObject(response);
			if (!ServicesUtil.isEmpty(json)) {
				JSONArray dailyList = (JSONArray) json.get("daily");
				for (Object o : dailyList) {
					WeatherDto dto = new WeatherDto();
					dto.setLat(lat);
					dto.setLon(lon);
					dto.setTime(Long.parseLong(((JSONObject) o).get("dt").toString()));
					JSONArray daily = (JSONArray) ((JSONObject) o).get("weather");
					if (!ServicesUtil.isEmpty(daily)) {
						dto.setId(Integer.parseInt(((JSONObject) daily.get(0)).get("id").toString()));
						dto.setIcon(((JSONObject) daily.get(0)).get("icon").toString());
					}
					weatherDtos.add(dto);
				}
			}

		} catch (Exception e) {
			logger.error("[Murphy][WeatherFacade][getDailyWeather] error : " + e.getMessage());
		}

		return weatherDtos;
	}

	@Override
	public WeatherDto getCurrentWeather(Double lat, Double lon) {
		WeatherDto dto = new WeatherDto();
		try {
			String response = getWeatherData(lat, lon);
			JSONObject json = new JSONObject(response);
			if (!ServicesUtil.isEmpty(json)) {
				JSONObject current = (JSONObject) json.get("current");
				dto.setLat(lat);
				dto.setLon(lon);
				dto.setTime(Long.parseLong(current.get("dt").toString()));
				dto.setId(Integer.parseInt(((JSONObject)((JSONArray)current.get("weather")).get(0)).get("id").toString()));
				dto.setIcon(((JSONObject)((JSONArray)current.get("weather")).get(0)).get("icon").toString());
			}

		} catch (Exception e) {
			logger.error("[Murphy][WeatherFacade][getCurrentWeather] error : " + e.getMessage());
		}

		return dto;
	}
	private String getWeatherData(Double lat, Double lon) {
		HttpClient httpClient = HttpClientBuilder.create().build();
		String url = WeatherConstants.WEATHER_API_BASE_URL + "lat=" + lat + "&lon=" + lon + "&appid="
				+ WeatherConstants.API_ID + "&units=" + WeatherConstants.WEATHER_UNIT;
		HttpGet httpGet = new HttpGet(url);
		httpGet.addHeader("content-type", "application/json; charset=UTF-8");

		String response = null;
		try {
			ServicesUtil.unSetupSOCKS();
			HttpResponse httpResponse = httpClient.execute(httpGet);
			response = EntityUtils.toString(httpResponse.getEntity());
		} catch (Exception e) {
			logger.error("[Murphy][WeatherFacade][getWeatherData] error : " + e.getMessage());
		}
		return response;
	}
}
