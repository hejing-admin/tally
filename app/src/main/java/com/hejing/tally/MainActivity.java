package com.hejing.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hejing.tally.adapter.AccountAdapter;
import com.hejing.tally.db.AccountBean;
import com.hejing.tally.db.DBManager;
import com.hejing.tally.utils.BudgetDialog;
import com.hejing.tally.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView todayLv;  // 展示今日收支情况的ListView
    private List<AccountBean> mDatas;  // 声明数据源
    private int year, month, day;  // 当日
    private AccountAdapter adapter;

    // 头布局相关
    private View headerView;  // 头布局视图
    private TextView topOutTv;  // 表示支出
    private TextView topInTv;   // 表示收入
    private TextView topBudgetTv;   // 表示预算
    private TextView topConTv;  // 表示今日情况
    private ImageView topShowLv;    // 表示显示或者隐藏

    // 设置共享参数
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTime();

        // 初始化共享参数
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        todayLv = findViewById(R.id.main_lv);
        // 添加ListView的头布局
        addLVHeaderView();

        mDatas = new ArrayList<>();
        // 设置适配器: 加载每一行数据到列表中
        adapter = new AccountAdapter(this, mDatas);
        todayLv.setAdapter(adapter);

        findViewById(R.id.main_btn_edit).setOnClickListener(this);  // 给"记一笔"按钮添加点击事件监听
        findViewById(R.id.main_iv_search).setOnClickListener(this); // 给"搜索"添加点击事件监听
        findViewById(R.id.main_btn_more).setOnClickListener(this);  // 给"更多"添加点击事件监听
        // 给ListView 添加长按事件监听
        setLVLongClickListener();
    }

    /**
     * 设置ListView的长按事件监听
     */
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  // 长按的是 头布局
                    return false;
                }
                int pos = position - 1;
                AccountBean clickBean = mDatas.get(pos);  // 获取正在被点击的这个信息
                // 弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    /**
     * 弹出是否删除某一条记录的对话框
     */
    private void showDeleteItemDialog(AccountBean accountBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("您确定要删除这条记录吗?")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 执行删除操作
                        int click_id = accountBean.getId();
                        int i = DBManager.deleteItemFromAccountTbById(click_id);  // 数据库根据传入的id进行删除操作
                        mDatas.remove(accountBean);  // 实时刷新，移除集合中的删除对象
                        adapter.notifyDataSetChanged();  // 提示适配器进行更新
                        setTopTvShow();  // 数据更新之后，头布局中的数据显示也需要进行更新。
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * 给ListView添加头布局的方法
     */
    private void addLVHeaderView() {
        // 将页面布局转换为 View对象
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        todayLv.addHeaderView(headerView);  // 设置头布局

        // 查找头布局可用的控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topBudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowLv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        // 给有点击事件的控件设置事件监听
        headerView.setOnClickListener(this);  // 头布局整体有一个点击事件
        topBudgetTv.setOnClickListener(this); // 预算金额有点击事件
        topShowLv.setOnClickListener(this);  // 隐藏图标有点击事件


    }

    /**
     * 获取今日的具体时间
     */
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);         // 当年
        month = calendar.get(Calendar.MONTH) + 1;   // 当月
        day = calendar.get(Calendar.DAY_OF_MONTH);  // 当日
    }

    /**
     * 当Activity获取焦点时会调用的方法
     * 在此处加载数据库，进行页面的更新显示
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();

        // 设置头布局的显示，每次回到该页面都会查询数据库并进行数据的显示更新
        setTopTvShow();
    }

    /**
     * 设置头布局中文本内容的显示
     */
    private void setTopTvShow() {
        // 获取今日收入和支出的总金额，并显示在头布局相应的TextView中
        double incomeOneDay = DBManager.getSumMoneyOneDayForKind(year, month, day, 1);  // 今日收入
        double outcomeOneDay = DBManager.getSumMoneyOneDayForKind(year, month, day, -1); // 今日支出
        String infoOneDay = "今日支出 ￥" + outcomeOneDay + "  今日收入 ￥" + incomeOneDay;
        topConTv.setText(infoOneDay);
        // 获取本月的收入和支出的总金额
        double incomeOneMonth = DBManager.getSumMoneyOneMonthForKind(year, month, 1);
        double outcomeOneMonth = DBManager.getSumMoneyOneMonthForKind(year, month, -1);
        topInTv.setText("￥ " + incomeOneMonth); // 在头布局中显示本月收入情况
        topOutTv.setText("￥ " + outcomeOneMonth); // 在头布局中显示本月情况

        // 设置显示预算剩余
        // 从共享参数中获取预算金额
        String moneyStr = preferences.getString("money", "0");
        double money = Double.parseDouble(moneyStr);
        if (money == 0) {
            topBudgetTv.setText("￥ 0");
        } else {
            double syMoney = money - outcomeOneMonth;
            topBudgetTv.setText("￥ " + syMoney);
        }
    }

    /**
     * 加载数据库数据的方法实现
     */
    private void loadDBData() {
        // 此处查询时间默认是当天。可扩展
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccountTb(year, month, day);
        // 将原来的数据清空
        mDatas.clear();
        // 添加新数据
        mDatas.addAll(list);
        // 提示adapter更新
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.main_btn_edit) {  // 点击 记一笔
            Intent it1 = new Intent(this, RecordActivity.class);
            // 跳转到记录页面
            startActivity(it1);
        } else if (view.getId() == R.id.main_iv_search) {  // 点击 搜索
            Intent it2 = new Intent(this, SearchActivity.class);
            // 跳转到搜索页面
            startActivity(it2);
        } else if (view.getId() == R.id.main_btn_more) {  // 点击 更多
            // 显示更多对话框
            showMoreDialog();
        } else if (view.getId() == R.id.item_mainlv_top_tv_budget) {  // 点击头布局中的 预算金额
            // 显示设置预算金额的对话框
            showBudgetDialog();
        } else if (view.getId() == R.id.item_mainlv_top_iv_hide) {  // 点击头布局中的 隐藏图标
            // 切换TextView 明文和密文
            toggleHideAndShow();

        } else if (view == headerView) {    // 点击 头布局整体
            Intent intent = new Intent(this, MonthChartActivity.class);
            startActivity(intent);  // 跳转到账单详情页面
        }
    }

    /**
     * 显示 "更多"对话框
     */
    private void showMoreDialog() {
        MoreDialog moreDialog = new MoreDialog(this);
        moreDialog.show();
        moreDialog.setDialogSize();  // 设置尺寸 (自定义)
    }

    /**
     * 显示预算设置对话框
     */
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();  // 设置dialog尺寸

        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(double money) {
                // 将预算金额写到到共享参数中进行储存
                SharedPreferences.Editor editor = preferences.edit();
                // 由于共享参数不支持保存double类型，所以此处将double转为str进行储存，
                // 后续再转换回double即可。
                editor.putString("money", String.valueOf(money));
                editor.commit();

                // 计算剩余金额
                // 得到本月支出金额
                double outcomeOneMonth = DBManager.getSumMoneyOneMonthForKind(year, month, -1);
                double syMoney = money - outcomeOneMonth;  // 得到预算剩余
                topBudgetTv.setText("￥ " + syMoney);

            }
        });
    }

    boolean isShow = true;  // 明文密文切换的标志
    /**
     * 点击头布局 眼睛图标，在明文和密文之间进行切换
     */
    private void toggleHideAndShow() {
        if (isShow){  // 原来是明文，则转为密文
            PasswordTransformationMethod passwordTransformationMethod =
                    PasswordTransformationMethod.getInstance();  // 设置密文对象
            // 通过该方法设置显示模式 setTransfromationMethod(密文对象)
            topInTv.setTransformationMethod(passwordTransformationMethod);      // 隐藏收入金额
            topOutTv.setTransformationMethod(passwordTransformationMethod);     // 隐藏支出金额
            topBudgetTv.setTransformationMethod(passwordTransformationMethod);  // 隐藏预算金额

            topShowLv.setImageResource(R.mipmap.ih_hide);   // 切换 眼睛图标的样式
            isShow = false;  // 设置标志位为隐藏状态

        } else {  // 是密文，则转为明文
            HideReturnsTransformationMethod hideInstance =
                    HideReturnsTransformationMethod.getInstance();  // 明文对象
            topInTv.setTransformationMethod(hideInstance);
            topOutTv.setTransformationMethod(hideInstance);
            topBudgetTv.setTransformationMethod(hideInstance);
            topShowLv.setImageResource(R.mipmap.ih_show);
            isShow = true;
        }
    }
}





















