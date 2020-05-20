package com.example.module1.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FirstFragmentAdapter extends RecyclerView.Adapter<FirstFragmentAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> list;

    private FirstFragmentAdapter() {
    }

    public FirstFragmentAdapter(Context context, ArrayList<String> strList) {
        mContext = context;
        list = strList;
    }

    @NonNull
    @Override
    public FirstFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new TextView(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull FirstFragmentAdapter.ViewHolder holder, int position) {
        holder.setValue(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }

        public void setValue(String string) {
            tv.setText(string);
        }
    }
}

