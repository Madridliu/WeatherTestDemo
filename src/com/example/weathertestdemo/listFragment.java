package com.example.weathertestdemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.example.weathertestdemo.adapter.CityListAdapter;
import com.example.weathertestdemo.adapter.CityListAdapter.CallBack;
import com.example.weathertestdemo.adapter.SearchResultAdapter;
import com.example.weathertestdemo.dbhelper.AllCitySqliteOpenHelper;
import com.example.weathertestdemo.dbhelper.CitySqliteOpenHelper;
import com.example.weathertestdemo.model.City;
import com.example.weathertestdemo.util.PingYinUtil;
import com.example.weathertestdemo.view.MyLetterView;
import com.example.weathertestdemo.view.MyLetterView.OnSlidingListener;

public class listFragment extends Fragment {

	protected static final String TAG = "MainActivity";
	private MyLetterView myLetterView;// �Զ����View
	private TextView tvDialog;// ��������ʾ��ĸ��TextView
	private ListView lvCity;// ���г����б�չʾ
	private EditText etSearch;
	private ListView lvResult;// ��������б�չʾ
	private TextView tvNoResult;// �����޽��ʱ����չʾ

	private ImageButton imgBtn;

	private List<City> allCityList;// ���еĳ���
	private List<City> hotCityList;// ���ų����б�
	private List<City> searchCityList;// ���������б�

	public CitySqliteOpenHelper cityOpenHelper;// �Ա�����������ʳ��е����ݿ�����İ�����
	public SQLiteDatabase cityDb;// ����������ʳ��е����ݿ�
	public CityListAdapter cityListAdapter;
	public SearchResultAdapter searchResultAdapter;
	private boolean isScroll = false;
	private boolean mReady = false;
	private Handler handler;
	private OverlayThread overlayThread; // ��ʾ����ĸ�Ի���

