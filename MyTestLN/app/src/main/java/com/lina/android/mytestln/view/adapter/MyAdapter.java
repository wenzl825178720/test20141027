package com.lina.android.mytestln.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lina.android.mytestln.R;
import com.lina.android.mytestln.bean.BaseBean;
import com.lina.android.mytestln.http.MyImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/10.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BaseBean> list = new ArrayList<BaseBean>();
    private DisplayImageOptions videoCoverOptions=new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.video_cover)
            .showImageOnFail(R.mipmap.video_cover)
            .showImageOnLoading(R.mipmap.video_cover)
            .build();

    DisplayImageOptions headIamgeOptions=new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .displayer(new BitmapDisplayer() {
                @Override
                public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
                    Bitmap bitmap1 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap1);
                    BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    Paint paint = new Paint();
                    paint.setShader(shader);
                    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getHeight() / 2, paint);
                    imageAware.setImageBitmap(bitmap1);
                }
            })
            .build();

    public MyAdapter(Context context, ArrayList<BaseBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return (list.size() / 2) + 1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image_layout, parent, false);
            vh.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            vh.userimage = (ImageView) convertView.findViewById(R.id.userimage);
            vh.songname = (TextView) convertView.findViewById(R.id.songName);
            vh.imageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
            vh.userimage2 = (ImageView) convertView.findViewById(R.id.userimage2);
            vh.songname2 = (TextView) convertView.findViewById(R.id.songName2);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        BaseBean beanL = list.get(position);
        vh.songname.setText(beanL.soundname);
        MyImageLoader.universalimageloader.displayImage(beanL.imageUrl, vh.imageView, videoCoverOptions);
        MyImageLoader.universalimageloader.displayImage(beanL.imageUser, vh.userimage, headIamgeOptions);

        if (position * 2 + 1 < list.size()) {
            BaseBean beanR = list.get(position * 2 + 1);
            vh.songname2.setText(beanR.soundname);
            MyImageLoader.universalimageloader.displayImage(beanR.imageUrl, vh.imageView2, videoCoverOptions);
            MyImageLoader.universalimageloader.displayImage(beanR.imageUser, vh.userimage2, headIamgeOptions);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        ImageView userimage;
        TextView songname;
        ImageView imageView2;
        ImageView userimage2;
        TextView songname2;
    }
}
