package com.hejing.tally.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hejing.tally.R;
import com.hejing.tally.db.AccountBean;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    private Context context;
    private List<AccountBean> mDatas;
    private LayoutInflater inflater;
    private int year, month, day;  // 保存当前的年月日， 与数据库中的年月日作比较，完成当天的数据显示“今日”的效果。

    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(this.context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);         // 获取当年
        month = calendar.get(Calendar.MONTH) + 1;   // 获取当月
        day = calendar.get(Calendar.DAY_OF_MONTH);  // 获取当日
    }

    // 返回数据源数量
    @Override
    public int getCount() {
        return mDatas.size();
    }

    // 返回指定位置的元素
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    // 返回指定位置
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * ListView 在该方法中加载数据
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 将布局页面转换为 view
            convertView = inflater.inflate(R.layout.item_mainlv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AccountBean bean = mDatas.get(position);
        holder.typeIv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getTypeName());
        holder.remarkTv.setText(bean.getRemark());
        holder.moneyTv.setText("￥ " + bean.getMoney());

        if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day) {  // 说明是当天的数据
            String time = bean.getTime().split(" ")[1];  // 得到时钟和分钟的信息
            holder.timeTv.setText("今天 " + time);
        } else {
            holder.timeTv.setText(bean.getTime());
        }

        return convertView;
    }

    class ViewHolder {
        ImageView typeIv;
        TextView typeTv, remarkTv, timeTv, moneyTv;
        public ViewHolder(View view) {
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            remarkTv = view.findViewById(R.id.item_mainlv_tv_remark);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);
        }
    }
}






