	private CallBack mCallback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.activity_main, container, false);
		myLetterView = (MyLetterView) view.findViewById(R.id.my_letterview);
		tvDialog = (TextView) view.findViewById(R.id.tv_dialog);
		myLetterView.setTextView(tvDialog);
		lvCity = (ListView) view.findViewById(R.id.lv_city);
		etSearch = (EditText) view.findViewById(R.id.et_search);
		lvResult = (ListView) view.findViewById(R.id.lv_result);
		tvNoResult = (TextView) view.findViewById(R.id.tv_noresult);
		imgBtn = (ImageButton) view.findViewById(R.id.back);
		imgBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackkey();
			}
		});

		initData();
		setListener();
		initAllCityData();
		initHotCityData();// ��ʼ�����ų���
		setAdapter();// ����������
		mReady = true;
		return view;
	}

	private void onBackkey() {
		WeatherActivity wa = (WeatherActivity) getActivity();
		wa.onBackPressed();
	}

	// ������ʾ��ĸ��TextViewΪ���ɼ�
	private class OverlayThread implements Runnable {
		@Override
		public void run() {
			tvDialog.setVisibility(View.INVISIBLE);
		}
	}

	public listFragment(CallBack callback) {
		this.mCallback = callback;
	}

	private void setAdapter() {
		cityListAdapter = new CityListAdapter(getActivity(), allCityList,
				hotCityList, new CallBack() {

					@Override
					public void getCityName(String cityname) {
						// TODO Auto-generated method stub
						if (cityname != null) {
							mCallback.getCityName(cityname);
							onBackkey();
						} else {
							Log.e("null", "kong");
						}
					}
				});
		lvCity.setAdapter(cityListAdapter);

		searchResultAdapter = new SearchResultAdapter(getActivity(),
				searchCityList, new CallBack() {

					@Override
					public void getCityName(String cityname) {
						// TODO Auto-generated method stub
						mCallback.getCityName(cityname);
						onBackkey();
					}
				});

		lvResult.setAdapter(searchResultAdapter);
	}

	private void initData() {
		allCityList = new ArrayList<City>();
		hotCityList = new ArrayList<City>();
		searchCityList = new ArrayList<City>();
		handler = new Handler();
		overlayThread = new OverlayThread();
	}

	private void setListener() {
		// �Զ���myLetterView��һ������

		myLetterView.setOnSlidingListener(new OnSlidingListener() {

			@Override
			public void sliding(String s) {
				isScroll = false;
				if (cityListAdapter.alphaIndexer.get(s) != null) {
					// ����MyLetterView�����������ݻ��ListViewӦ��չʾ��λ��
					int position = cityListAdapter.alphaIndexer.get(s);
					// ��listViewչʾ����Ӧ��λ��
					lvCity.setSelection(position);
				}
			}
		});

		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				if (s.toString() == null || "".equals(s.toString())) {
					myLetterView.setVisibility(View.VISIBLE);
					lvCity.setVisibility(View.VISIBLE);
					lvResult.setVisibility(View.GONE);
					tvNoResult.setVisibility(View.GONE);
				} else {
					searchCityList.clear();
					myLetterView.setVisibility(View.GONE);
					lvCity.setVisibility(View.GONE);
					getResultCityList(s.toString());
					if (searchCityList.size() <= 0) {
						lvResult.setVisibility(View.GONE);
						tvNoResult.setVisibility(View.VISIBLE);
					} else {
						lvResult.setVisibility(View.VISIBLE);

						tvNoResult.setVisibility(View.GONE);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		lvCity.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_TOUCH_SCROLL
						|| scrollState == SCROLL_STATE_FLING) {
					isScroll = true;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (!isScroll) {
					return;
				}
				if (mReady) {
					String text;
					if (allCityList.isEmpty()) {
						allCityList = getCityList();
					}
					String name = allCityList.get(firstVisibleItem).getName();
					String pinyin = allCityList.get(firstVisibleItem)
							.getPinyin();
					if (firstVisibleItem < 2) { // �м�textView��ÿ����ʾ����Ŀ��������Ϊ�����ࡰ���š�����ȫ����
						text = name;
					} else {
						text = PingYinUtil.converterToFirstSpell(pinyin)
								.substring(0, 1).toUpperCase();
					}
					tvDialog.setText(text);
					tvDialog.setVisibility(View.VISIBLE);
					handler.removeCallbacks(overlayThread);
					// �ӳ�һ���ִ�У����м���ʾ��TextViewΪ���ɼ�
					handler.postDelayed(overlayThread, 1000);
				}
			}
		});

	}

	private void initAllCityData() {

		City city = new City("����", "0");
		allCityList.add(city);
		city = new City("ȫ��", "1");
		allCityList.add(city);
		allCityList.addAll(getCityList());
	}

	@SuppressWarnings("unchecked")
	private ArrayList<City> getCityList() {
		SQLiteDatabase db;
		Cursor cursor = null;
		// ��ȡassetsĿ¼�µ����ݿ��е����г��е�openHelper
		AllCitySqliteOpenHelper op = new AllCitySqliteOpenHelper(getActivity());
		ArrayList<City> cityList = new ArrayList<City>();
		try {
			op.createDataBase();
			db = op.getWritableDatabase();
			cursor = db.rawQuery("select * from city", null);

			while (cursor.moveToNext()) {
				String cityName = cursor.getString(cursor
						.getColumnIndex("name"));
				String cityPinyin = cursor.getString(cursor
						.getColumnIndex("pinyin"));
				City city = new City(cityName, cityPinyin);
				cityList.add(city);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
		Collections.sort(cityList, comparator);
		return cityList;

	}

	private void initHotCityData() {
		City city = new City("����", "0");
		hotCityList.add(city);
		city = new City("�Ϻ�", "0");
		hotCityList.add(city);
		city = new City("����", "0");
		hotCityList.add(city);
		city = new City("����", "0");
		hotCityList.add(city);
		city = new City("�Ͼ�", "0");
		hotCityList.add(city);
		city = new City("����", "0");
		hotCityList.add(city);
		city = new City("����", "0");
		hotCityList.add(city);
		city = new City("�人", "0");
		hotCityList.add(city);
		city = new City("����", "0");
		hotCityList.add(city);
	}

	/**
	 * �Զ����������򣬰���A-Z��������
	 */
	@SuppressWarnings("rawtypes")
	Comparator comparator = new Comparator<City>() {
		@Override
		public int compare(City lhs, City rhs) {
			String a = lhs.getPinyin().substring(0, 1);
			String b = rhs.getPinyin().substring(0, 1);
			int flag = a.compareTo(b);
			if (flag == 0) {
				return a.compareTo(b);
			} else {
				return flag;
			}
		}
	};

	@SuppressWarnings("unchecked")
	private void getResultCityList(String keyword) {
		AllCitySqliteOpenHelper dbHelper = new AllCitySqliteOpenHelper(
				getActivity());
		try {
			dbHelper.createDataBase();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.rawQuery(
					"select * from city where name like \"%" + keyword
							+ "%\" or pinyin like \"%" + keyword + "%\"", null);
			City city;
			while (cursor.moveToNext()) {
				String cityName = cursor.getString(cursor
						.getColumnIndex("name"));
				String cityPinyin = cursor.getString(cursor
						.getColumnIndex("pinyin"));
				city = new City(cityName, cityPinyin);
				searchCityList.add(city);
			}
			cursor.close();
			db.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ���õ��ļ��ϰ����Զ����comparator�Ĺ����������
		Collections.sort(searchCityList, comparator);
		// searchResultAdapter.freshView(searchCityList);
		searchResultAdapter.notifyDataSetChanged(); // ˢ�������������
	}

}
