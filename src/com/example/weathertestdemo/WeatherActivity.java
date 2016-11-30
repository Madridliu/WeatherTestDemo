package com.example.weathertestdemo;

import org.json.JSONArray;
import org.json.JSONException;
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
	private TextView realTemp;
	private Button btnList;
	
	private TextView today;
	private TextView infoWeather;
	private TextView min;
	private TextView max;
	
	private TextView day1;
	private TextView infoWeather1;
	private TextView min1;
	private TextView max1;
	
	private TextView day2;
	private TextView infoWeather2;
	private TextView min2;
	private TextView max2;
	
	private TextView day3;
	private TextView infoWeather3;
	private TextView min3;
	private TextView max3;
	
	private TextView day4;
	private TextView infoWeather4;
	private TextView min4;
	private TextView max4;
	
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
					showTodayInfo();
					showDay1Info();
					showDay2Info();
					showDay3Info();
					showDay4Info();
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

	protected void showDay4Info() {
		// TODO Auto-generated method stub
		day4.setText("周" + weather.getDay4());
		infoWeather4.setText(weather.getInfoWeather4());
		min4.setText(weather.getMin4());
		max4.setText(weather.getMax4());
	}

	protected void showDay3Info() {
		// TODO Auto-generated method stub
		day3.setText("周" + weather.getDay3());
		infoWeather3.setText(weather.getInfoWeather3());
		min3.setText(weather.getMin3());
		max3.setText(weather.getMax3());
	}

	protected void showDay2Info() {
		// TODO Auto-generated method stub
		day2.setText("周" + weather.getDay2());
		infoWeather2.setText(weather.getInfoWeather2());
		min2.setText(weather.getMin2());
		max2.setText(weather.getMax2());
	}

	protected void showDay1Info() {
		// TODO Auto-generated method stub
		infoWeather1.setText(weather.getInfoWeather1());
		min1.setText(weather.getMin1());
		max1.setText(weather.getMax1());
	}

	protected void showTodayInfo() {
		// TODO Auto-generated method stub
		cityName.setText(weather.getCityName());
		publishText.setText("今天" + weather.getPublishTime().substring(0, 5) + "发布");
		currentDate.setText(weather.getCurrentDate());
		week.setText("周" + weather.getWeek());
		weatherDesp.setText(weather.getWeatherInfo());
		realTemp.setText(weather.getRealTemp());
		infoWeather.setText(weather.getWeatherInfo());
		min.setText(weather.getMin());
		max.setText(weather.getMax());
	}
	//将返回的json数据按需提取并设置到对应的weather实体类中
	protected String parserShowWeather(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getString("reason").equals("successed!")) {
				JSONObject jsonData = jsonObject.getJSONObject("result").getJSONObject("data");
				JSONArray jsonArray = jsonData.getJSONArray("weather");
				todayWeatherinfo(jsonData, jsonArray);
				day1Weatherinfo(jsonArray);
				day2weatherinfo(jsonArray);
				day3weatherinfo(jsonArray);
				day4weatherinfo(jsonArray);
				return "ok";
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("parserError", "天气信息解析提取失败");
		}
		return "fault";
	}

	
	private void day4weatherinfo(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		try {
			weather.setDay4(jsonArray.getJSONObject(4).getString("week"));
			JSONObject jsonInfo = jsonArray.getJSONObject(4).getJSONObject("info");
			weather.setInfoWeather4(jsonInfo.getJSONArray("day").getString(1));
			weather.setMin4(jsonInfo.getJSONArray("night").getString(2));
			weather.setMax4(jsonInfo.getJSONArray("day").getString(2));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void day3weatherinfo(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		try {
			weather.setDay3(jsonArray.getJSONObject(3).getString("week"));
			JSONObject jsonInfo = jsonArray.getJSONObject(3).getJSONObject("info");
			weather.setInfoWeather3(jsonInfo.getJSONArray("day").getString(1));
			weather.setMin3(jsonInfo.getJSONArray("night").getString(2));
			weather.setMax3(jsonInfo.getJSONArray("day").getString(2));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void day2weatherinfo(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		try {
			weather.setDay2(jsonArray.getJSONObject(2).getString("week"));
			JSONObject jsonInfo = jsonArray.getJSONObject(2).getJSONObject("info");
			weather.setInfoWeather2(jsonInfo.getJSONArray("day").getString(1));
			weather.setMin2(jsonInfo.getJSONArray("night").getString(2));
			weather.setMax2(jsonInfo.getJSONArray("day").getString(2));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void day1Weatherinfo(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		try {
			weather.setDay1(jsonArray.getJSONObject(1).getString("week"));
			JSONObject jsonInfo = jsonArray.getJSONObject(1).getJSONObject("info");
			weather.setInfoWeather1(jsonInfo.getJSONArray("day").getString(1));
			weather.setMin1(jsonInfo.getJSONArray("night").getString(2));
			weather.setMax1(jsonInfo.getJSONArray("day").getString(2));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void todayWeatherinfo(JSONObject jsonData, JSONArray jsonArray) {
		// TODO Auto-generated method stub
		try {
			weather.setCurrentDate(jsonData.getString("pubdate"));
			weather.setPublishTime(jsonData.getJSONObject("realtime").getString("time"));
			weather.setCityName(jsonData.getJSONObject("realtime").getString("city_name"));
			weather.setWeatherInfo(jsonData.getJSONObject("realtime").getJSONObject("weather").getString("info"));
			JSONObject jsonInfo = jsonArray.getJSONObject(0).getJSONObject("info");
			weather.setMin(jsonInfo.getJSONArray("night").getString(2));
			weather.setMax(jsonInfo.getJSONArray("day").getString(2));
			weather.setRealTemp(jsonData.getJSONObject("realtime").getJSONObject("weather").getString("temperature"));
			weather.setWeek(jsonArray.getJSONObject(0).getString("week"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		realTemp = (TextView) findViewById(R.id.realTemp);
		week = (TextView) findViewById(R.id.week);
		btnList = (Button) findViewById(R.id.btnList);
		
		today = (TextView) findViewById(R.id.tv_Today);
		infoWeather = (TextView) findViewById(R.id.tv_Weather);
		min = (TextView) findViewById(R.id.tv_Min); 
		max = (TextView) findViewById(R.id.tv_Max);
		
		day1 = (TextView) findViewById(R.id.tv_Day1);
		infoWeather1 = (TextView) findViewById(R.id.tv_Weather1);
		min1 = (TextView) findViewById(R.id.tv_Min1);
		max1 = (TextView) findViewById(R.id.tv_Max1);
		
		day2 = (TextView) findViewById(R.id.tv_Day2);
		infoWeather2 = (TextView) findViewById(R.id.tv_Weather2);
		min2 = (TextView) findViewById(R.id.tv_Min2);
		max2 = (TextView) findViewById(R.id.tv_Max2);
		
		day3 = (TextView) findViewById(R.id.tv_Day3);
		infoWeather3 = (TextView) findViewById(R.id.tv_Weather3);
		min3 = (TextView) findViewById(R.id.tv_Min3);
		max3 = (TextView) findViewById(R.id.tv_Max3);
		
		day4 = (TextView) findViewById(R.id.tv_Day4);
		infoWeather4 = (TextView) findViewById(R.id.tv_Weather4);
		min4 = (TextView) findViewById(R.id.tv_Min4);
		max4 = (TextView) findViewById(R.id.tv_Max4);
	}

	
}
