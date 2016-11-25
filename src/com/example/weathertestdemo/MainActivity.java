package com.example.weathertestdemo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.weathertestdemo.adapter.PlaceAdapter;
import com.example.weathertestdemo.adapter.PlaceAdapter1;
import com.example.weathertestdemo.adapter.PlaceAdapter2;
import com.example.weathertestdemo.model.Place;
import com.example.weathertestdemo.util.HttpCallbackListener;
import com.example.weathertestdemo.util.HttpUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int SHOW_RESPONSE = -1;
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	// public static final int LEVEL_CITYTe = 4;
	public static final int LEVEL_DISTRICT = 2;

	private TextView textView;
	private ListView listView;
	private List<Place> data;
	private List<Place> data1;
	private List<Place> data2;
	private PlaceAdapter adapter;
	private PlaceAdapter1 adapter1;
	private PlaceAdapter2 adapter2;
	private String temp;
	private int currentLevel = 3;
	private String str, str1;
	private String levelPlacePro, levelPlaceCity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
		showPlace();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (currentLevel == LEVEL_CITY) {
					// String city = data1.get(position).getCity();
					levelPlaceCity = data1.get(position).getCity();
					handlerCollection(str1, levelPlaceCity);
				}

				if (currentLevel == LEVEL_PROVINCE) {
					levelPlacePro = data.get(position).getProvince();
					handlerCollection(levelPlacePro, str);
				}

			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (currentLevel == LEVEL_DISTRICT) {
			currentLevel = LEVEL_CITY;
			listView.setAdapter(adapter1);
			textView.setText(levelPlacePro);
		} 
			else if (currentLevel == LEVEL_CITY) {
			listView.setAdapter(adapter);
			textView.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
			
		} 
			else {
			finish();
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_RESPONSE:
				String responsetemp = (String) msg.obj;
				if (currentLevel == LEVEL_CITY) {
					String cityT = msg.getData().getString("msgcity");
					showDistrict(responsetemp, cityT);
				}
				if (currentLevel == LEVEL_PROVINCE) {
					String prov = msg.getData().getString("msg");
					showCity(responsetemp, prov);
				}
				if (currentLevel == 3) {
					showProvince(responsetemp);
				}

				break;
			default:
				break;
			}

		};
	};

	private void handlerCollection(String pro, String citytemp) {
		Message message = new Message();
		message.what = SHOW_RESPONSE;
		message.obj = temp.toString();
		Bundle bun = new Bundle();
		bun.putString("msg", pro);
		bun.putString("msgcity", citytemp);
		message.setData(bun);
		handler.sendMessage(message);
	}

	private void showPlace() {

		String url = "http://v.juhe.cn/weather/citys?key=6d031f7f9efd85805226e36117e9dbec";
		HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				temp = response;
				handlerCollection(str, str1);
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				Log.d("error", "fault");
			}
		});
	}

	protected void parseWithJson(String response) {
		// TODO Auto-generated method stub
		data = new ArrayList<Place>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			String reason = jsonObject.getString("reason");
			if (jsonObject.getString("resultcode").equals("200")) {
				JSONArray jA = jsonObject.getJSONArray("result");
				for (int i = 0; i < jA.length(); i++) {
					Place place = new Place();
					if (!jA.getJSONObject(i)
							.getString("province")
							.equals(jA.getJSONObject(i + 1).getString(
									"province"))) {
						place.setProvince(jA.getJSONObject(i).getString(
								"province"));
						data.add(place);
					}
				}
			}

		} catch (Exception e) {
			Log.e("false", "ʡ�ݽ���ʧ��");
		}
	}

	protected void parseWithJson1(String response, String prov) {
		// TODO Auto-generated method stub
		data1 = new ArrayList<Place>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			String reason = jsonObject.getString("reason");
			if (jsonObject.getString("resultcode").equals("200")) {
				JSONArray jA = jsonObject.getJSONArray("result");
				for (int i = 0; i < jA.length(); i++) {
					if (jA.getJSONObject(i).getString("province").equals(prov)) {
						Place place = new Place();
						if (!jA.getJSONObject(i)
								.getString("city")
								.equals(jA.getJSONObject(i + 1).getString(
										"city"))) {
							place.setCity(jA.getJSONObject(i).getString("city"));
							data1.add(place);
						}
					}
				}
			}

		} catch (Exception e) {
			Log.e("false", "�м�����ʧ��");
		}
	}

	protected void parseWithJson2(String response, String cityTe) {
		// TODO Auto-generated method stub

		data2 = new ArrayList<Place>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			if (jsonObject.getString("resultcode").equals("200")) {
				JSONArray jA = jsonObject.getJSONArray("result");
				for (int i = 0; i < jA.length(); i++) {
					if (jA.getJSONObject(i).getString("city").equals(cityTe)) {
						Place place = new Place();
						place.setDistrict(jA.getJSONObject(i).getString("district"));
						data2.add(place);
					}
				}
			}

		} catch (Exception e) {
			Log.e("false", "��������ʧ��");
		}
	}

	private void showProvince(String datas) {
		currentLevel = LEVEL_PROVINCE;
		parseWithJson(datas);
		adapter = new PlaceAdapter(MainActivity.this, data,
				R.layout.weather_listview);
		listView.setAdapter(adapter);
		textView.setText("�й�");
	}

	private void showCity(String str, String pro) {
		currentLevel = LEVEL_CITY;

		parseWithJson1(str, pro);
		adapter1 = new PlaceAdapter1(MainActivity.this, data1,
				R.layout.weather_listview1);
		listView.setAdapter(adapter1);
		textView.setText(pro);

	}

	private void showDistrict(String str, String cityT) {
		currentLevel = LEVEL_DISTRICT;
		parseWithJson2(str, cityT);
		adapter2 = new PlaceAdapter2(MainActivity.this, data2,
				R.layout.weather_listview2);
		listView.setAdapter(adapter2);
		textView.setText(cityT);

	}

	private void initView() {
		textView = (TextView) findViewById(R.id.titile_text);
		listView = (ListView) findViewById(R.id.list_view);
	}

}
