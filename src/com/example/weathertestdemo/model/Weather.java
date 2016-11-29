package com.example.weathertestdemo.model;

public class Weather {
	private String cityName;
	private String publishTime;
	private String currentDate;
	private String weatherInfo;
	private String tempMin;
	private String tempMax;
	private String week;

	public Weather() {
		
	}
	
	public Weather(String cityName, String publishTime, String currentDate,
			String weatherInfo, String tempMin, String tempMax, String week) {
		super();
		this.cityName = cityName;
		this.publishTime = publishTime;
		this.currentDate = currentDate;
		this.weatherInfo = weatherInfo;
		this.tempMin = tempMin;
		this.tempMax = tempMax;
		this.week = week;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getWeatherInfo() {
		return weatherInfo;
	}

	public void setWeatherInfo(String weatherInfo) {
		this.weatherInfo = weatherInfo;
	}

	public String getTempMin() {
		return tempMin;
	}

	public void setTempMin(String tempMin) {
		this.tempMin = tempMin;
	}

	public String getTempMax() {
		return tempMax;
	}

	public void setTempMax(String tempMax) {
		this.tempMax = tempMax;
	}

}
