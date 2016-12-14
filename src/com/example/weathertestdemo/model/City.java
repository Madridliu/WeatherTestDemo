package com.example.weathertestdemo.model;

public class City {
	
	private String name;//城市的名字
	private String pinyin;//城市名字的拼音
	
	public City(String name, String pinyin) {
		super();
		this.name = name;
		this.pinyin = pinyin;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

}
