package com.murphy.weatherServices.service.interfaces;

import java.util.List;

import com.murphy.weatherServices.dto.WeatherDto;

public interface WeatherFacadeLocal {

	List<WeatherDto> getDailyWeather(Double lat, Double lon);

	List<WeatherDto> getHourlyWeather(Double lat, Double lon);

	WeatherDto getCurrentWeather(Double lat, Double lon);
	
}
