package com.example.weathertestdemo.adapter;

import java.util.List;

import android.content.Context;

import com.example.weathertestdemo.R;
import com.example.weathertestdemo.model.Place;
import com.example.weathertestdemo.util.CommonAdapter;
import com.example.weathertestdemo.util.ViewHolder;

public class PlaceAdapter2 extends CommonAdapter<Place>{

	public PlaceAdapter2(Context context, List<Place> datas, int LayoutId) {
		super(context, datas, LayoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void convert(ViewHolder holder, Place place) {
		// TODO Auto-generated method stub
		holder.setText(R.id.tv2, place.getDistrict());
	}

	
}
