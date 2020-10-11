package com.zh.suface.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zh.suface.data.Media;
import com.zh.suface.utils.FileUtils;
import com.zh.suface.utils.PickerConfig;
import com.zh.suface.view.MySurfaceView;
import com.zh.suface.R;
import com.zh.suface.utils.CameraUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 动态surface布局
 */
public class MainActivity extends AppCompatActivity {
    private MySurfaceView surface1, surface2, surface3, surfaceTeacher, surfaceCenter;
    private int width, height;
    private CameraUtils cameraUtils;
    private TextView tvTakePhoto, tvPath;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWindowWH();
        initView();
        initListener();
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
        tvTakePhoto = findViewById(R.id.tv_take_photo);
        tvPath = findViewById(R.id.tv_img_path);

    }

    /**
     * 点击事件
     */
    public void initListener() {
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, 1002);
                    } else {
                        //跳转相机
                        Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class); //Take a photo with a camera
                        intent.putExtra(PickerConfig.TAILOR, getIntent().getBooleanExtra(PickerConfig.TAILOR, true));//是否裁剪
                        startActivityForResult(intent, 102);

                    }
                } else {
                    //跳转相机
                    Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class); //Take a photo with a camera
                    intent.putExtra(PickerConfig.TAILOR, getIntent().getBooleanExtra(PickerConfig.TAILOR, true));//是否裁剪
                    startActivityForResult(intent, 102);

                }

            }
        });
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
        } else if (requestCode == 1002) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FileUtils.createSurfaceApp();
                //跳转相机
                Intent intent = new Intent(MainActivity.this, TakePhotoActivity.class); //Take a photo with a camera
                intent.putExtra(PickerConfig.TAILOR, getIntent().getBooleanExtra(PickerConfig.TAILOR, true));//是否裁剪
                startActivityForResult(intent, 102);

            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "存储权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            //裁剪完成
            ArrayList<Media> selects = data.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT);
            if (selects.size() > 0) {
                result(selects);
            }
        }
    }

    public void result(ArrayList<Media> medias) {
        File file = new File(medias.get(0).path);
        tvPath.setText(medias.get(0).path);
    }


    /**
     * 根据uri获取文件路径
     */
    public String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI,
                new String[]{MediaStore.Images.ImageColumns.DATA},//
                null, null, null);
        if (cursor == null) result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }


}
