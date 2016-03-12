package com.lina.android.mytestln.http.image.loader;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import libcore.io.DiskLruCache;

/**
 * Created by Administrator on 2016/3/11.
 */
public class BitmapCache {

    private LruCache<String, Bitmap> memoryLruCache;//LRU内存缓存
    public DiskLruCache diskLruCache;//LRU磁盘缓存
    private static final String DISK_CACHE_SUBDIR = "temp";
    private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;

    public BitmapCache(Context context) {
        try {
            //获取当前Activity的内存大小
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int memoryClass = am.getMemoryClass();
            memoryLruCache = new LruCache<>(memoryClass * 1024 * 1024 / 8);//字节，八分之一的内存作为缓存大小（通常）

            //open（）方法接收四个参数：
            //1、指定的是数据的缓存地址
            //2、指定当前应用程序的版本号
            //3、指定同一个key可以对应多少个缓存文件，基本都是传1
            //4、指定最多可以缓存多少字节的数据 通常是10M
            diskLruCache = DiskLruCache.open(getCacheDir(context,DISK_CACHE_SUBDIR), getAppVersion(context), 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取缓存目录
    private File getCacheDir(Context context,String name) {
        String cachePath = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
                !Environment.isExternalStorageRemovable() ?
                context.getExternalCacheDir().getPath() : context.getCacheDir().getPath();
        name = cachePath + File.separator + name;
        return new File(name);
    }

    //获取版本号
    private int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    //MD5计算摘要
    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //添加内存缓存
    public void addBitmapToMemoryCache(String url, Bitmap bitmap) {
        if(url==null||bitmap==null){
            return;
        }
        String key = hashKeyForDisk(url);
        if (getBitmapFromMemoryCache(key) == null) {
            memoryLruCache.put(key, bitmap);
        }
    }

    //从内存缓存中读取方法
    public Bitmap getBitmapFromMemoryCache(String url) {
        String key = hashKeyForDisk(url);
        return memoryLruCache.get(key);
    }


    //添加到磁盘缓存
    public void addBitmapToDiskCache(String url, Bitmap bitmap) {
        if(url==null||bitmap==null){
            return;
        }
        String key = hashKeyForDisk(url);
        DiskLruCache.Editor editor = null;
        try {
            editor = diskLruCache.edit(key);
            //位图压缩后输出（参数：压缩格式，质量（100表示不压缩，30表示压缩70%），输出流）
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, editor.newOutputStream(0));
            editor.commit();
        } catch (IOException e) {
            try {
                editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    /**
     * 从磁盘缓存中读取方法
     */
    public Bitmap getBitmapFromDiskCache(String url) {
        String key = hashKeyForDisk(url);
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            System.out.println(snapshot);
            if (snapshot != null) {
                InputStream in = snapshot.getInputStream(0);
                bitmap = BitmapFactory.decodeStream(in);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    //刷新磁盘缓存
    public void flush() {
        if (diskLruCache != null) {
            try {
                diskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //关闭磁盘缓存
    public void close() {
        if (diskLruCache != null && !diskLruCache.isClosed()) {
            try {
                diskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
