package com.example.weathertestdemo;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.weathertestdemo.model.Weather;
import com.example.weathertestdemo.util.HttpCallbackListener;
import com.example.weathertestdemo.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

	private static final int SHOW_RESPONSE = -1;
	
	private TextView cityName;
	private TextView publishText;
	private TextView currentDate;
	private TextView week;
	private TextView weatherDesp;
	private TextView tempMin;
	private TextView tempMax;
	private Button btnList;
	
	private Weather weather = new Weather();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		initView();
		showWeather();
		btnList.setOnClickListener(this);
	}

	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				String responseData = (String) msg.obj;
				parserShowWeather(responseData);
				if (parserShowWeather(responseData).equals("ok")) {
					cityName.setText(weather.getCityName());
					publishText.setText("今天" + weather.getPublishTime().substring(0, 5) + "发布");
					currentDate.setText(weather.getCurrentDate());
					week.setText("星期" + weather.getWeek());
					weatherDesp.setText(weather.getWeatherInfo());
					tempMin.setText(weather.getTempMin());
					tempMax.setText(weather.getTempMax());
				}
				break;

			default:
				break;
			}
		};
	};

	private void showWeather() {
		// TODO Auto-generated method stub
		String placeName = getIntent().getStringExtra("cityName");
		String url = "http://op.juhe.cn/onebox/weather/query?cityname="
				+ placeName + "&key=e17e3ffde6a485fe3b366b74526fbb88";
		HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = SHOW_RESPONSE;
				message.obj = response.toString();
				handler.sendMessage(message);
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				Log.d("error", "返回数据解析失败");
			}
		});
	}

	protected String parserShowWeather(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getString("reason").equals("successed!")) {
				JSONObject jsonData = jsonObject.getJSONObject("result").getJSONObject("data");
				weather.setCurrentDate(jsonData.getString("pubdate"));
				weather.setPublishTime(jsonData.getString("pubtime"));
				weather.setCityName(jsonData.getJSONObject("realtime").getString("city_name"));
				weather.setWeatherInfo(jsonData.getJSONObject("realtime").getJSONObject("weather").getString("info"));
				JSONArray jsonArray = jsonData.getJSONArray("weather");
				JSONObject jsonInfo = jsonArray.getJSONObject(0).getJSONObject("info");
				weather.setTempMax(jsonInfo.getJSONArray("day").getString(2));
				weather.setTempMin(jsonInfo.getJSONArray("night").getString(2));
				weather.setWeek(jsonArray.getJSONObject(0).getString("week"));
				return "ok";
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("parserError", "天气信息解析提取失败");
		}
		return "fault";
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnList:
			Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}
	
	private void initView() {
		// TODO Auto-generated method stub
		cityName = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		currentDate = (TextView) findViewById(R.id.current_date);
		weatherDesp = (TextView) findViewById(R.id.weather_desp);
		tempMin = (TextView) findViewById(R.id.temp1);
		tempMax = (TextView) findViewById(R.id.temp2);
		week = (TextView) findViewById(R.id.week);
		btnList = (Button) findViewById(R.id.btnList);
	}

	
}
