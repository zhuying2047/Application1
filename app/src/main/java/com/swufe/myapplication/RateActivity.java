package com.swufe.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RateActivity extends AppCompatActivity {

    private final String TAG ="Rate";
    private float dollarRate = 0.1f;
    private float euroRate = 0.2f;
    private float wonRate = 0.3f;

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
        //计算
        if(btn.getId()==R.id.btn_dollar){
            show.setText(String.format("%.2f",r*dollarRate));//保留两位小数
        }else if(btn.getId()==R.id.btn_euro){
            show.setText(String.format("%.2f",r*euroRate));
        }else{
            show.setText(String.format("%.2f",r*wonRate));;
        }
    }

    public void openOne(View btn){
        openConfig();
    }

    private void openConfig() {
        //打开一个页面Activity
        Intent config = new Intent(this, ConfigActivity.class);
        //将这三个参数传递到ConfigActivity.class（也可以打包成bundle传递）
        config.putExtra("dollar_rate_key", dollarRate);
        config.putExtra("euro_rate_key", euroRate);
        config.putExtra("won_rate_key", wonRate);

        //用于调试
        Log.i(TAG, "openOne:dollar_rate_key=" + dollarRate);
        Log.i(TAG, "openOne:euro_rate_key=" + euroRate);
        Log.i(TAG, "openOne:won_rate_key=" + wonRate);

        //startActivity(config);
        startActivityForResult(config, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        //返回的数据类型是boolean类型的，true当前Activity有菜单项，faulse为没有菜单项
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_set){
            openConfig();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //接受返回来的数据
    //三个参数 requestCode:打开一个窗口需要请求参数，每打开一个窗口就要一个新的请求参数，
    //                    用来确定打开的是哪个窗口
    //         resultCode：返回参数，用来区分不同的数据
    //        利用这两个参数来唯一确定返回的是什么数据
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 2) {
            /*
            *bdl.putFloat("key_dollar",newDollar);
             bdl.putFloat("key_euro",newEuro);
             bdl.putFloat("key_won",newWon);   */
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.1f);
            euroRate = bundle.getFloat("key_euro",0.1f);
            wonRate = bundle.getFloat("key_won",0.1f);
            Log.i(TAG,"onActivityResult: dollarRate="+dollarRate);
            Log.i(TAG,"onActivityResult: euroRate="+euroRate);
            Log.i(TAG,"onActivityResult: wonRate="+wonRate);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
