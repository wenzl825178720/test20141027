package com.lina.android.mytestln.http.image.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 不带缓存的图片下载
 * Created by Administrator on 2016/3/10.
 */
public class ImageLoader {

    public static void setImage2(final ImageView iv, final String urlstr) {
        iv.setTag(urlstr);
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                InputStream in = null;
                try {
                    URL url = new URL(urlstr);
                    in = url.openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    return bitmap;
                } catch (MalformedURLException e) {
                    Log.i("ImageLoader", "1");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("ImageLoader", "2");
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                Log.i("ImageLoader", "3");
                if (iv.getTag().equals(urlstr)) {
                    iv.setImageBitmap(bitmap);
                }
            }
        }.execute();

    }

    public static Handler handler = new Handler();

    public static void setImage(final ImageView iv, final String urlstr) {
        iv.setTag(urlstr);
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream in = null;
                try {
                    URL url = new URL(urlstr);
                    in = url.openStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(in);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (iv.getTag().equals(urlstr)) {
                                iv.setImageBitmap(bitmap);
                            }
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (in != null) {
                            in.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
