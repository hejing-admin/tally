package com.hejing.tally.frag_record;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hejing.tally.R;
import com.hejing.tally.db.TypeBean;

import java.util.List;

public class TypeBaseAdapter extends BaseAdapter {

    Context context;  // 表明所在的Activity
    List<TypeBean> mDatas;  // 数据源
    int selectPos = 0;  // 本次点击的位置，默认为0

    public TypeBaseAdapter(Context context, List<TypeBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    /**
     * 返回集合长度
     * @return
     */
    @Override
    public int getCount() {
        return mDatas.size();
    }

    /**
     * 返回指定位置的数据源
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    /**
     * 返回指定位置
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 这个适配器不考虑复用问题，因为所有的item都显示在界面上，不会因为滑动就消失，因为没有剩余的convertView,
     * 所以不用复写
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_recordfrag_gv, parent, false);
        // 查找布局中的控件
        ImageView iv = convertView.findViewById(R.id.item_recordfrag_iv);
        TextView tv = convertView.findViewById(R.id.item_recordfrag_tv);
        // 获取指定位置的数据源
        TypeBean typeBean = mDatas.get(position);
        tv.setText(typeBean.getTypeName());
        // 判断当前位置是否为选中位置，如果是选中位置，就设置带颜色的图片，否则为灰色图
        if (selectPos == position) {
            iv.setImageResource(typeBean.getsImageId());
        } else {
            iv.setImageResource(typeBean.getImageId());
        }
        return convertView;
    }
}





























