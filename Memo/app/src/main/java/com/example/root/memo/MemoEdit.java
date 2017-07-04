package com.example.root.memo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.root.memo.Adapter.MemosAdapter;
import com.example.root.memo.Data.Memos;

import org.w3c.dom.Text;

public class MemoEdit extends AppCompatActivity {
    private TextView cancleBtn;
    private TextView saveBtn;
    private EditText titleText;
    private EditText editText;
    private Bundle bundle;
    private static final int SHOWCONTENT = 56;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        cancleBtn=(TextView)findViewById(R.id.cancleBtn);
        saveBtn=(TextView)findViewById(R.id.saveBtn);
        Log.d("MemoEdit",cancleBtn.getText().toString());
        titleText=(EditText)findViewById(R.id.titleText);
        editText=(EditText)findViewById(R.id.contentText);

        bundle=getIntent().getExtras();
        init();
    }

    private void init(){
        final int id =bundle.getInt("id");
        Log.d("MemoEdit","id: "+id);
        if(id!=-1){
            titleText.setText(bundle.getString("title"));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    content= Memos.getInstance().getContentById(id);
                    Message msg=Message.obtain();
                    msg.what=SHOWCONTENT;
                    myHandler.sendMessage(msg);
                }
            }).start();
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("memoEdit","save");
                final int id = bundle.getInt("id");
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                if(id==-1){
                    bundle.putInt("id",Memos.getInstance().insertMemoItem(titleText.getText().toString(),editText.getText().toString()));
                    bundle.putString("title",titleText.getText().toString());
                    bundle.putBoolean("isNew",true);
                }
                else{
                    bundle.putInt("id",id);
                    bundle.putString("title",titleText.getText().toString());
                    bundle.putBoolean("isNew",false);
                    Memos.getInstance().updateMemoItem(id,titleText.getText().toString(),editText.getText().toString());
                }
                intent.putExtras(bundle);
                setResult(0,intent);
                finish();
            }
        });

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("memoEdit","cancle");
                setResult(1,new Intent());
                finish();
            }
        });
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOWCONTENT:
                    editText.setText(content);
            }
            super.handleMessage(msg);
        }
    };
}
