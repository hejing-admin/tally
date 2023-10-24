package com.hejing.tally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hejing.tally.R;
import com.hejing.tally.db.ChartItemBean;

import java.util.List;

/**
 * 账单详情页面，ListView的适配器
 */
public class ChartItemAdapter extends BaseAdapter {

    private Context context;    // 上下文对象
    private List<ChartItemBean> mDatas;  // 数据源
    private LayoutInflater inflater;

    public ChartItemAdapter(Context context, List<ChartItemBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        this.inflater = LayoutInflater.from(context);
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chart_fragment_lv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 获取显示内容
        ChartItemBean bean = mDatas.get(position);
        holder.iv.setImageResource(bean.getsImageId());
        holder.typeNameTv.setText(bean.getTypeName());
        holder.radioTv.setText(bean.getRatio() * 100 + "%");
        holder.totalTv.setText("￥ " + bean.getTotalMoney());
        return convertView;
    }

    class ViewHolder {
        TextView typeNameTv, radioTv, totalTv;
        ImageView iv;
        public ViewHolder(View view) {
            typeNameTv = view.findViewById(R.id.item_chart_fragment_tv_type);
            radioTv = view.findViewById(R.id.item_chart_fragment_tv_percent);
            totalTv = view.findViewById(R.id.item_chart_fragment_tv_sum_money);
            iv = view.findViewById(R.id.item_chart_fragment_iv);
        }
    }
}




















