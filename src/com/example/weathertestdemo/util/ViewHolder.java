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
	 * ͨ��viewId��ȡ�ؼ�
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
	 * ��װsetText����������TextView��ֵ
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}
	
	/**
	 * ���������Զ����װ���������ͼƬ����ַ�ȵĸ��ַ��������ƴ�ViewHolder
	 */
	
}
