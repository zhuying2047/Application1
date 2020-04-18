package com.swufe.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private final String TAG = "SecondActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String scorea = ((TextView)findViewById(R.id.score)).getText().toString();
        String scoreb = ((TextView)findViewById(R.id.score2)).getText().toString();

        Log.i(TAG,"onSaveInstanceState: ");
        outState.putString("teama_score",scorea);
        outState.putString("teamb_score",scoreb);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String scorea = savedInstanceState.getString("teama_score");
        String scoreb = savedInstanceState.getString("teamb_score");

        Log.i(TAG,"onRestoreInstanceState: ");
        ((TextView)findViewById(R.id.score)).setText(scorea);
        ((TextView)findViewById(R.id.score2)).setText(scoreb);
    }

    public void btnAdd1(View btn){
        if(btn.getId()==R.id.btn_1){
            showScore(1);
        }else{
            showScore2(1);
        }
    }

    public void btnAdd2(View btn){
        if(btn.getId()==R.id.btn_2){
            showScore(2);
        }else{
            showScore2(2);
        }
    }

    public void btnAdd3(View btn){
        if(btn.getId()==R.id.btn_3){
            showScore(3);
        }else{
            showScore2(3);
        }
    }

    public void btnReset(View btn){
        ((TextView)findViewById(R.id.score)).setText("0");
        ((TextView)findViewById(R.id.score2)).setText("0");
    }

    private void showScore(int inc){
        Log.i("show","inc=" + inc);
        TextView out = findViewById(R.id.score);
        String oldScore = (String) out.getText();
        int newScore = Integer.parseInt(oldScore) + inc;
        out.setText("" + newScore);
    }
    private void showScore2(int inc){
        Log.i("show","inc=" + inc);
        TextView out = findViewById(R.id.score2);
        String oldScore = (String) out.getText();
        int newScore = Integer.parseInt(oldScore) + inc;
        out.setText("" + newScore);
    }
}
