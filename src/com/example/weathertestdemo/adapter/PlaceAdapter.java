package com.example.weathertestdemo.adapter;

/*
 * 继承一个通用的adapter， 跟应用运行无关
 */
import java.util.List;

import android.content.Context;

import com.example.weathertestdemo.R;
import com.example.weathertestdemo.model.Place;
import com.example.weathertestdemo.util.CommonAdapter;
import com.example.weathertestdemo.util.ViewHolder;

public class PlaceAdapter extends CommonAdapter<Place> {

	public PlaceAdapter(Context context, List<Place> datas, int LayoutId) {
		super(context, datas, LayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder holder, Place place) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tv, place.getProvince())
				.setText(R.id.tv, place.getCity())
				.setText(R.id.tv, place.getDistrict());
	}

}
