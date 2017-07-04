package com.example.root.memo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.root.memo.Adapter.MemosAdapter;
import com.example.root.memo.Data.Memos;
import com.example.root.memo.Model.MemoItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView addMemo;
    private TextView editMemo;
    private ListView memoList;
    private boolean isEdit=false;
    private ArrayList<MemoItem> memos;
    private MemosAdapter adapter;

    private static final int INITLIST = 1;
    private static final int APPENDITEM = 2;
    private static final int REMOVEITEM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addMemo=(TextView)findViewById(R.id.addBtn);
        editMemo=(TextView)findViewById(R.id.editBtn);
        memoList=(ListView)findViewById(R.id.memoList);

        init();
    }

    private void init(){
        addMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MemoMain","add");
                Intent intent=new Intent(MainActivity.this,MemoEdit.class);
                Bundle bundle=new Bundle();
                bundle.putInt("id",-1);
                bundle.putString("title",null);
                intent.putExtras(bundle);
                startActivityForResult(intent,0);
            }
        });

        editMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MemoMain","edit");
                if(adapter==null) return;

                if(isEdit){
                    editMemo.setText("编辑");
                    adapter.setEditState(false);
                    isEdit=false;
                }
                else{
                    editMemo.setText("完成");
                    adapter.setEditState(true);
                    isEdit=true;
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                Memos.getInstance().initData(getApplicationContext());
                memos=Memos.getInstance().getMemoTitle();
                Message msg=Message.obtain();
                msg.what=INITLIST;
                myHandler.sendMessage(msg);
            }
        }).start();
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INITLIST:
                    adapter=new MemosAdapter(MainActivity.this,memos);
                    memoList.setAdapter(adapter);
                    memoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent=new Intent(MainActivity.this,MemoEdit.class);
                            Bundle bundle=new Bundle();
                            bundle.putInt("id",(int)l);
                            Log.d("MemoMain",i+"");
                            bundle.putString("title",adapter.getItem(i).getTitle());
                            intent.putExtras(bundle);
                            startActivityForResult(intent,0);
                        }
                    });
                    break;
                case APPENDITEM:
                    break;
                case REMOVEITEM:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            Bundle bundle=data.getExtras();
            int id=bundle.getInt("id");
            String title=bundle.getString("title");
            boolean isNew=bundle.getBoolean("isNew");

            if(isNew){
                MemoItem item=new MemoItem();
                item.setId(id);
                item.setTitle(title);
                memos.add(item);
            }
            else{
                for(int i=0;i<memos.size();i++){
                    if(memos.get(i).getId()==id){
                        memos.get(i).setTitle(title);
                        break;
                    }
                }
            }

            adapter.notifyDataSetChanged();
        }
    }
}
