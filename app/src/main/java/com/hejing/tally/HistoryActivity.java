package com.hejing.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hejing.tally.adapter.AccountAdapter;
import com.hejing.tally.db.AccountBean;
import com.hejing.tally.db.DBManager;
import com.hejing.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView historyLv;     // ListView
    private TextView timeTv;        // 时间图标
    private List<AccountBean> mDatas;  // ListView的数据源声明
    private AccountAdapter adapter;  // 适配器声明
    private int year, month;  // 初始化的是 year年，month月的数据

    public int dialogSelectYearPos = -1;  // 年份位置
    public int dialogSelectMonthPos = -1;  // 月份位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyLv = findViewById(R.id.history_lv);
        timeTv = findViewById(R.id.history_tv_time);

        // 为 ListView设置适配器
        mDatas = new ArrayList<>();
        adapter = new AccountAdapter(this, mDatas);
        historyLv.setAdapter(adapter);

        // 初始化需要查询数据的具体的月份
        initTime();
        timeTv.setText(year + "年 " + month + "月");

        // 从数据库中加载数据
        loadData(year, month);

        // 设置事件监听
        findViewById(R.id.history_calendar).setOnClickListener(this); // 点击历史账单图标
        findViewById(R.id.history_iv_back).setOnClickListener(this);  // 点击返回图标

        // 设置ListView的长按事件
        setLVClicListener();
    }

    /**
     * 设置ListView中每一个item的长按事件
     */
    private void setLVClicListener() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取被点击的item
                AccountBean accountBean = mDatas.get(position);
                deleteItem(accountBean);
                return false;
            }
        });
    }

    /**
     * 根据id删除元素
     * @param accountBean
     */
    private void deleteItem(AccountBean accountBean) {
        int delId = accountBean.getId();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("你确定要删除这条记录吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBManager.deleteItemFromAccountTbById(delId); // 从数据库删除
                        mDatas.remove(accountBean);  // 实时刷新从数据源删除
                        adapter.notifyDataSetChanged();  // 提示适配器更新
                    }
                });
        builder.create().show();
    }

    /**
     * 从数据库中加载数据
     * 获取指定年份-月份的收支情况的数据
     */
    private void loadData(int year, int month) {
        List<AccountBean> list = DBManager.getAccountListOneMonthFromAccountTb(year, month);
        mDatas.clear();  // 清空原数据源
        mDatas.addAll(list);    // 添加数据源
        adapter.notifyDataSetChanged();  // 提示适配器进行更新
        // Log.i("ting", year + ": " + month);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.history_iv_back) {  // 点击返回图标
            finish();
        } else if (view.getId() == R.id.history_calendar) {  // 点击历史账单图标
            CalendarDialog dialog = new CalendarDialog(this, dialogSelectYearPos,
                    dialogSelectMonthPos);
            dialog.show();
            dialog.setDialogSize();
            // 设置接口回调
            dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
                @Override
                public void onRefresh(int selYearPos, int year, int month) {
                    timeTv.setText(year + "年 " + month + "月");
                    // 从数据库中加载数据
                    loadData(year, month);
                    dialogSelectYearPos = selYearPos;       // 记录之前选中的年份位置
                    dialogSelectMonthPos = month - 1;   // 记录之前选中的月份位置
                    // Log.i("ting", year + ": " + month);
                }
            });
        }
    }
}































