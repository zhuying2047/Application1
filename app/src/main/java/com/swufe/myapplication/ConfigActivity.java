package com.swufe.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class ConfigActivity extends AppCompatActivity {

    public final String TAG = "ConfigActivity";
    EditText dollarText;
    EditText euroText;
    EditText wonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        //获取从RateActivity传过来的参数 （传参）
        Intent intent = getIntent();
        //注意标签要相同dollar_rate_key defaultValue:0.0f 为默认值（接收参数）
        float dollar2 = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro2 = intent.getFloatExtra("euro_rate_key",0.0f);
        float won2 = intent.getFloatExtra("won_rate_key",0.0f);

        Log.i(TAG,"onCreate:dollar2=" + dollar2);
        Log.i(TAG,"onCreate:euro2=" + euro2);
        Log.i(TAG,"onCreate:won2=" + won2);

        dollarText = findViewById(R.id.dollar_rate);
        euroText = findViewById(R.id.euro_rate);
        wonText = findViewById(R.id.won_rate);

        //显示数据到控件
        dollarText.setText(String.valueOf(dollar2));
        euroText.setText(String.valueOf(euro2));
        wonText.setText(String.valueOf(won2));
    }

    public void save(View btn){
        Log.i("cfg","save: ");
        //获取新的值
        float newDollar = Float.parseFloat(dollarText.getText().toString());
        float newEuro = Float.parseFloat(euroText.getText().toString());
        float newWon = Float.parseFloat(wonText.getText().toString());

        Log.i(TAG,"save:获取到新的值 ");
        Log.i(TAG,"save:newDollar=" + newDollar);
        Log.i(TAG,"save:newEuro=" + newEuro);
        Log.i(TAG,"save:newWon=" + newWon);

        //保存到Bundle或放入到Extra
        Intent intent = getIntent();
           //将数据放到bundle(包)里面
        Bundle bdl = new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);

        setResult(2,intent);

        //结束当前页面，返回到调用界面（上一级页面）
        finish();
    }
}
