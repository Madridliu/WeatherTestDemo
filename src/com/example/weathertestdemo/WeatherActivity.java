package com.example.weathertestdemo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weathertestdemo.adapter.CityListAdapter;
import com.example.weathertestdemo.adapter.CityListAdapter.CallBack;
import com.example.weathertestdemo.dbhelper.CitySqliteOpenHelper;
import com.example.weathertestdemo.model.Weather;
import com.example.weathertestdemo.util.HttpCallbackListener;
import com.example.weathertestdemo.util.HttpUtil;

public class WeatherActivity extends FragmentActivity {

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
	private String tempCity;
	private Weather weather = new Weather();

	public CitySqliteOpenHelper cityOpenHelper;// 对保存了最近访问城市的数据库操作的帮助类
	public SQLiteDatabase cityDb;// 保存最近访问城市的数据库
	public CityListAdapter cityListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		initView();
		//缓存最后一次点击的城市名字，并在下次启动时显示此城市天气信息
		SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
		String cityNameTemp = pref.getString("name", "");
		if (cityNameTemp == "") {
			tempCity = "北京";
		} else {
			tempCity = cityNameTemp;
		}
		showWeather();
		btnList = (Button) findViewById(R.id.btnList);
		btnList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction transaction = fm.beginTransaction();
				transaction.replace(R.id.weather_layout, new listFragment(
						new CallBack() {

							@Override
							public void getCityName(String cityname) {
								SharedPreferences.Editor editor = getSharedPreferences(
										"data", MODE_PRIVATE).edit();
								editor.putString("name", cityname);
								editor.commit();
								tempCity = cityname;
								showWeather();
							}

						}));
				transaction.addToBackStack(null); // 添加返回栈中
				transaction.commit();
			}
		});

	}

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private Handler handler = new Handler() {
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

		String url = "http://op.juhe.cn/onebox/weather/query?cityname="
				+ tempCity + "&key=e17e3ffde6a485fe3b366b74526fbb88";
		HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = SHOW_RESPONSE;
				message.obj = response;
				handler.sendMessage(message);
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				Log.d("error", "返回数据解析失败");
			}
		});
	}

	private void showDay4Info() {
		// TODO Auto-generated method stub
		day4.setText("周" + weather.getDay4());
		infoWeather4.setText(weather.getInfoWeather4());
		min4.setText(weather.getMin4());
		max4.setText(weather.getMax4());
	}

	private void showDay3Info() {
		// TODO Auto-generated method stub
		day3.setText("周" + weather.getDay3());
		infoWeather3.setText(weather.getInfoWeather3());
		min3.setText(weather.getMin3());
		max3.setText(weather.getMax3());
	}

	private void showDay2Info() {
		// TODO Auto-generated method stub
		day2.setText("周" + weather.getDay2());
		infoWeather2.setText(weather.getInfoWeather2());
		min2.setText(weather.getMin2());
		max2.setText(weather.getMax2());
	}

	private void showDay1Info() {
		// TODO Auto-generated method stub
		infoWeather1.setText(weather.getInfoWeather1());
		min1.setText(weather.getMin1());
		max1.setText(weather.getMax1());
	}

	private void showTodayInfo() {
		// TODO Auto-generated method stub
		cityName.setText(weather.getCityName());
		publishText.setText("今天" + weather.getPublishTime().substring(0, 5)
				+ "发布");
		currentDate.setText(weather.getCurrentDate());
		week.setText("周" + weather.getWeek());
		weatherDesp.setText(weather.getWeatherInfo());
		realTemp.setText(weather.getRealTemp());
		infoWeather.setText(weather.getWeatherInfo());
		min.setText(weather.getMin());
		max.setText(weather.getMax());
	}

	// 将返回的json数据按需提取并设置到对应的weather实体类中
	private String parserShowWeather(String response) {
		// TODO Auto-generated method stub
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getString("reason").equals("successed!")) {
				JSONObject jsonData = jsonObject.getJSONObject("result")
						.getJSONObject("data");
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
			JSONObject jsonInfo = jsonArray.getJSONObject(4).getJSONObject(
					"info");
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
			JSONObject jsonInfo = jsonArray.getJSONObject(3).getJSONObject(
					"info");
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
			JSONObject jsonInfo = jsonArray.getJSONObject(2).getJSONObject(
					"info");
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
			JSONObject jsonInfo = jsonArray.getJSONObject(1).getJSONObject(
					"info");
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
			weather.setPublishTime(jsonData.getJSONObject("realtime")
					.getString("time"));
			weather.setCityName(jsonData.getJSONObject("realtime").getString(
					"city_name"));
			weather.setWeatherInfo(jsonData.getJSONObject("realtime")
					.getJSONObject("weather").getString("info"));
			JSONObject jsonInfo = jsonArray.getJSONObject(0).getJSONObject(
					"info");
			weather.setMin(jsonInfo.getJSONArray("night").getString(2));
			weather.setMax(jsonInfo.getJSONArray("day").getString(2));
			weather.setRealTemp(jsonData.getJSONObject("realtime")
					.getJSONObject("weather").getString("temperature"));
			weather.setWeek(jsonArray.getJSONObject(0).getString("week"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	// 事件分发，转到天气页面使输入法自动隐藏，但仍会出现listfragment中搜索框获取不到焦点的问题（无法编辑）
	/**
	 * 保存MyTouchListener接口的列表
	 */
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MyTouchListener>();

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyTouchListener listener : myTouchListeners) {
			listener.onTouchEvent(ev);
		}
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View view = getCurrentFocus();
			if (isHideInput(view, ev)) {
				HideSoftInput(view.getWindowToken());
			}
			if (isFastDoubleClick()) {
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	// 判定是否需要隐藏
	private boolean isHideInput(View v, MotionEvent ev) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right =

			left + v.getWidth();
			if (ev.getX() > left && ev.getX() < right && ev.getY() > top
					&& ev.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	// 隐藏软键盘
	private void HideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 回调接口
	 * 
	 */
	public interface MyTouchListener {
		public void onTouchEvent(MotionEvent event);
	}

	private static long lastClickTime = 0;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		lastClickTime = time;
		return timeD <= 250;
	}
}
