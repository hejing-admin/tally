package com.hejing.tally.frag_chart;

import android.graphics.Color;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.hejing.tally.db.BarChartItemBean;
import com.hejing.tally.db.ChartItemBean;
import com.hejing.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class IncomeChartFragment extends BaseChartFragment {

    private int kind = 1;

    @Override
    public void setYAxis(int year, int month) {
        // 获取本月收入最高的一天为多少，将之设定为y轴的最大值
        double maxMoney = DBManager.getMaxMoneyOneDayInMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney);  // 将最大金额向上取整
        // 设置y轴
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(max);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(max);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setEnabled(false);  // 不显示左边的y轴

        // 设置不显示图例
        Legend legend = barChart.getLegend();  // 得到图例
        legend.setEnabled(false);

    }


    @Override
    public void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();  // 柱状图数据的集合
        // 获取这个月每天的支出总金额
        List<BarChartItemBean> list = DBManager.getSumMoneyOneDayInMonth(year, month, kind);
        if (list.size() == 0) {
            // 如果有收支记录
            barChart.setVisibility(View.GONE);   // 不显示柱状图
            chartTv.setVisibility(View.VISIBLE); // 显示提示文本
        } else {
            // 如果没有收支记录
            barChart.setVisibility(View.VISIBLE);   // 显示柱状图
            chartTv.setVisibility(View.GONE); // 不显示提示文本

            // 设置柱状图有多少根"柱子"
            List<BarEntry> barEntries = new ArrayList<>();  // BarEntry 表示每一根柱子
            for (int i = 0; i < 31; i++) {
                // 初始化每一根柱子，添加到柱状图中
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries.add(entry);
            }
            for (int i = 0; i < list.size(); i++) {
                BarChartItemBean itemBean = list.get(i);
                int day = itemBean.getDay();  // 获取日期 (具体是那一天进行消费的)
                // 根据day获取x轴的位置
                int xIndex = day - 1;
                BarEntry barEntry = barEntries.get(xIndex);
                barEntry.setY((float) itemBean.getSummoney());
            }

            //
            BarDataSet barDataSet1 = new BarDataSet(barEntries, "");
            barDataSet1.setValueTextColor(Color.BLACK);  // 设置值的颜色
            barDataSet1.setValueTextSize(8f);  // 设置值的大小
            barDataSet1.setColor(Color.parseColor("#006400"));    // 设置柱子的颜色

            // 设置柱子上数据的显示格式
            barDataSet1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    if (value == 0) {
                        return "";
                    }
                    return value + "";
                }
            });

            sets.add(barDataSet1);
            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f);  // 设置柱子宽度
            barChart.setData(barData);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadDatas(year, month, kind);
    }

    @Override
    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadDatas(year, month, kind);
    }

    @Override
    public void loadDatas(int year, int month, int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccountTb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        // 提示适配器更新
        chartItemAdapter.notifyDataSetChanged();
    }
}
















