package com.example.root.memo.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.root.memo.Model.*;

import java.util.ArrayList;

/**
 * Created by root on 2017/7/4.
 */

public class Memos {
    private static Memos memos;

    private SQLiteDatabase db;

    static public Memos getInstance(){
        if(memos==null){
            memos=new Memos();
        }

        return memos;
    }

    private Memos(){

    }

    public void initData(Context context){
        db=context.openOrCreateDatabase("test.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists memos( _id Integer primary key autoincrement , title CHARACTER(20) , content Text)");
    }

    public ArrayList<MemoItem> getMemoTitle(){
        ArrayList<MemoItem> memoItems=new ArrayList<MemoItem>();

        if(db!=null){
            Cursor cursor = db.query("memos", new String[]{"_id","title"}, null, null, null, null, null);

            if(cursor.moveToFirst()){
                do{
                    MemoItem item=new MemoItem();
                    item.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    item.setTitle(cursor.getString(cursor.getColumnIndex("title")));

                    memoItems.add(item);
                }
                while(cursor.moveToNext());
            }
        }

        return memoItems;
    }

    public String getContentById(int id){
        String ret=null;

        if(db!=null){
            Cursor cursor=db.query("memos", new String[]{"content"}, "_id = "+id, null, null, null, null);

            if(cursor.moveToFirst()){
                ret=cursor.getString(cursor.getColumnIndex("content"));
            }
        }

        return ret;
    }

    public int insertMemoItem(String title,String content){
        ContentValues cv=new ContentValues();
        cv.put("title",title);
        cv.put("content",content);
        db.insert("memos",null,cv);

        Cursor cursor = db.rawQuery("select last_insert_rowid() from memos",null);
        int id=-1;
        if(cursor.moveToFirst()){
            id=cursor.getInt(0);
        }

        return id;
    }

    public void deleteMemoItem(int id){
        db.delete("memos","_id=?",new String[]{String.valueOf(id)});
    }

    public void updateMemoItem(int id,String title,String content){
        ContentValues cv=new ContentValues();
        cv.put("title",title);
        cv.put("content",content);

        db.update("memos",cv,"_id=?",new String[]{String.valueOf(id)});
    }
}
