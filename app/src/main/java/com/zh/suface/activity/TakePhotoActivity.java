package com.zh.suface.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.zh.suface.data.Media;
import com.zh.suface.utils.PickerConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TakePhotoActivity extends AppCompatActivity {

    Uri uriForFile;
    File mTmpFile = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            mTmpFile = createImageFile();
            Log.e("TakePhotoActivity", "length  =  " + mTmpFile.length());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//如果大于等于7.0使用FileProvider
            uriForFile = FileProvider.getUriForFile(this, this.getPackageName() + ".dmc", mTmpFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            startActivityForResult(intent, 100);
        } else {
            if (mTmpFile != null && mTmpFile.exists()) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                startActivityForResult(intent, 101);
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Media> medias = new ArrayList<>();

        if (mTmpFile.length() == 0) {
            finish();
            return;
        }
        if ((requestCode == 100 || requestCode == 101) && resultCode == RESULT_OK) {
            if (getIntent().getBooleanExtra(PickerConfig.TAILOR, false)) {
                Log.e("TakePhotoActivity", mTmpFile.getPath());
                Log.e("TakePhotoActivity", mTmpFile.getAbsolutePath());
                ClipActivity.jumpClipActivity(this, 102, mTmpFile.getPath());
            } else {
                result(medias);
            }
        } else if (requestCode == 102 && resultCode == RESULT_OK) {
            result(medias);
        } else if (requestCode == 102 && resultCode == 103) {
            finish();
        }

    }

    public void result(ArrayList<Media> medias) {
        Media media = new Media(mTmpFile.getPath(), mTmpFile.getName(), 0, 1, mTmpFile.length(), 0, "");
        medias.add(media);
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(PickerConfig.EXTRA_RESULT, medias);
        setResult(RESULT_OK, intent);
        finish();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

}
