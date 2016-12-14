package com.example.weathertestdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MyLetterView extends View {

	private Paint mPaint;
	private boolean isShowBg = false;// 用于区分是否显示view的背景
	private OnSlidingListener mOnSlidingListener;// 滑动此View的监听器
	private int choose = -1;// 用于标记当前所选中的位置
	private TextView mTvDialog;//用于接受从activity中传过来的，中间用于展示字母的textView
	private float height;
	//需要展示的数据
	private String[] letter = {"热门", "全部", "A", "B", "C", "D",
			"E", "F", "G", "H","J", "K", "L", "M", "N","P", "Q",
			"R", "S", "T","W", "X", "Y", "Z" };

	public MyLetterView(Context context) {
		super(context);

	}

	public MyLetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	public MyLetterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true); //抗锯齿功能，使图像边缘相对清晰，减少锯齿痕迹
		mPaint.setTextSize(35); //设置每个view中字体的大小，单位像素
		mPaint.setColor(Color.parseColor("#8c8c8c"));//设置右边自定义view字体颜色
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//当此View被按下时所显示的背景颜色
		if (isShowBg) {
			canvas.drawColor(Color.parseColor("#40000000"));
		}
		//使自定义view的高度只开始时获取一次
		if (height==0) {
			height=getHeight();
		}
		//计算每个字符所占的高度
		float singleHeight = height / letter.length; //view高度除以字符个数=每个字符的高度,view高度match_parent
		int width = getWidth();
		for (int i = 0; i < letter.length; i++) {
			String text = letter[i];

			float xPosition = width / 2 - mPaint.measureText(text) / 2;//x坐标，使字母显示在view中x方向的中间
			float yPosition = singleHeight * i + singleHeight;//每个字符的Y坐标
			
			//通过不断的改变yPosition将数组中的数据一个一个绘制到自定义的View中
			canvas.drawText(text, xPosition, yPosition, mPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int action = event.getAction();
		int position = (int) (event.getY() / getHeight() * letter.length);
		int oldChoose = choose;
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			
			isShowBg = true;
			if (oldChoose != position && mOnSlidingListener != null) {
				if (position > 0 && position < letter.length) {
					//将滑动到的字母传递到activity中
					mOnSlidingListener.sliding(letter[position]);
					choose=position;
					if(mTvDialog!=null){
						mTvDialog.setVisibility(View.VISIBLE);
						mTvDialog.setText(letter[position]);
					}
				}
				invalidate();
			}
			break;

		case MotionEvent.ACTION_MOVE:

			isShowBg = true;
			if (oldChoose != position && mOnSlidingListener != null) {
				if (position >=0 && position < letter.length) {
					mOnSlidingListener.sliding(letter[position]);
					choose=position;
					if(mTvDialog!=null){
						mTvDialog.setVisibility(View.VISIBLE);
						mTvDialog.setText(letter[position]);
					}
				}
				invalidate();
			}
			break;

		case MotionEvent.ACTION_UP:
			isShowBg = false;
			choose=-1;
			if(mTvDialog!=null){
				mTvDialog.setVisibility(View.GONE);
			}
			invalidate();
			break;
		}

		return true;
	}
	//MyLetterView的一个滑动的监听
	public void setOnSlidingListener(OnSlidingListener mOnSlidingListener) {
		this.mOnSlidingListener = mOnSlidingListener;
	}

	public interface OnSlidingListener {
		public void sliding(String str);
	}

	public void setTextView(TextView tvDialog) {
		mTvDialog=tvDialog;
	}

}
