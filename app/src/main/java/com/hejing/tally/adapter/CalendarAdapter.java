package com.hejing.tally.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hejing.tally.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史账单界面，点击日历表，弹出对话框，本类为GridView对应的适配器
 */
public class CalendarAdapter extends BaseAdapter {

    private Context context;
    private List<String> mDatas;
    public int year;  // 年份
    public int selMonthPos = -1;  // 月份位置

    public void setYear(int year) {
        this.year = year;
        mDatas.clear();
        loadDatas(year);
        notifyDataSetChanged();
    }

    private void loadDatas(int year) {
        for (int i = 1; i < 13; i++) {
            String data = year + "/" + i;
            mDatas.add(data);
        }
    }

    public CalendarAdapter(Context context, int year) {
        this.context = context;
        this.year = year;
        mDatas = new ArrayList<>();
        loadDatas(year);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 将准备好的item_dialogcal_gv.xml转为为 view
        convertView = LayoutInflater.from(context).inflate(R.layout.item_dialogcal_gv, parent, false);
        TextView tv = convertView.findViewById(R.id.item_dialogcal_gv_tv);
        tv.setText(mDatas.get(position));
        tv.setBackgroundResource(R.color.grey_f3f3f3);  // 没有被选中的View的背景
        tv.setTextColor(Color.BLACK);  //  没有被选中的View的字体颜色
        // 设置选中后的背景和字体颜色
        if (position == selMonthPos) {
            tv.setBackgroundResource(R.color.green_006400);
            tv.setTextColor(Color.WHITE);
        }
        return convertView;
    }
}


























