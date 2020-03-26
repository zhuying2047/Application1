package com.swufe.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {

    TextView out;
    EditText inp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        out = findViewById(R.id.showText);        //获取id为showText的控件对象
        inp=findViewById(R.id.inpText);

        Button btn = findViewById(R.id.btn1);          //按钮需要处理事件，需要有监听器

        btn.setOnClickListener(new View.OnClickListener() {     //另一种实现方式（利用匿名类）——方法二
           @Override
            public void onClick(View v) {
                Log.i("mail","onClick called......");
                String str = inp.getText().toString();

                out.setText("Hello "+str);
            }
        });
    }

   // public void btnOnclick(View btn){                   //方法三，在activity_main中设置（最简单）
     //   Log.i("click","btnClick called......");
    //}
}
