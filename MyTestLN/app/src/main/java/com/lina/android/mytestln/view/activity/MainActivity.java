package com.lina.android.mytestln.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lina.android.mytestln.R;
import com.lina.android.mytestln.bean.BaseBean;
import com.lina.android.mytestln.http.MyAndroidAsyncHttp;
import com.lina.android.mytestln.http.MyVolley;
import com.lina.android.mytestln.view.adapter.MyAdapter;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String jsonUrl = "http://api.mimic.mobi/video/videoInfo/getHotVideoList?start=0&count=40";
    private PullToRefreshListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getListAndroidAsyncHttp();
        //getListVolley();
        listView= (PullToRefreshListView) findViewById(R.id.listView);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        listView.getLoadingLayoutProxy(true,false);

    }

    public void getListVolley() {
        MyVolley.requestQueue.add(new StringRequest(Request.Method.GET, jsonUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                ArrayList<BaseBean> beanlist = new ArrayList<BaseBean>();//创建本地对象的集合

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);//获取jsonArray中的每个对象
                        BaseBean baseBean = new BaseBean();
                        baseBean.imageUrl = jsonObject2.getString("cover_addr");
                        baseBean.imageUser = jsonObject2.getString("icon");
                        baseBean.soundname = jsonObject2.getString("title");
                        beanlist.add(baseBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MyAdapter adapter = new MyAdapter(MainActivity.this, beanlist);
                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "网络错误");
            }
        }));
    }

    public void getListAndroidAsyncHttp() {
        MyAndroidAsyncHttp.asyncHttpClient.get(jsonUrl, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "网络错误");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                ArrayList<BaseBean> beanlist = new ArrayList<BaseBean>();//创建本地对象的集合

                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);//获取jsonArray中的每个对象
                        BaseBean baseBean = new BaseBean();
                        baseBean.imageUrl = jsonObject2.getString("cover_addr");
                        baseBean.imageUser = jsonObject2.getString("icon");
                        baseBean.soundname = jsonObject2.getString("title");
                        beanlist.add(baseBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MyAdapter adapter = new MyAdapter(MainActivity.this, beanlist);
                listView.setAdapter(adapter);
            }
        });
    }

}
