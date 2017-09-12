package com.example.liruibao.exprogress.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.liruibao.exprogress.R;

/**
 * Created by liruibao on 2017/9/4.
 */

public class WaterButton extends android.support.v7.widget.AppCompatButton {

    private boolean mRunning = false;
    private int[] mStrokeWidthArr;
    private int mMaxStrokeWidth;
    private int mRippleCount;
    private int mRippleSpacing;
    private Paint mPaint;
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;

    public WaterButton(Context context) {
        super(context);
    }

    public WaterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }

    public WaterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context,attrs);
    }

    @Override
    public void setBackground(Drawable background) {
        Drawable back = getResources().getDrawable(R.drawable.shape_circle_btn);
        super.setBackground(back);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterRippleView);
        int waveColor = typedArray.getColor(R.styleable.WaterRippleView_rippleColor,
                ContextCompat.getColor(context, R.color.colorPrimary));
        Drawable drawable = typedArray.getDrawable(R.styleable.WaterRippleView_rippleCenterIcon);
        mRippleCount = typedArray.getInt(R.styleable.WaterRippleView_rippleCount, 2);
        mRippleSpacing = typedArray.getDimensionPixelSize(R.styleable.WaterRippleView_rippleSpacing,
                16);
        mRunning = typedArray.getBoolean(R.styleable.WaterRippleView_rippleAutoRunning, false);
        typedArray.recycle();
        mBitmap = ((BitmapDrawable) drawable).getBitmap();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(waveColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = (mRippleCount * mRippleSpacing + 50 / 2) * 2;
        mWidth = resolveSize(size, widthMeasureSpec);
        mHeight = resolveSize(size, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
        mMaxStrokeWidth = (mWidth - 0) / 2;
        initArray();
    }
    private void initArray() {
        mStrokeWidthArr = new int[mRippleCount];
        for (int i = 0; i < mStrokeWidthArr.length; i++) {
            mStrokeWidthArr[i] = - mMaxStrokeWidth / mRippleCount * i;
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        drawBitmap(canvas);
        if (mRunning) {
            drawRipple(canvas);
            postInvalidateDelayed(50);
        }
    }
    private void drawBitmap(Canvas canvas) {
//        int left = (mWidth - mBitmap.getWidth()) / 2;
//        int top = (mHeight - mBitmap.getHeight()) / 2;
//        canvas.drawBitmap(mBitmap, left, top, null);
    }
    private void drawRipple(Canvas canvas) {
        for (int strokeWidth : mStrokeWidthArr) {
            if (strokeWidth < 0) {
                continue;
            }
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setAlpha(255 - 255 * strokeWidth / mMaxStrokeWidth);
            canvas.drawCircle(mWidth / 2, mHeight / 2, 0 / 2 + strokeWidth/2,
                    mPaint);
        }
        for (int i = 0; i < mStrokeWidthArr.length; i++) {
            if ((mStrokeWidthArr[i] += 4) > mMaxStrokeWidth) {
                mStrokeWidthArr[i] = 0;
            }
        }
    }
    public void start() {
        mRunning = true;
        postInvalidate();
    }
    public void stop() {
        mRunning = false;
        initArray();
        postInvalidate();
    }
}
