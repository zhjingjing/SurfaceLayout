package com.zh.suface.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class CameraUtils {
    private Context context;
    private SurfaceViewCallback callback = new SurfaceViewCallback();

    public CameraUtils(Context context) {
        this.context = context;
    }

    public boolean checkCameraHardware() {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 设置相机界面SurfaceView的Holder
     *
     * @param surfaceView Holder所绑定的响应的SurfaceView
     */
    public void setCameraSurfaceHolder(SurfaceView surfaceView) {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(callback);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        callback.setContext(context);
    }
}
