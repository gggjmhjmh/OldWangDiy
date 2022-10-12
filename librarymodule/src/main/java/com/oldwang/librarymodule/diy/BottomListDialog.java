package com.oldwang.librarymodule.diy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 底部列表弹框
 */

public class BottomListDialog extends Dialog {
    private Context mContext;

    private ListView listView;
    private TextView mTitle;

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

    /**
     * 设置数据列表
     *
     * @param title
     * @param dataList
     */

    public BottomListDialog setData(String title, List<String> dataList) {
        return setData(title, dataList, null);
    }

    /**
     * 设置数据列表
     *
     * @param title
     * @param dataList
     * @param memberName 成员变量名称,取该成员的值作为显示文本
     */
    public BottomListDialog setDataObj(String title, List<Object> dataList, String memberName) {
        return setData(title, dataList, memberName);
    }

    /**
     * 设置数据列表
     *
     * @param title
     * @param dataList 实现ItemShowText接口的对象，通过实现itemShowText函数返回要显示的文本
     */
    public BottomListDialog setDataObj(String title, List<ItemShowText> dataList) {
        return setData(title, dataList, null);
    }

    /**
     * 设置数据列表
     *
     * @param title
     * @param dataList
     * @param memberName 成员变量名称,取该成员的值作为显示文本
     */
    private BottomListDialog setData(String title, List dataList, String memberName) {
        mTitle.setText(title);
        adapter.setList(dataList, memberName);
        adapter.notifyDataSetChanged();
        return this;
    }

    public BottomListDialog setSelectIndex(int selectIndex) {
        adapter.setSelcetIndex(selectIndex);
        adapter.notifyDataSetChanged();
        return this;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        listView.setOnItemClickListener(itemClickListener);
    }


    class DialogListAdapter extends BaseAdapter {

        private List list = new ArrayList<>();
        private int selcetIndex = -1; //选中下标
        private String memberName; //成员变量名称,取该成员的值作为显示文本

        private LayoutInflater inflater;
        private Context mContext;

        public DialogListAdapter(Context context) {
            this.mContext = context;
            inflater = LayoutInflater.from(mContext);
        }

        /**
         * @param list
         * @param memberName 成员变量名称,取该成员的值作为显示文本
         */
        public void setList(List list, String memberName) {
            this.list = list;
            this.memberName = memberName;
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
            //如果调用者自定义view
            if (getViewCallback != null) {
                convertView = getViewCallback.diyItemView(position, convertView, parent);
                if (convertView != null) {
                    return convertView;
                }
            }

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
                if (list.get(position) instanceof String) {
                    hv.tv.setText((String) list.get(position));
                } else if (!TextUtils.isEmpty(memberName)) {
                    try {
//                    Class c = Class.forName(User.class.getCanonicalName());
                        Object obj = list.get(position);
                        Class c = obj.getClass();
                        // Field f = c.getField("name");
                        Field f = c.getDeclaredField(memberName); //能获取到私有变量
                        f.setAccessible(true);//暴力访问，能获取到私有权限
                        String showText = f.get(obj).toString();
                        hv.tv.setText(showText);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (list.get(position) instanceof ItemShowText) {
                    hv.tv.setText(((ItemShowText) list.get(position)).itemShowText() + "");
                }
            }

            //给个回调，让调用者可以自定义条目的额外显示
            if (getViewCallback != null) {
                getViewCallback.getViewAfter(hv, position, list.get(position));
            }

            return convertView;
        }

        class MyViewHolder {
            View itemView;
            ImageView iv;
            TextView tv;
        }

    }

    /**
     * 条目显示文本
     */
    interface ItemShowText {
        String itemShowText();
    }


    private AdapterGetViewCallback getViewCallback;

    /**
     * 设置列表条目getView时的回调
     *
     * @param getViewCallback
     */
    public void setGetViewCallback(AdapterGetViewCallback getViewCallback) {
        this.getViewCallback = getViewCallback;
    }

    interface AdapterGetViewCallback<T> {
        /**
         * 完全自己定义条目
         */
        default View diyItemView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        /**
         * 在 getView() 之后, 可以做条目的额外显示等处理。
         * 如果自定义了条目(diyItemView())返回了View，该方法不会再走
         *
         * @param viewHolder
         * @param position
         * @param item
         */
        void getViewAfter(DialogListAdapter.MyViewHolder viewHolder, int position, T item);
    }


}
