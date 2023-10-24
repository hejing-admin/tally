package com.hejing.tally.frag_chart;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.hejing.tally.R;
import com.hejing.tally.adapter.ChartItemAdapter;
import com.hejing.tally.db.ChartItemBean;
import com.hejing.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public abstract class BaseChartFragment extends Fragment {

    private ListView chartLv;  // Fragment 中的 ListView控件
    public int year;  // 需要展示的年份
    public int month;  // 需要展示的月份
    public List<ChartItemBean> mDatas;  // 数据源
    public ChartItemAdapter chartItemAdapter;
    public BarChart barChart;  // 代表柱状图的控件
    public TextView chartTv;  // 如果没有收支情况，需要显示的文本控件

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_chart, container, false);
        chartLv = view.findViewById(R.id.frag_chart_lv);
        // 获取Activity 传递过来的bundle对象
        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");
        // 设置数据源
        mDatas = new ArrayList<>();
        // 设置适配器
        chartItemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(chartItemAdapter);

        // 添加头布局:
        addLVHeaderView();
        
        return view;
    }

    /**
     * 添加头布局的方法
     */
    private void addLVHeaderView() {
        // 将柱状图布局xml转化为View对象
        View headerView = getLayoutInflater().inflate(R.layout.item_chart_fragmen_top, null);
        // 将 View对象添加到ListView的头布局上
        chartLv.addHeaderView(headerView);
        // 查找头布局当中的控件
        barChart = headerView.findViewById(R.id.item_chart_fragment_top_chart);  // 柱状图控件
        chartTv = headerView.findViewById(R.id.item_chart_fragment_top_tv);  // 如果没有收支情况需要显示的文本控件
        // 设定柱状图不显示描述
        barChart.getDescription().setEnabled(false);
        // 设定柱状图的内边距
        barChart.setExtraOffsets(20, 20, 20, 20);
        // 设置坐标轴
        setAxis(year, month);
        // 设置坐标轴显示的数据
        setAxisData(year, month);
    }

    /**
     * 设置坐标轴显示的数据，在子类中进行完善
     * @param year
     * @param month
     */
    public abstract void setAxisData(int year, int month);

    /**
     * 设置柱状图坐标轴的显示
     * 注意，方法必须重写
     * @param year
     * @param month
     */
    private void setAxis(int year, int month) {
        // 设置x轴
        XAxis xAxis = barChart.getXAxis();  // 得到x轴对象
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置x轴显示在下方，默认显示在上方
        xAxis.setDrawGridLines(true);  // 设置绘制该轴的网格线
        // 设置x轴标签的个数
        xAxis.setLabelCount(31);  // 一个月最多31天
        xAxis.setTextSize(12f);  // 设置x轴标签的大小
        // 设置x轴显示的值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int val = (int) value;
                if (val == 0) {
                    return month + "-1";  // 显示1号
                }
                if (value == 14) {
                    return month + "-15";  // 显示15号
                }
                // 根据不同的月份，显示最后一天的位置
                if (month == 2) {
                    if (val == 27) {
                        return month + "-28";
                    }
                } else if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    if (val == 30) {
                        return month + "-31";
                    }
                } else {
                    if (val == 30) {
                        return month + "-30";
                    }
                }
                return "";  // 其他位置显示空
            }
        });
        xAxis.setYOffset(10);  // 设置标签对x轴的偏移量，垂直方向

        // 设置y轴
        // 注意，对于y轴的设置，不同的子类有不同的"想法"，该处由子类自己来完成。
        setYAxis(year, month);
    }

    /**
     * 设置y轴，因为最高的坐标并不确定，所以有子类自己来设置
     */
    public abstract void setYAxis(int year, int month);

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 设置年、月的方法，后续具体的数据源更新则由具体的子类来完成
     * @param year
     * @param month
     */
    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;


        barChart.clear();  // 清空柱状图中的数据
        barChart.invalidate();  // 重新绘制柱状图
        setAxis(year, month);  // 设置x轴、y轴坐标
        setAxisData(year, month);  // 设置x轴、y轴数据

    }

    // 具体的数据查询由子类来完成
    public abstract void loadDatas(int year, int month, int kind);
}
















