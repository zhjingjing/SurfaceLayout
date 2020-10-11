package com.zh.suface.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zh.suface.R;
import com.zh.suface.utils.FileUtils;
import com.zh.suface.view.ClipViewLayout;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * 裁剪
 */
public class ClipActivity extends AppCompatActivity {
    private ClipViewLayout clipViewLayout;
    private TextView tvCancel, tvPost;

    public static void jumpClipActivity(Activity activity, int request, String path) {
        Intent intent = new Intent(activity, ClipActivity.class);
        intent.putExtra("path", path);
        activity.startActivityForResult(intent, request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip);
        initView();
        initListener();
        initData();
    }


    public void initView() {
        tvCancel = findViewById(R.id.clip_cancel_btn);
        tvPost = findViewById(R.id.clip_post_btn);
        clipViewLayout = findViewById(R.id.clip_clip_view);
    }

    public void initListener() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(103);
                finish();

            }
        });

        tvPost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                generateUriAndReturn();
            }
        });
    }

    public void initData() {
        clipViewLayout.setImageSrc(getIntent().getStringExtra("path"));
    }


    /**
     * 生成Uri并且通过setResult返回给打开的activity
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateUriAndReturn() {
        //调用返回剪切图
        Bitmap zoomedCropBitmap = clipViewLayout.clip();
        if (zoomedCropBitmap == null) {
            Log.e("android", "zoomedCropBitmap == null");
            return;
        }
        Uri mSaveUri = Uri.fromFile(new File(FileUtils.getClipImagesPath(), new Date().getTime() + ".jpg"));
        if (mSaveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(mSaveUri);
                if (outputStream != null) {
                    zoomedCropBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException ex) {
                Log.e("android", "Cannot open file: " + mSaveUri, ex);
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Intent intent = new Intent();
            intent.setData(mSaveUri);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
