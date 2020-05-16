package com.swufe.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements Runnable {

    private String TAG = "search";

    Handler handler;
    EditText inp;
    ListView show;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        inp = findViewById(R.id.inp3);
        show = findViewById(R.id.show3);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what==7){
                    List<String> list2 = (List<String>) msg.obj;

                }
                super.handleMessage(msg);
            }
        };

        show.getOnItemLongClickListener();
    }

    //用按钮控制关键词的比较
    public void onClick(View btn){
        String str1 = String.valueOf(inp.getText());
        List<String> list3 = new ArrayList<String>();

        if(str1.length()>0){
            for(int i=0;i<list.size();i++){
                String str2 = list.get(i);
                if(!(str2.indexOf(str1) ==-1)){
                    list3.add(str2);
                }
            }
            if(list3.size()>0){
                ListAdapter adapter = new ArrayAdapter<String>
                        (SearchActivity.this,android.R.layout.simple_list_item_1,list3);
                show.setAdapter(adapter);
            }else{
                Toast.makeText(this,"没有符合关键词的结果",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this,"请输入内容",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void run() {
        Document doc = null;
        list = new ArrayList<String>(); //用于储存数据
        try {
            doc = Jsoup.connect("http://www.swufe.edu.cn/index/tzgg/3.htm").get();

            Elements uls = doc.getElementsByTag("ul");
            int i = 0;
            //查找出来第8个ul为所需要的
            Element ul1 = uls.get(8);
            Log.i(TAG, "run: ul1=" + doc.html());

            //获取li中的数据，并加到list中
            Elements lis = doc.getElementsByTag("li");
            int j = 0;
            for (Element li : lis) {
                j++;
                list.add(li.text());
                Log.i(TAG, "run: li[" + j + "]=" + li.text());
            }
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        //将list通过message对象带回到主线程
        Message msg = handler.obtainMessage(7);
        msg.obj = list;
        handler.sendMessage(msg);
    }
}
