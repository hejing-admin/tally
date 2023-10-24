package com.hejing.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hejing.tally.adapter.AccountAdapter;
import com.hejing.tally.db.AccountBean;
import com.hejing.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView searchIvBack;  // 返回控件
    private ImageView searchIvDone;  // 搜索控件
    private ListView searchLv;      // 显示的ListView 元素项
    private EditText searchEt;      // 搜索框控件
    private TextView searchEmptyTv;       //空页面TextView控件

    private List<AccountBean> mDatas;  // 需要进行显示的数据源
    private AccountAdapter adapter;     // 记账本适配器对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // 初始化页面各个控件
        // 注意，一定要先初始化，之后在进行下面的配置适配器操作
        // 刚开始做的时候把initView() 方法写在了后面，app一直闪退，排查了挺长时间的bug，
        // 最后发现是initView() 没先被执行的缘故。
        initView();

        mDatas = new ArrayList<>(); // 初始化数据源
        adapter = new AccountAdapter(this, mDatas); // 初始化适配器
        searchLv.setAdapter(adapter);  // ListView 设置适配器
        searchLv.setEmptyView(searchEmptyTv);  // 设置无数据时需要显示的控件

        // 设置事件监听:
        searchIvBack.setOnClickListener(this);  // 返回控件事件监听
        searchIvDone.setOnClickListener(this);  // 搜索控件事件监听

    }

    /**
     * 初始化页面各个控件
     */
    private void initView() {
        searchIvBack = findViewById(R.id.search_iv_back);
        searchIvDone = findViewById(R.id.search_iv_done);
        searchEt = findViewById(R.id.search_et);
        searchLv = findViewById(R.id.search_lv);
        searchEmptyTv = findViewById(R.id.search_tv_empty);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.search_iv_back) {  // 点击返回控件
            finish();
        } else if (view.getId() == R.id.search_iv_done) {  // 点击搜索进行搜索执行操作
            String remark_msg = searchEt.getText().toString().trim();
            if (TextUtils.isEmpty(remark_msg)) {
                Toast.makeText(this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            // 根据remark 开始进行搜索
            List<AccountBean> list = DBManager.getAccountListByRemarkFromAccountTb(remark_msg);
            mDatas.clear();  // 先进行清空再更新数据源
            mDatas.addAll(list);  // 更新数据源
            adapter.notifyDataSetChanged();  // 提示适配器更新
        }
    }
}




















