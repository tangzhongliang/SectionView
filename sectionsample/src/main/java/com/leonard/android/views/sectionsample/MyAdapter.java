package com.leonard.android.views.sectionsample;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C) 2013-2016 RICOH Co.,LTD
 * All rights reserved
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context mContext;
    List<String> datas;

    enum ItemTypeEnum {
        START(1),
        NORMAL(0);

        private final int value;

        ItemTypeEnum(int i) {
            this.value = i;
        }
    }

    public MyAdapter(Context mContext, List<String> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        if (datas.get(position).startsWith("start")) {
            return ItemTypeEnum.START.value;
        }
        return ItemTypeEnum.NORMAL.value;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.i("Section", "bind position" + position);
        holder.textView.setText((String) getItem(position));
        holder.textView.setOnClickListener(holder);
        final View itemView = holder.itemView;
        if (getItemViewType(position)==0){
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }else {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.startDrag(ClipData.newPlainText("label", "text"), new View.DragShadowBuilder(v) {
                    @Override
                    public void onDrawShadow(Canvas canvas) {
                        canvas.drawColor(Color.RED);
                        super.onDrawShadow(canvas);
                    }
                }, holder, 0);
                return true;
            }
        });
        itemView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int srcPosition = ((ViewHolder) event.getLocalState()).getAdapterPosition();
                int destPosition = holder.getAdapterPosition();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.i("aaa", "src" + srcPosition);
                        Log.i("aaa", "ACTION_DRAG_ENTERED" + destPosition);

                        break;
                    case DragEvent.ACTION_DROP:
                        switchItem(srcPosition, destPosition);
                        notifyItemMoved(srcPosition, destPosition);
                        break;
                }
                return true;
            }
        });
    }

    private void switchItem(int srcPosition, int destPosition) {
        String[] objects = datas.toArray(new String[datas.size()]);
        if (srcPosition > destPosition) {
            String temp = objects[srcPosition];
            int i;
            for (i = srcPosition; i > destPosition; i--) {
                objects[i] = objects[i - 1];
            }
            objects[i] = temp;
        } else if (srcPosition < destPosition) {
            String temp = objects[srcPosition];
            int i;
            for (i = srcPosition; i < destPosition; i++) {
                objects[i] = objects[i + 1];
            }
            objects[i] = temp;
        }
        datas = Arrays.asList(objects);
    }

    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView textView;


        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
