package com.swufe.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{

    private final String TAG ="RateList";
    String data[] = {"one","two","three"};
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);
        // ListActivity作为父类，已经包含一个布局，所以不需要用activity_rate_list布局

        List<String> list1 = new ArrayList<String>();
        for(int i=1;i<100;i++){
            list1.add("item" + i);
        }

        //创建一个adapter（适配器）对象，控制数据和控件之间关系的，中间桥梁作用，处理Data和ListView之间的关系
        //把list1里面的数据放到安卓提供的列表中
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
        //setListAdapter()方法是在ListActivity中设置好的，可以直接使用，作用是把当前界面用adapter来管理
        setListAdapter(adapter);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==7){
                    List<String> list2 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        //获取网络数据，放入list带回到主线程中
        List<String> retList = new ArrayList<String>();

        Document doc = null;
        try {
            Thread.sleep(3000);
            doc = Jsoup.connect("https://www.bankofchina.com/sourcedb/whpj/").get();
            Log.i(TAG,"run: " + doc.title());
            //获取doc中标签是"table"的源码
            Elements tables = doc.getElementsByTag("table");
            Element table1 = tables.get(1);
            Log.i(TAG,"run: table1="+table1);

            //获取td中的数据
            Elements tds = table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);

                String str1 = td1.text();
                String val = td2.text();

                Log.i(TAG,"run:" + str1 + "==>" +val);
                //把值放到retlist中
                retList.add(str1 + "==>" + val);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //将retList通过message对象带回到主线程
        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);
    }
}
