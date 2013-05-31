package com.gdg.findme.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

public class CountDownIndicator extends View {
	private Paint paint;
	private double mPhase;
	private RectF localRectF;
	public CountDownIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint = new Paint();
		paint.setAntiAlias(true);
		localRectF=new RectF();
		// 得到扇形外面矩形的宽高
		float x = dip2px(context, 150);
		float y = dip2px(context, 150);

		RadialGradient rg = new RadialGradient(x, y, y, Color.argb(255, 143,
				201, 233), Color.argb(255, 166, 212, 235), TileMode.MIRROR);
		paint.setShader(rg);
		
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float f1=(float) (this.mPhase * 360.0);
		float f2=270.0f-f1;
		localRectF.set(1.0F, 1.0F, getWidth(), getHeight());
		canvas.drawArc(localRectF, f2, f1, true, paint);
	}

	public void setPhase(double paramDouble) {
		if ((paramDouble < 0.0) || (paramDouble > 1.0)) {
			String str = "phase: " + paramDouble;
			throw new IllegalArgumentException(str);
		}
		mPhase = paramDouble;
		invalidate();
	}

	private int dip2px(Context context, float dip) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}
}
