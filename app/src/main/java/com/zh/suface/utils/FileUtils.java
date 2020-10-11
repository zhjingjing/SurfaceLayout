package com.zh.suface.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * create by zj on 2020/10/11
 */
public class FileUtils {
    public static String filePackagePath = Environment.getExternalStorageDirectory().getAbsolutePath();

    /**
     * 创建SurfaceApp文件夹
     */
    public static void createSurfaceApp() {
        Log.e("xxxx", "createSurfaceApp:isDirExists=" + isDirExists("SurfaceApp"));
        if (!isDirExists("SurfaceApp")) {
            Log.e("xxxx", "createSurfaceApp:createFolder=" + createFolder(FileUtils.filePackagePath + "/SurfaceApp"));
            createFolder(FileUtils.filePackagePath + "/SurfaceApp");
        }
    }

    /**
     * 获取剪切图片所在目录
     *
     * @return
     */
    public static String getClipImagesPath() {
        createClipImages();
        return FileUtils.filePackagePath + "/SurfaceApp/ClipImages";
    }

    /**
     * 创建ClipImages文件夹
     */
    private static void createClipImages() {
        if (!isDirExists("SurfaceApp")) {
            createSurfaceApp();
        }
        if (!FileUtils.isDirExists("SurfaceApp", "ClipImages")) {
            FileUtils.createFolder(FileUtils.filePackagePath + "/SurfaceApp/ClipImages");
        }
    }

    /**
     * 创建ClipImages文件夹
     */
    private static void createVideoFirstImgs() {
        if (!isDirExists("SurfaceApp")) {
            createSurfaceApp();
        }
        if (!FileUtils.isDirExists("SurfaceApp", "VideoFirstImgs")) {
            createFolder(FileUtils.filePackagePath + "/SurfaceApp/VideoFirstImgs");
        }
    }

    /**
     * 创建裁剪临时文件
     */
    public static void createCompressFolder() {
        if (!isDirExists("SurfaceApp")) {
            createSurfaceApp();
        }
        if (!FileUtils.isDirExists("SurfaceApp", "CompressFolder")) {
            createFolder(FileUtils.filePackagePath + "/SurfaceApp/CompressFolder");
        }
    }


    /**
     * 判断文件夹是否存在
     *
     * @param fileName
     * @return true存在 false不存在
     */
    private static boolean isDirExists(String fileName) {
        File file = new File(filePackagePath + "/" + fileName);
        return file.exists() || file.isDirectory();
    }

    /**
     * 判断文件夹是否存在
     *
     * @param fileName
     * @return true存在 false不存在
     */
    private static boolean isDirExists(String DirName, String fileName) {
        File file = new File(filePackagePath + "/" + DirName);
        if (file.exists()) {
            File[] fileList = getFileList(file.getAbsolutePath());
            for (File file1 : fileList) {
                if (fileName.endsWith(file1.getName()))
                    return true;
            }
        }
        return false;
    }

    /**
     * 判断文件是否已经存在
     *
     * @param fileName 要检查的文件名
     * @return boolean, true表示存在，false表示不存在
     */
    public static boolean isFileExist(String fileName) {
        File file = new File(filePackagePath + "/" + fileName);
        return file.exists();
    }

    /**
     * 新建目录
     *
     * @param path 目录的绝对路径
     * @return 创建成功则返回true
     */
    private static boolean createFolder(String path) {
        Log.e("xxxx", "createFolder=" + path);
        File file = new File(path);
        return file.mkdirs();
    }

    /**
     * 创建文件
     *
     * @param path     文件所在目录的目录名
     * @param fileName 文件名
     * @return 文件新建成功则返回true
     */
    public static boolean createFile(String path, String fileName) {
        File file = new File(path + File.separator + fileName);
        if (file.exists()) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 获取某个路径下的文件列表
     *
     * @param path 文件路径
     * @return 文件列表File[] files
     */
    public static File[] getFileList(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                return files;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


}
