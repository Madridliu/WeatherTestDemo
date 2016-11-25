package com.example.weathertestdemo.util;

public interface HttpCallbackListener {
	void onFinish(String response);
	
	void onError(Exception e);
}
