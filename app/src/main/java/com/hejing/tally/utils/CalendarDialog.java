package com.hejing.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hejing.tally.R;
import com.hejing.tally.adapter.CalendarAdapter;
import com.hejing.tally.db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarDialog extends Dialog implements View.OnClickListener {

    private ImageView errorIv;  // "X"取消按钮
    private GridView gv;  //
    private LinearLayout hsvLayout;  //

    private List<TextView> hsvTextViewList;
    private List<Integer> yearList;
    private int selectYearPos = -1;  // 记录被点击的年份的位置

    private int selectMonthPos = -1;  // 记录被点击的月份的位置
    private CalendarAdapter adapter;

    /**
     * 设置接口回调，当退出该Dialog后，把从该Dialog中点击得到的信息传递到外界。
     */
    public interface OnRefreshListener {
        void onRefresh(int selPos, int year, int month);
    }

    OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }

    public CalendarDialog(@NonNull Context context, int selectYearPos, int selectMonthPos) {
        super(context);
        // 初始化年份位置和月份位置
        this.selectYearPos = selectYearPos;
        this.selectMonthPos = selectMonthPos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_calendar);

        gv = findViewById(R.id.dialog_calendar_gv);
        errorIv = findViewById(R.id.dialog_calendar_iv);
        hsvLayout = findViewById(R.id.dialog_calendar_layout);

        // 设置点击事件
        errorIv.setOnClickListener(this);

        // 向横向的ScrollView当中添加view的方法
        addViewToLayout();

        // 初始化gridview (各个月份)
        initGridView();

        // 设置GridView中每一个item的点击事件
        setGVListener();
    }

    /**
     * 设置GridView中每一个item的点击事件
     */
    private void setGVListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selMonthPos = position;
                adapter.notifyDataSetInvalidated();  // 提示adapter进行更新
                // 获取到被选中的年份和月份
                int month = position + 1;  // 月份
                int year = adapter.year;   // 年份
                onRefreshListener.onRefresh(selectYearPos, year, month);
                cancel();
            }
        });
    }

    /**
     * 根据选中的年份初始化GridView (12个月份)
     */
    private void initGridView() {
        int selYear = yearList.get(selectYearPos);  // 获取被选中的年份
        adapter = new CalendarAdapter(getContext(), selYear);

        if (selectMonthPos == -1) {
            // 默认选择当前月份
            int monthPos = Calendar.getInstance().get(Calendar.MONTH);  // 得到当前月份所在的pos(从0开始)
            adapter.selMonthPos = monthPos;  // 更新适配器中的"选中月份"的位置
        } else {
            adapter.selMonthPos = selectMonthPos;
        }
        gv.setAdapter(adapter);

    }

    /**
     * 向横向的ScrollView中添加view的方法
     */
    private void addViewToLayout() {
        hsvTextViewList = new ArrayList<>();  // 将添加进入线性布局当中的TextView进行统一管理的集合。
        yearList = DBManager.getYearListFromAccountTb();  // 获取数据库中储存了哪些年份 (从低到高排列)
        // 如果数据库中没有记录，就添加今年的记录,保证至少有一条数据 (view)
        if (yearList.size() == 0) {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            yearList.add(year);
        }
        // 遍历年份，有哪些年就向HorizontalScrollView中添加对应的view
        for (int i = 0; i < yearList.size(); i++) {
            int year = yearList.get(i);
            // 将已经准备好的 item_dialogcal_hsv.xml页面转化为 view
            View view = getLayoutInflater().inflate(R.layout.item_dialogcal_hsv, null);
            hsvLayout.addView(view);  // 将view添加到布局中进行展示
            TextView hsvTv = view.findViewById(R.id.item_dialogcal_hsv_tv);
            hsvTv.setText(year+"");
            hsvTextViewList.add(hsvTv);  // 将各个TextView(不同年份)统一放在集合中，方面后续的管理(比如点击事件等)
        }
        if (selectYearPos == -1) {
            selectYearPos = hsvTextViewList.size() - 1;  // 设置默认被选中的年份是最近的年份
        }
        changeTvBg(selectYearPos);  // 设置选中背景

        setHSVClickListener();   // 设置每一个view的事件监听 (每一个year)
    }

    /**
     * 给横向的ScrollView当中每一个TextView设置点击事件
     */
    private void setHSVClickListener() {
        for (int i = 0; i < hsvTextViewList.size(); i++) {
            TextView tv = hsvTextViewList.get(i);
            int pos = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeTvBg(pos);
                    selectYearPos = pos;
                    // 获取被选中的年份，然后ScrollView下面的GridView显示数据源随之发生相应变化
                    int year = yearList.get(selectYearPos);
                    adapter.setYear(year);  // 在adapter的内部进行适配器的更新
                }
            });
        }
    }

    /**
     * 传入被选中的位置，改变此位置上的背景和文字颜色
     * @param selectYearPos
     */
    private void changeTvBg(int selectYearPos) {
        for (int i = 0; i < hsvTextViewList.size(); i++) {
            TextView tv = hsvTextViewList.get(i);
            tv.setBackgroundResource(R.drawable.dialog_btn_bg);  // 设置背景
            tv.setTextColor(Color.BLACK);  // 设置文字颜色
        }
        TextView selView = hsvTextViewList.get(selectYearPos);
        selView.setBackgroundResource(R.drawable.main_recordbtn_bg);    // 设置背景
        selView.setTextColor(Color.WHITE);  // 设置文字颜色
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_calendar_iv) {  // "X"被点击
            cancel();
        }
    }


    /**
     * 设置Dialog的尺寸和屏幕尺寸一致, 顶部对齐
     */
    public void setDialogSize() {
        // 获取当前窗口对象
        Window window = getWindow();
        // 获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取整个屏幕的宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());  // 对话框窗口为屏幕窗口
        wlp.gravity = Gravity.TOP;  // 上对齐
        // 设置背景为透明色
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(wlp);
    }
}































