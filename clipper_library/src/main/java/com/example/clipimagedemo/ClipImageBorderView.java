package com.example.clipimagedemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @ClassName: ClipImageBorderView
 * @Description:
 * @author xiechengfa2000@163.com
 * @date 2015-5-10 �???10:22:53
 */
public class ClipImageBorderView extends View {
	/**
	 * 水平?��??�?View??边�??
	 */
	private int mHorizontalPadding;
	/**
	 * ???��?��??�?View??边�??
	 */
	private int mVerticalPadding;
	/**
	 * �??��???�形??宽度
	 */
	private int mWidth;
	/**
	 * 边�???�??��?�?认为?��??
	 */
	private int mBorderColor = Color.parseColor("#FFFFFF");
	/**
	 * 边�???宽�????�?dp
	 */
	private int mBorderWidth = 1;

	private Paint mPaint;

	public ClipImageBorderView(Context context) {
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 计�???�形?��????宽�??
		mWidth = getWidth() - 2 * mHorizontalPadding;
		// 计�??�?离�?�????�边?? ??边�??
		mVerticalPadding = (getHeight() - mWidth) / 2;
		mPaint.setColor(Color.parseColor("#aa000000"));
		mPaint.setStyle(Style.FILL);
		// �??�左�?1
		canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
		// �??��?�边2
		canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
				getHeight(), mPaint);
		// �??��?�?3
		canvas.drawRect(mHorizontalPadding, 0, getWidth() - mHorizontalPadding,
				mVerticalPadding, mPaint);
		// �??��?�?4
		canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
				getWidth() - mHorizontalPadding, getHeight(), mPaint);
		// �??��?边�??
		mPaint.setColor(mBorderColor);
		mPaint.setStrokeWidth(mBorderWidth);
		mPaint.setStyle(Style.STROKE);
		canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
				- mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);

	}

	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}
}
