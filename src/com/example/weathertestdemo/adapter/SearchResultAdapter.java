package com.example.weathertestdemo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weathertestdemo.R;
import com.example.weathertestdemo.adapter.CityListAdapter.CallBack;
import com.example.weathertestdemo.model.City;


public class SearchResultAdapter extends BaseAdapter {
	
	private List<City> mSearchList;
	private Context mContext;
	private LayoutInflater mInflater;
	private CallBack mCallback;
	
	public SearchResultAdapter(Context context, List<City> searchList, CallBack callback){
		this.mSearchList=searchList;
		this.mContext=context;
		this.mCallback = callback;
		mInflater=LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return mSearchList.size();
	}

	@Override
	public Object getItem(int position) {
		return mSearchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_search_list,null);
			viewHolder.tvCityName=(TextView) convertView.findViewById(R.id.tv_city_name);
			convertView.setTag(viewHolder);
		}else{
			viewHolder =(ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvCityName.setText(mSearchList.get(position).getName());
		viewHolder.tvCityName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mCallback.getCityName(mSearchList.get(position).getName());
				
			}
		});
		
		return convertView;
	}
	//有时当notifyDataSetChanged()刷新失败时，则手动传值刷新
	/*public void freshView(List<City> mSearchList){
		this.mSearchList=mSearchList;
		notifyDataSetChanged();
	}*/
	
	class ViewHolder{
		LinearLayout ll_item;
		TextView tvCityName;
	}
	
	public SearchResultAdapter (CallBack callback) {
		this.mCallback = callback;
	}
	

}
