package com.hejing.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hejing.tally.adapter.ChartVPAdapter;
import com.hejing.tally.db.DBManager;
import com.hejing.tally.frag_chart.IncomeChartFragment;
import com.hejing.tally.frag_chart.OutcomeChartFragment;
import com.hejing.tally.utils.CalendarDialog;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity implements View.OnClickListener {

    private Button inBtn;   // 收入按钮
    private Button outBtn;  // 支出按钮
    private TextView dateTv; // 表示时间的TextView
    private TextView inTv;   // 表示收入的TextView
    private TextView outTv;  // 表示支出的TextView
    private ViewPager chartVp;  // viewPager

    private int year, month;

    private int selectYearPos = -1;  // 日历中年份位置
    private int selectMonthPos = -1;  // 日历中月份位置
    private List<Fragment> chartFragList;  // 统一存放fragment
    private IncomeChartFragment incomeChartFragment;
    private OutcomeChartFragment outcomeChartFragment;
    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);

        initView(); // 初始化控件的方法

        initTime();  // 初始化时间，否则页面展示的是哪一年哪一月的数据 ?

        initStatistics(year, month); // 初始化数据的方法

        initFrag();  // 初始化fragment (收入和支出页面)

        setVPSelectListener();  // 支出收入滑动页面的监听

    }

    /**
     * ViewPager滑动页面的监听
     */
    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                // 关联自定义的Button显示方法，使得页面的变化与收入、支出Button的的变化相互关联。
                setButtonStyle(position);  // 0 支出， 1 收入
            }
        });
    }

    private void initFrag() {
        chartFragList = new ArrayList<>();
        // 添加Fragment对象
        incomeChartFragment = new IncomeChartFragment();
        outcomeChartFragment = new OutcomeChartFragment();
        // 添加数据到Fragment中
        Bundle bundle = new Bundle();
        bundle.putInt("year", year);
        bundle.putInt("month", month);
        incomeChartFragment.setArguments(bundle);
        outcomeChartFragment.setArguments(bundle);
        // 将Fragment添加到数据源中，支出在前，收入在后
        chartFragList.add(outcomeChartFragment);
        chartFragList.add(incomeChartFragment);
        // 使用适配器
        // 将Fragment 加载到Activity中
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), chartFragList);
        chartVp.setAdapter(chartVPAdapter);

    }

    /**
     * 统计某年某月的收支情况
     */
    private void initStatistics(int year, int month) {
        double inSumMoneyOneMonth = DBManager.getSumMoneyOneMonthForKind(year, month, 1);  // 收入总金额
        double outSumMoneyOneMonth = DBManager.getSumMoneyOneMonthForKind(year, month, -1); // 支出总金额
        int inCountItemOneMonth = DBManager.getCountItemOneMonthForKind(year, month, 1);  // 收入多少笔
        int outCountItemOneMonth = DBManager.getCountItemOneMonthForKind(year, month, -1);  // 支出多少笔
        dateTv.setText(year + "年 " + month + "月账单");
        inTv.setText("共" + inCountItemOneMonth + "笔收入，￥ " + inSumMoneyOneMonth);
        outTv.setText("共" + outCountItemOneMonth + "笔支出，￥ " + outSumMoneyOneMonth);
    }

    /**
     * 初始化时间的方法
     */
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);         // 获取本年
        month = calendar.get(Calendar.MONTH) + 1;   // 获取本月
    }

    /**
     * 初始化控件的方法
     */
    private void initView() {
        inBtn = findViewById(R.id.chart_btn_in);  // 收入按钮
        outBtn = findViewById(R.id.chart_btn_out);  // 支出按钮
        dateTv = findViewById(R.id.chart_tv_date);  // 显示时间的TextView
        inTv = findViewById(R.id.chart_tv_in);  // 显示收入情况的TextView
        outTv = findViewById(R.id.chart_tv_out);  // 显示支出情况的TextView
        chartVp = findViewById(R.id.chart_vp);  // ViewPager

        // 设置点击监听
        findViewById(R.id.chart_iv_back).setOnClickListener(this);  // 点击返回ImageView
        findViewById(R.id.chart_iv_calendar).setOnClickListener(this);  // 点击日历图标
        inBtn.setOnClickListener(this);     // 点击收入按钮
        outBtn.setOnClickListener(this);    // 点击支出按钮
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.chart_iv_back) {  // 点击返回ImageView
            finish();
        } else if (view.getId() == R.id.chart_iv_calendar) {  // 点击日历图标
            showCalendarDialog();  // 初始化日历对话框
        } else if (view.getId() == R.id.chart_btn_in) {  // 点击收入按钮
            setButtonStyle(1);  // 改变按钮样式
            // 备注: ViewPager的setCurrentItem(index)方法，可以直接定位到指定的页面
            chartVp.setCurrentItem(1);
        } else if (view.getId() == R.id.chart_btn_out) {  // 点击支出按钮
            setButtonStyle(0);  // 改变按钮样式
            chartVp.setCurrentItem(0);
        }
    }

    /**
     * 显示日历对话框
     */
    private void showCalendarDialog() {
        CalendarDialog dialog = new CalendarDialog(this, selectYearPos, selectMonthPos);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                selectYearPos = selPos;
                selectMonthPos = month - 1;
                initStatistics(year, month);   // 显示数据更新
                incomeChartFragment.setDate(year, month);  // 收入数据源更新
                outcomeChartFragment.setDate(year, month); // 支出数据源更新
            }
        });
    }

    /**
     * 设置按钮样式的改变  支出: -1， 收入: 1
     */
    private void setButtonStyle(int kind) {
        if (kind == 0) {
            outBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            outBtn.setTextColor(Color.WHITE);
            inBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            inBtn.setTextColor(Color.BLACK);
        } else if (kind == 1) {
            inBtn.setBackgroundResource(R.drawable.main_recordbtn_bg);
            inBtn.setTextColor(Color.WHITE);
            outBtn.setBackgroundResource(R.drawable.dialog_btn_bg);
            outBtn.setTextColor(Color.BLACK);
        }
    }

}























