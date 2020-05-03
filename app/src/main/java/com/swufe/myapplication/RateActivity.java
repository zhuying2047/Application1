package com.swufe.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RateActivity extends AppCompatActivity implements Runnable {

    private final String TAG ="Rate";
    private float dollarRate = 0.0f;
    private float euroRate = 0.0f;
    private float wonRate = 0.0f;
    private String updateDate = "";
    private int i;

    EditText rmb;
    TextView show;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        rmb = findViewById(R.id.rmb);
        show = findViewById(R.id.showOut);

        //获取SP里面保存的数据
        //getSharedPreferences(文件名,访问权限)  私有访问只有当前的app应用才能读取这个文件myrate
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);//0.0f默认值
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);
        updateDate = sharedPreferences.getString("update_date","");
        i=sharedPreferences.getInt("number",0);

        Log.i(TAG,"onCreate:sp dollarRate=" + dollarRate);
        Log.i(TAG,"onCreate:sp euroRate=" + euroRate);
        Log.i(TAG,"onCreate:sp wonRate=" + wonRate);
        Log.i(TAG,"onCreate:sp update_date=" + updateDate);
        Log.i(TAG,"更新次数："+i);

        //获取当前系统时间
//        Date currentTime = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String todayStr2 = formatter.format(currentTime);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr = sdf.format(today);

        //判断时间
        if(!todayStr.equals(updateDate)){
            //开启子线程
            Thread t = new Thread(this);   //this代表要运行当前对象RateActivity的run()方法
            t.start();                            //调用子线程，就会去调用RateActivity的run()方法

            //获取子线程发送的数据,对handle类方法的重写
            handler = new Handler() {

                public void handleMessage(Message msg) {
                    if(msg.what==5){
                        Bundle bdl =(Bundle) msg.obj;
                        dollarRate = bdl.getFloat("dollar-rate");
                        euroRate = bdl.getFloat("euro-rate");
                        wonRate = bdl.getFloat("won-rate");

                        //保存更新的汇率
                        SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putFloat("dollar_rate",dollarRate);
                        editor.putFloat("euro_rate",euroRate);
                        editor.putFloat("won_rate",wonRate);
                        editor.putString("update_date",todayStr);      //更新的时间
                        editor.putInt("number",i+1);
                        editor.apply();

                        Log.i(TAG,"handleMessage:dollarRate:"+dollarRate);
                        Log.i(TAG,"handleMessage:euroRate:"+euroRate);
                        Log.i(TAG,"handleMessage:wonRate:"+wonRate);
                        Log.i(TAG,"update date:"+todayStr);
                        Log.i(TAG,"更新次数"+(i+1));

                        Toast.makeText(RateActivity.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                    }
                    super.handleMessage(msg);
                }
            };
        }

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
        }else if(item.getItemId()==R.id.open_list){
            //打开列表窗口
            Intent list = new Intent(this,MyList2Activity.class);
            startActivity(list);
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

            //将新设置的汇率写到SP里
            SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.commit();   //保存数据（一定不要忘了）

            Log.i(TAG,"onActivityResult:数据已保存到sharedPreferences");


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    //子线程中需要运行的代码
    public void run() {
        for(int i=1;i<6;i++){
            Log.i(TAG,"run: i=" + i);
            //线程停止一秒钟
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

//        //获取Msg对象，用于返回主线程
//        Message msg = handler.obtainMessage(5);    //msg.what = 5;
//        //将需要返回到主线程的数据放到msg.obj中带到主线程
//        msg.obj = "Hello from run()";
//        handler.sendMessage(msg);

        //用于保存获取的汇率
        Bundle bundle;

//        URL url = null;
//        try {
//            url = new URL("http://www.usd-cny.com/bankofchina.htm");
//            HttpURLConnection http = (HttpURLConnection)url.openConnection();
//            InputStream in = http.getInputStream();
//
//            String html = inputStream2String(in);
//            Log.i(TAG,"run:html="+html);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //直接根据网络地址转成document对象，然后从document对象中提取数据
        bundle = getFromBOC();

        //通过message对象带回
        Message msg = handler.obtainMessage(5);    //msg.what = 5;
        //将需要返回到主线程的数据放到msg.obj中带到主线程
        msg.obj = bundle;
        handler.sendMessage(msg);
    }
    /**
     *从bankofchina获取数据
     *return
     */
    private Bundle getFromBOC() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.bankofchina.com/sourcedb/whpj/").get();
            Log.i(TAG,"run: " + doc.title());
            //获取doc中标签是"table"的源码
            Elements tables = doc.getElementsByTag("table");
//            for(Element table : tables){
//                Log.i(TAG,"run: table["+i+"]=" + table);
//                i++;
//            }
            //只有1个table，索引为0
            Element table1 = tables.get(1);
            Log.i(TAG,"run: table1="+table1);
            //获取td中的数据
            Elements tds = table1.getElementsByTag("td");
            //每一个币种有六个元素
            for(int i=0;i<tds.size();i+=8){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                Log.i(TAG,"run: text=" + td1.text());
                Log.i(TAG,"run:" + td1.text() + "==>" +td2.text());
                String str1 = td1.text();
                String val = td2.text();

                //bundle中保存所获取的汇率
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate", 100f / Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate", 100f / Float.parseFloat(val));
                }else if("韩国元".equals(str1)){
                    bundle.putFloat("won-rate", 100f / Float.parseFloat(val));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    private Bundle getFromUsdCny() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG,"run: " + doc.title());
            //获取doc中标签是"table"的源码
            Elements tables = doc.getElementsByTag("table");
//            for(Element table : tables){
//                Log.i(TAG,"run: table["+i+"]=" + table);
//                i++;
//            }
            //只有1个table，索引为0
            Element table1 = tables.get(0);
            Log.i(TAG,"run: table1="+table1);
            //获取td中的数据
            Elements tds = table1.getElementsByTag("td");
            //每一个币种有六个元素
            for(int i=0;i<tds.size();i+=6){
                Element td1 = tds.get(i);
                Element td2 = tds.get(i+5);
                Log.i(TAG,"run: text=" + td1.text());
                Log.i(TAG,"run:" + td1.text() + "==>" +td2.text());
                String str1 = td1.text();
                String val = td2.text();

                //bundle中保存所获取的汇率
                if("美元".equals(str1)){
                    bundle.putFloat("dollar-rate", 100f / Float.parseFloat(val));
                }else if("欧元".equals(str1)){
                    bundle.putFloat("euro-rate", 100f / Float.parseFloat(val));
                }else if("韩元".equals(str1)){
                    bundle.putFloat("won-rate", 100f / Float.parseFloat(val));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }
}
