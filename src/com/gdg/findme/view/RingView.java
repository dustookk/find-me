package com.gdg.findme.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RingView extends View {
	public static final String TAG = "com.gyh.countdowntest";
	private Paint paint;
	private Context context;
	public RingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		paint=new Paint();
		paint.setAntiAlias(true);   //没有边缘锯齿
		paint.setStyle(Paint.Style.STROKE);//没有圆心
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int center =getWidth()/2;
		int innerCircle=dip2px(context,83);
		int circleWidth=dip2px(context,5);
		
		//内园
		paint.setARGB(155, 167, 190, 206);
		paint.setStrokeWidth(2);
		canvas.drawCircle(center, center, innerCircle, paint);
		
		//圆环
		paint.setARGB(255, 212, 225, 233);
		paint.setStrokeWidth(circleWidth);
		canvas.drawCircle(center, center, innerCircle+circleWidth/2, paint);
		//外圆
		
		paint.setARGB(155, 167, 190, 206);
		paint.setStrokeWidth(2);
		canvas.drawCircle(center, center, innerCircle+circleWidth, paint);
		
	}
	public int dip2px(Context context,float dip){
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(dip*scale+0.5f);
	}
	
}


