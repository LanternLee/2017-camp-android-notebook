package com.example.root.memo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.root.memo.Data.Memos;
import com.example.root.memo.Model.*;
import com.example.root.memo.R;

import java.util.List;

/**
 * Created by root on 2017/7/4.
 */

public class MemosAdapter extends BaseAdapter {
    private List<MemoItem> memos = null;

    private Context mContext = null;

    private boolean isEdit=false;

    public MemosAdapter(Context context, List<MemoItem> memoItems) {
        mContext = context;
        memos = memoItems;
    }

    public void setEditState(boolean state){
        isEdit=state;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int rnt = 0;
        if (memos != null) {
            rnt = memos.size();
        }
        return rnt;
    }

    @Override
    public MemoItem getItem(int position) {
        MemoItem item = null;
        if (memos != null) {
            item = memos.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        int id = -1;
        if (memos != null) {
            id = memos.get(position).getId();
        }
        return id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.memo_item, null);

            holder.deleteBtn = (Button) convertView.findViewById(R.id.deleteBtn);
            holder.title = (TextView) convertView.findViewById(R.id.memoTitle);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MemoItem item = getItem(position);
        if (null != item) {
            holder.title.setText(item.getTitle());
            if(isEdit){
                holder.deleteBtn.setVisibility(View.VISIBLE);
            }
            else{
                holder.deleteBtn.setVisibility(View.INVISIBLE);
            }
            holder.id=item.getId();
            holder.position=position;

            final int id=item.getId();
            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Memos.getInstance().deleteMemoItem(id);
                        }
                    }).start();

                    memos.remove(position);
                    notifyDataSetChanged();
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        Button deleteBtn;
        TextView title;
        int id;
        int position;
    }
}
