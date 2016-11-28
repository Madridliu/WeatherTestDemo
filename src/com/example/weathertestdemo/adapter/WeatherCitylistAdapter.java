package com.example.weathertestdemo.adapter;

import java.util.List;
import com.example.weathertestdemo.R;
import com.example.weathertestdemo.model.Place;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WeatherCitylistAdapter extends BaseAdapter{
	
	private int resourceId;
	private List<Place> data;
	private Context context;

	public WeatherCitylistAdapter(int resourceId, List<Place> data,
			Context context) {
		super();
		this.resourceId = resourceId;
		this.data = data;
		this.context = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(resourceId, null);
			viewHolder.tvProvince = (TextView) convertView.findViewById(R.id.tv);
//			viewHolder.tvCity = (TextView) convertView.findViewById(R.id.tv);
//			viewHolder.tvDistrict = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvProvince.setText(data.get(position).getProvince());
//		viewHolder.tvCity.setText(data.get(position).getCity());
//		viewHolder.tvDistrict.setText(data.get(position).getDistrict());
		return convertView;
	}
	
	private class ViewHolder {
		TextView tvProvince;
		TextView tvCity;
		TextView tvDistrict;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
