package com.swufe.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    List<String> data = new ArrayList<String>();
    private String TAG = "MyList";
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        ListView listView = findViewById(R.id.mylist);
        //初始化数据
        for(int i=0;i<10;i++){
            data.add("item"+i);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        //自己定义的列表来设置adapter对象
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.nodata));    //列表里没有数据时显示id为nodata的控件
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> listv, View view, int position, long id) {

        Log.i(TAG,"onItemClick:position= "+position);
        Log.i(TAG,"onItemClick:parent= "+listv);

        //通过adapter移除被点击的列表元素
        adapter.remove(listv.getItemAtPosition(position));
        //通知适配器刷新列表，但remove方法自动刷新，这里就可以省略
        // adapter.notifyDataSetChanged();
    }
}
