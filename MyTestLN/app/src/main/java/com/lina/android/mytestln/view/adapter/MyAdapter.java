package com.lina.android.mytestln.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lina.android.mytestln.R;
import com.lina.android.mytestln.bean.BaseBean;
import com.lina.android.mytestln.http.image.loader.LinaImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/10.
 */
public class MyAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BaseBean> list = new ArrayList<BaseBean>();

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
//        ImageLoader.setImage(vh.imageView, beanL.imageUrl);
//        ImageLoader.setImage(vh.userimage, beanL.imageUser);
        if (beanL.imageUrl != null) {
            LinaImageLoader.getInstance(context).loadBitmap(vh.imageView, beanL.imageUrl);
        }
        if (beanL.imageUser != null) {
            LinaImageLoader.getInstance(context).loadBitmap(vh.userimage, beanL.imageUser);
        }

        if (position * 2 + 1 < list.size()) {
            BaseBean beanR = list.get(position * 2 + 1);
            vh.songname2.setText(beanR.soundname);
//            ImageLoader.setImage(vh.imageView2, beanR.imageUrl);
//            ImageLoader.setImage(vh.userimage2, beanR.imageUser);
            if (beanR.imageUrl != null) {
                LinaImageLoader.getInstance(context).loadBitmap(vh.imageView2, beanR.imageUrl);
            }
            if (beanR.imageUser != null) {
                LinaImageLoader.getInstance(context).loadBitmap(vh.imageView2, beanR.imageUser);
            }
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
