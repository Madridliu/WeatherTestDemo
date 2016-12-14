package com.example.weathertestdemo.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weathertestdemo.R;
import com.example.weathertestdemo.model.City;
import com.example.weathertestdemo.view.MyGridView;

public class CityListAdapter extends BaseAdapter {

	private Context mContext;
	private List<City> mAllCityList;
	private List<City> mHotCityList;
	public HashMap<String, Integer> alphaIndexer;// ��Ŵ��ڵĺ���ƴ������ĸ����֮��Ӧ���б�λ��
	private String[] sections;// ��Ŵ��ڵĺ���ƴ������ĸ
	private final int VIEW_TYPE = 3;//view�����͸���
	private int temp;
	private CallBack mCallback;
	
	public CityListAdapter(Context context,int temp,List<City> hotCityList,CallBack callback){
		this.mContext = context;
		this.temp = temp;
		this.mHotCityList = hotCityList;
		this.mCallback = callback;
	}
	
	public CityListAdapter(Context context, List<City> allCityList,
			List<City> hotCityList,CallBack callback) {
		this.mContext = context;
		this.mAllCityList = allCityList;
		this.mHotCityList = hotCityList;
//		this.mRecentCityList=recentCityList;
		this.mCallback = callback;
		alphaIndexer = new HashMap<String, Integer>();
		sections = new String[allCityList.size()];
		
		//�������ҪĿ���ǽ�listview��Ҫ��ʾ��ĸ����Ŀ���������������ڻ���ʱ���λ�ã�alphaIndexer��Acitivity�е���
		for (int i = 0; i < mAllCityList.size(); i++) {
			// ��ǰ����ƴ������ĸ
			String currentStr = getAlpha(mAllCityList.get(i).getPinyin());
			// ��һ������ƴ������ĸ�����������Ϊ" "
			String previewStr = (i - 1) >= 0 ? getAlpha(mAllCityList.get(i - 1).getPinyin()) : " ";
			if (!previewStr.equals(currentStr)) {
				String name = getAlpha(mAllCityList.get(i).getPinyin());
				alphaIndexer.put(name, i);
				sections[i] = name;
			}
		}
	}

	@Override
	public int getViewTypeCount() {

		return VIEW_TYPE;
	}

	@Override
	public int getItemViewType(int position) {
		return position < 2 ? position : 2;
	}

	@Override
	public int getCount() {
		if (temp==0) {
			return mAllCityList.size();
		}else {
			return mHotCityList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (temp==0) {
			return mAllCityList.get(position);
		}else {
			return mHotCityList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	MyGridView gvRecentVisitCity;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		int viewType = getItemViewType(position);
		if (temp==1) {
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(mContext,R.layout.item_city, null);
				viewHolder.tvCityName = (TextView) convertView
						.findViewById(R.id.tv_city_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.tvCityName.setText(mHotCityList.get(position).getName());
			viewHolder.tvCityName.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					Log.e("positon", position+"--"+mHotCityList.get(position).getName());
					mCallback.getCityName(mHotCityList.get(position).getName());
				}
			});
		}else if (viewType == 0) {//���ų���
			convertView = View.inflate(mContext,R.layout.item_recent_visit_city, null);
			TextView tvRecentVisitCity=(TextView) convertView.findViewById(R.id.tv_recent_visit_city);
			tvRecentVisitCity.setText("���ų���");
			gvRecentVisitCity = (MyGridView) convertView.findViewById(R.id.gv_recent_visit_city);
			gvRecentVisitCity.setAdapter(new CityListAdapter(mContext,1, mHotCityList,mCallback));
			
		} else if (viewType == 1) {//ȫ�����У���չʾ��ȫ���������ĸ��֡�
			convertView = View.inflate(mContext,R.layout.item_all_city_textview, null);
		} else {//���ݿ������еĳ��е�����չʾ
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_city_list,null);
				viewHolder.tvAlpha = (TextView) convertView.findViewById(R.id.tv_alpha);
				viewHolder.tvCityName = (TextView) convertView.findViewById(R.id.tv_city_name);
				viewHolder.llMain=(LinearLayout) convertView.findViewById(R.id.ll_main);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (position >= 1) {
				viewHolder.tvCityName.setText(mAllCityList.get(position).getName());
				viewHolder.llMain.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//���ȫ��������Ӧ
						if(mCallback!=null){
							
							mCallback.getCityName(mAllCityList.get(position).getName());
							
						}else{
							Log.e("mcallback-->", "null");
						}
					}
				});
				String currentStr = getAlpha(mAllCityList.get(position).getPinyin());
				String previewStr = (position - 1) >= 0 ? getAlpha(mAllCityList
						.get(position - 1).getPinyin()) : " ";
				//�����ǰ����Ŀ�ĳ������ֵ�ƴ��������ĸ����ǰһ����Ŀ�ĳ��е����ֵ�ƴ��������ĸ����ͬ���򽫲����е�չʾ��ĸ��TextViewչʾ����
				if (!previewStr.equals(currentStr)) {
					viewHolder.tvAlpha.setVisibility(View.VISIBLE);
					viewHolder.tvAlpha.setText(currentStr);
				} else {
					viewHolder.tvAlpha.setVisibility(View.GONE);
				}
			}

		}

		return convertView;
	}

	// ��ú���ƴ������ĸ
	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); //ת��Ϊ��д
		} /*else if (str.equals("0")) {
			return "��λ";
		} else if (str.equals("1")) {
			return "���";
		}*/ else if (str.equals("0")) {
			return "����";
		} else if (str.equals("1")) {
			return "ȫ��";
		} else {
			return "#";
		}
	}

	class ViewHolder {
		TextView tvAlpha;
		TextView tvCityName;
		LinearLayout llMain;
	}
	
	public CityListAdapter (CallBack callback) {
		this.mCallback = callback;
	}
	
	public interface CallBack {
		public void getCityName(String cityname);
	}
	
}
