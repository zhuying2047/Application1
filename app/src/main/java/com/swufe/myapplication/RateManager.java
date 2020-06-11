package com.swufe.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/*
通过这个类对数据库进行管理
 */
public class RateManager {

    private DBHelper dbHelper;
    private String TBNAME;

    public RateManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }

    /*
    *添加一行数据
     */
    public void add(RateItem item){
        //获得数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //构造一个ContentValues对象
        ContentValues values = new ContentValues();
        //把数据放到列名为“curname”的列中
        values.put("curname",item.getCurName());
        values.put("currate",item.getCurRate());
        db.insert(TBNAME,null,values);
        db.close();
    }

    /*
    *添加多行数据
     */
    public void addAll(List<RateItem> list){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for(RateItem item : list){
            ContentValues values = new ContentValues();
            values.put("curname",item.getCurName());
            values.put("currate",item.getCurRate());
            db.insert(TBNAME,null,values);
        }
        db.close();
    }

    /*
    *删除所有数据
     */
    public void deleteAll(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME,null,null);
        db.close();
    }

    /*
     *显示所有数据
     */
    public List<RateItem> listAll(){
        List<RateItem> rateList = null;
        //获取只读数据库
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //查询表里的所有数据
        Cursor cursor = db.query(TBNAME,null,null,null,null,null,null);
        //当表不为空时，将数据装载到列表rateList中
        if(cursor!=null){
            rateList = new ArrayList<RateItem>();
            //将光标移到下一行，一开始光标指向列名那一行
            while(cursor.moveToNext()){
                //rateItem是行数据对象，将行数据存到rateList中
                RateItem item = new RateItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));
                item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
                rateList.add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList;
    }
}
