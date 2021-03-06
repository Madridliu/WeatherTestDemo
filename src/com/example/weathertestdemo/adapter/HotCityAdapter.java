package com.example.weathertestdemo.adapter;

import java.util.List;

import com.example.weathertestdemo.R;
import com.example.weathertestdemo.model.City;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class HotCityAdapter extends BaseAdapter {

	private List<City> mHotCityList;
	private LayoutInflater mInflater;
	private Context mContext;
//	private callBack mCallBack;
	public HotCityAdapter(Context context, List<City> hotCityList) {
		this.mHotCityList = hotCityList;
		this.mContext = context;
		
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mHotCityList.size();
	}

	@Override
	public Object getItem(int position) {
		return mHotCityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_city, null);
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
				//点击热门城市响应
//				mCallBack.getCityName(mHotCityList.get(position).getName());
				Toast.makeText(mContext,mHotCityList.get(position).getName() + "", 0).show();
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tvCityName;
	}
	
	
}
