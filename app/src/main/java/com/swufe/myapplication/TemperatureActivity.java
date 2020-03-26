package com.swufe.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TemperatureActivity extends AppCompatActivity {

    TextView out;
    EditText inp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        out = findViewById(R.id.output);
        inp = findViewById(R.id.inp);

    }
    @SuppressLint("SetTextI18n")

    public void btnConvert1(View btn){
        String str = inp.getText().toString();
        double temF = Double.parseDouble(str);
        double newtemC = (temF - 32) * 5 / 9;
        out.setText(str+"华氏度转换为摄氏度为"+newtemC);
        Log.i("mail","onClick called......");
    }

    public void btnConvert2(View btn){
        String str = inp.getText().toString();
        double temC = Double.parseDouble(str);
        double newtemF = (temC*9/5)+32;
        out.setText(str + "摄氏度转换为华氏度为" + newtemF);
        Log.i("mail11！","onClick called......");
    }
}
