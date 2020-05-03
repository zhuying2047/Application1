package com.swufe.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable, AdapterView.OnItemClickListener {


    private String TAG ="mylist2";
    Handler handler;
    private ArrayList<HashMap<String,String>> listItems; //存放文字，图片信息
    private SimpleAdapter listItemAdapter;   //适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initListView();
        this.setListAdapter(listItemAdapter);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==7){
                    List<HashMap<String,String>> list2 = (List<HashMap<String, String>>) msg.obj;
                    listItemAdapter = new SimpleAdapter(MyList2Activity.this,list2,
                            R.layout.list_item,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail}
                    );
                    setListAdapter(listItemAdapter);
                }
                super.handleMessage(msg);
            }
        };

        /*获取listView对象，返回是数据就是当前点击的列表控件，当列表项被按下的时候，有个监听在onItemClick
        *在onItemClick方法中设置具体实现
        */
        getListView().setOnItemClickListener(this);
    }

    private void initListView(){
        listItems = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<10;i++){
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("ItemTitle","Rate: "+i);   //标题文字
            map.put("ItemDetail","detail"+i);  //详情描述
            //把map放到ArrayList listItems中
            listItems.add(map);
        }

        //生成适配器的Item和动态数组对应的元素 参数解释（生命周期，数据源，布局，数据map里的的key，布局的id 两个一一匹配）
        listItemAdapter = new SimpleAdapter(this,listItems,
                R.layout.list_item,
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail}
                );

    }

    @Override
    public void run() {
        //获取网络数据，放入list带回到主线程中
        List<HashMap<String,String>> retList = new ArrayList<HashMap<String, String>>();

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
                HashMap<String,String> map = new HashMap<String,String>();
                map.put("ItemTitle",str1);
                map.put("ItemDetail",val);
                retList.add(map);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        //将retList通过message对象带回到主线程
        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.i(TAG,"onItemClick: parent="+parent);
        Log.i(TAG,"onItemClick: parent="+view);
        Log.i(TAG,"onItemClick: parent="+position);
        Log.i(TAG,"onItemClick: parent="+id);

        //从对应位置的数据项中提取数据
        //第一种：从map中获得
        HashMap<String,String> map = (HashMap<String, String>) getListView().getItemAtPosition(position);
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG,"onItemClick: titleStr="+titleStr);
        Log.i(TAG,"onItemClick: detailStr="+detailStr);

        //第二种：从控件中获得
        TextView title = view.findViewById(R.id.itemTitle);
        TextView detail = view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG,"onItemClick: title2="+title2);
        Log.i(TAG,"onItemClick: detail2="+detail2);

        //打开新的页面，传入参数
        Intent rateCalc = new Intent(this,RateCalActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.valueOf(detailStr));
        startActivity(rateCalc);
    }
}
