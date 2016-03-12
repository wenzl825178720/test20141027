package com.lina.android.mytestln.http.image.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 带缓存的图片下载
 * Created by Administrator on 2016/3/12.
 */
public class LinaImageLoader {
    //回调接口
    public interface CallBack<T> {
        public void response(T entity);
    }

    public Handler handler = new Handler();

    private BitmapCache bitmapCache;

    private static LinaImageLoader instance;

    //单例模式
    public static LinaImageLoader getInstance(Context context) {
        if (instance == null) {
            instance = new LinaImageLoader(context);
        }
        return instance;
    }


    private LinaImageLoader(Context context) {
        bitmapCache = new BitmapCache(context);
    }

    //从缓存中获取图片
    public void loadBitmap(final ImageView imageView, final String url) {
        //从内存缓存中取图片
        Bitmap bitmap = bitmapCache.getBitmapFromMemoryCache(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            //再从磁盘缓存中取图片
            bitmap = bitmapCache.getBitmapFromDiskCache(url);
            if (bitmap != null) {
                bitmapCache.addBitmapToMemoryCache(url, bitmap);
                imageView.setImageBitmap(bitmap);
            } else {
                downLoadImage(url, new CallBack<Bitmap>() {

                    @Override
                    public void response(Bitmap entity) {
                        bitmapCache.addBitmapToMemoryCache(url, entity);
                        bitmapCache.addBitmapToDiskCache(url, entity);
                        imageView.setImageBitmap(entity);
                    }
                });
            }
        }
    }


    /**
     * 下载图片并缓存到内存和磁盘
     */
    private void downLoadImage(final String strurl, final CallBack callBack) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(strurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    ByteArrayOutputStream byteArrayOutputStream = null;
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int len = -1;
                        while ((len = bis.read(bytes)) != -1) {
                            byteArrayOutputStream.write(bytes, 0, len);
                        }
                        bis.close();
                        byteArrayOutputStream.close();
                        conn.disconnect();
                    }
                    Bitmap bitmap = null;
                    if (byteArrayOutputStream != null) {
                        bitmap = BitmapFactory.decodeByteArray(byteArrayOutputStream.toByteArray(), 0, byteArrayOutputStream.toByteArray().length);
                    }

                    final Bitmap tempBitmap = bitmap;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.response(tempBitmap);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                }
            }
        }).start();
    }

    public static void setImage(final ImageView iv, final String urlstr) {
        iv.setTag(urlstr);

    }


    //刷新磁盘缓存
    public void flush() {
        bitmapCache.flush();
    }

    //关闭磁盘缓存
    public void close() {
        bitmapCache.close();
    }


    /**
     * 位图重新采样
     */
    private Bitmap decodeSampledBitmapFromStream(byte[] bytes, int reqwidth, int reqheight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqwidth, reqheight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        //获取位图的原宽高
        int w = options.outWidth;
        int h = options.outHeight;
        int inSampleSize = 1;
        if (w > reqWidth || h > reqHeight) {
            if (w > h) {
                inSampleSize = Math.round((float) h / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) w / (float) reqWidth);
            }
        }
        System.out.println("inSampleSize=" + inSampleSize);
        return inSampleSize;
    }

}
