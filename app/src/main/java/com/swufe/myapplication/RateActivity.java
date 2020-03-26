package com.swufe.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RateActivity extends AppCompatActivity {

    EditText rmb;
    TextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showOut);
    }

    public void onClick(View btn){                 //作为按钮事件处理
        //获取用户输入内容
        String str = rmb.getText().toString();
        float r = 0;

        if(str.length()>0){
            r = Float.parseFloat(str);
        }else{
            //提示用户输入内容，经过一定时间会自动消失
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
        }

        float val;
        if(btn.getId()==R.id.btn_dollar){
            val = r * (1/6.7f);
        }else if(btn.getId()==R.id.btn_euro){
            val = r * (1/11f);
        }else{
            val = r * 500;
        }
        //结果保留两位小数
        show.setText(String.valueOf((float)(Math.round(val*100))/100));
    }

    public void openOne(View btn){
        //打开一个页面Activity
        Log.i("open","openOne:");
        Intent hello = new Intent(this,SecondActivity.class);
        startActivity(hello);
    }
}
