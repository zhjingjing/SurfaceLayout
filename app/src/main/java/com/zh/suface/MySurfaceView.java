package com.zh.suface;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

/**
 * create by zj on 2020/10/11
 */
public class MySurfaceView extends SurfaceView {
    public MySurfaceView(Context context) {
        super(context);
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i("xxxx", widthMeasureSpec + "---" + heightMeasureSpec);
//        int minSpec = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int minSize=0;
        int wid=getMeasuredWidth();
        int height=getMeasuredHeight();
        minSize=Math.min(wid,height);

        setMeasuredDimension(minSize,minSize);
    }
}
