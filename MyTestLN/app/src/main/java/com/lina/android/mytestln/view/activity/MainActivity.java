package com.lina.android.mytestln.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.lina.android.mytestln.http.HttpRequest;
import com.lina.android.mytestln.view.adapter.MyAdapter;
import com.lina.android.mytestln.R;
import com.lina.android.mytestln.bean.BaseBean;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HttpRequest.CallBack {

    private ListView listView;
    private String jsonUrl = "http://api.mimic.mobi/video/videoInfo/getHotVideoList?start=0&count=40";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        HttpRequest.httpRequest(jsonUrl, this);
    }

    @Override
    public void callBack(ArrayList<BaseBean> list) {
        MyAdapter adapter = new MyAdapter(MainActivity.this, list);
        listView.setAdapter(adapter);
    }
}
