package com.zh.suface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.zh.suface.utils.CameraUtils;

/**
 * 动态surface布局
 */
public class MainActivity extends AppCompatActivity {
    private MySurfaceView surface1, surface2, surface3, surfaceTeacher, surfaceCenter;
    private int width, height;
    private CameraUtils cameraUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWindowWH();
        initView();
        checkPermissionAndCamera();
    }


    /**
     * 获取屏幕宽高
     */
    public void initWindowWH() {
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

    }

    /**
     * 绑定
     */
    public void initView() {
        surface1 = findViewById(R.id.stu_surface1);
        surface2 = findViewById(R.id.stu_surface2);
        surface3 = findViewById(R.id.stu_surface3);
        surfaceTeacher = findViewById(R.id.surface_teacher);
        surfaceCenter = findViewById(R.id.surface_center);
    }

    public void initData() {
        cameraUtils = new CameraUtils(this);
        if (cameraUtils.checkCameraHardware()) {
            cameraUtils.setCameraSurfaceHolder(surfaceTeacher);
        }

    }

    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            initData();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    1001);
        }
    }

    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1001) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                initData();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }


}
