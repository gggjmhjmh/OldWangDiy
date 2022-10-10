package com.oldwang.librarymodule.diy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oldwang.librarymodule.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部列表弹框
 */
// TODO: 2022/10/10 待优化：数据可以传入Object

public class BottomListDialog extends Dialog {
    Context mContext;

    private ListView listView;
    private TextView mTitle;


    private String title;
    private List<String> dataList;
    private DialogListAdapter adapter;


    public BottomListDialog(Context context) {
        super(context, R.style.BottomDialog);
        mContext = context;
        setContentView(R.layout.dialog_bottom_list);
        Window window = this.getWindow();
        //设置位置在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置弹入弹出动画
        window.setWindowAnimations(R.style.BottomDialog_Animation);
        //设置为全屏dialog
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);

        //        setCancelable(false);
//        setCanceledOnTouchOutside(true);

        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        listView = findViewById(R.id.listView);
        mTitle = findViewById(R.id.title);

        adapter = new DialogListAdapter(mContext);
        listView.setAdapter(adapter);

    }

    public void setData(String title, List<String> dataList, int selectIndex) {
        this.title = title;
        this.dataList = dataList;

        mTitle.setText(title);
        adapter.setList(dataList);
        adapter.setSelcetIndex(selectIndex);
        adapter.notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        listView.setOnItemClickListener(itemClickListener);
    }

    public void setSelectIndex(int selectIndex) {
        adapter.setSelcetIndex(selectIndex);
        adapter.notifyDataSetChanged();
    }


    class DialogListAdapter extends BaseAdapter {

        private List<String> list = new ArrayList<>();
        private int selcetIndex = -1; //选中下标

        private LayoutInflater inflater;
        private Context mContext;

        public DialogListAdapter(Context context) {
            this.mContext = context;
            inflater = LayoutInflater.from(mContext);
        }

        public void setList(List<String> list) {
            this.list = list;
        }

        public void setSelcetIndex(int selcetIndex) {
            this.selcetIndex = selcetIndex;
        }

        @Override
        public int getCount() {
            return list.size();  //数据源的长度
        }

        @Override
        public Object getItem(int position) {
            return list.get(position); //返回数据源的其中某一个对象
        }

        @Override
        public long getItemId(int position) {
            return position; //返回adapter中的其中一个项的id
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder hv;
            if (null == convertView) {
                hv = new MyViewHolder();
                convertView = inflater.inflate(R.layout.item_dialog_bottom_list, null, false);
                hv.itemView = convertView.findViewById(R.id.itemView);
                hv.iv = convertView.findViewById(R.id.iv);
                hv.tv = convertView.findViewById(R.id.tv);
                convertView.setTag(hv);
            } else {
                hv = (MyViewHolder) convertView.getTag();
            }
            //一定要判刑断下数据源是否为空，否则很大几率就crash了
            if (list != null && !list.isEmpty()) {
                hv.iv.setVisibility(position == selcetIndex ? View.VISIBLE : View.INVISIBLE);
                hv.tv.setText(list.get(position));
            }

            //给个回调，让调用都者可以自定义条目的显示
            if (getViewCallback != null) {
                getViewCallback.getView(hv, position, list.get(position));
            }

            return convertView;
        }

        class MyViewHolder {
            View itemView;
            ImageView iv;
            TextView tv;
        }

    }


    private adapterGetViewCallback getViewCallback;

    public void setGetViewCallback(adapterGetViewCallback getViewCallback) {
        this.getViewCallback = getViewCallback;
    }

    interface adapterGetViewCallback<T> {
        void getView(DialogListAdapter.MyViewHolder viewHolder, int position, T item);
    }

}
