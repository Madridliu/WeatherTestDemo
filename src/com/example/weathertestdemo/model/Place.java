package com.example.weathertestdemo.model;

public class Place {
	private String Province;
	private String City;
	private String District;

	public Place() {
		
	}
	
	public Place(String province, String city, String district) {
		super();
		this.Province = province;
		this.City = city;
		this.District = district;
	}

	public String getProvince() {
		return Province;
	}

	public void setProvince(String province) {
		this.Province = province;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		this.City = city;
	}

	public String getDistrict() {
		return District;
	}

	public void setDistrict(String district) {
		this.District = district;
	}

}
