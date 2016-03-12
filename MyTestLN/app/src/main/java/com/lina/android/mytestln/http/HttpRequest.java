package com.lina.android.mytestln.http;

import android.os.AsyncTask;
import android.util.Log;

import com.lina.android.mytestln.bean.BaseBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/10.
 */
public class HttpRequest {
    public static interface CallBack{
        void callBack(ArrayList<BaseBean> list);
    }

    private static ArrayList<BaseBean> beanlist;

    public static void httpRequest(final String jsonUrl, final CallBack callBack){
        new AsyncTask<Void,Void,ArrayList<BaseBean>>(){
            @Override
            protected ArrayList<BaseBean> doInBackground(Void... params) {
                try {
                    URL httpUrl=new URL(jsonUrl);//创建url http地址
                    HttpURLConnection conn= (HttpURLConnection) httpUrl.openConnection();//打开http 链接

                    InputStream in=conn.getInputStream();//得到字节输入流
                    InputStreamReader isr=new InputStreamReader(in);//把字节流转为字符流
                    BufferedReader br=new BufferedReader(isr);

                    StringBuffer sb= new StringBuffer();//创建字符串容器
                    String str="";
                    while ((str=br.readLine())!=null){
                        sb.append(str);//读取完毕，添加到容器中
                    }
                    Log.e("TAG", sb.toString());//测试是否得到json字符串
                    beanlist =new ArrayList<BaseBean>();//创建本地对象的集合
                    JSONObject jsonObject=new JSONObject(sb.toString());
                    JSONArray jsonArray=jsonObject.getJSONArray("result");

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject2=jsonArray.getJSONObject(i);//获取jsonArray中的每个对象
                        BaseBean baseBean=new BaseBean();
                        baseBean.imageUrl=jsonObject2.getString("cover_addr");
                        baseBean.imageUser=jsonObject2.getString("icon");
                        baseBean.soundname=jsonObject2.getString("title");
                        beanlist.add(baseBean);
                    }

                } catch (JSONException e) {
                    Log.i("wzl","cuo wu 1");
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    Log.i("wzl","cuo wu 2");
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("wzl","cuo wu 3");
                    e.printStackTrace();
                }
                return beanlist;
            }

            @Override
            protected void onPostExecute(ArrayList<BaseBean> list) {
                if(callBack!=null){
                    callBack.callBack(list);
                }
            }
        }.execute();
    }

}
