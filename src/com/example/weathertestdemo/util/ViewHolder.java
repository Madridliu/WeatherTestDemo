package com.example.weathertestdemo.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewHolder {
	
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	
	public ViewHolder(Context context, ViewGroup parent,int position, int layoutId) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false); 
		mConvertView.setTag(this);
	}
	
	public static ViewHolder get(Context context, View convertView, 
			ViewGroup parent,int position, int layoutId) {
		
		if (convertView == null) {
			return new ViewHolder(context, parent, position, layoutId);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			return holder;
		}
	}
	
	/**
	 * 通过viewId获取控件
	 */
	public <T extends View> T getView(int viewId) {
		
		View view = mViews.get(viewId);
		
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		
		return (T) view;
		
	}

	public View getConvertView() {
		return mConvertView;
	}
	
	/**
	 * 封装setText方法，设置TextView的值
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	/**
	 * 后续可以自定义封装更多的设置图片，网址等的各种方法来完善此ViewHolder
	 */
	
}
