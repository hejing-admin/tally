package com.hejing.tally;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.hejing.tally.adapter.RecordPagerAdapter;
import com.hejing.tally.frag_record.IncomeFragment;
import com.hejing.tally.frag_record.OutcomeFragment;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        // 1、查找控件
        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_vp);
        // 2、设置ViewPager的加载页面
        initPager();
    }

    private void initPager() {
        // 初始化ViewPager页面的集合
        List<Fragment>fragmentList = new ArrayList<>();
        // 创建收入和支出页面，放置在Fragment中
        OutcomeFragment outFrag = new OutcomeFragment();  // 创建支出页面
        IncomeFragment inFrag = new IncomeFragment();  // 创建收入页面
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);

        // 创建适配器
        RecordPagerAdapter pagerAdapter = new RecordPagerAdapter(getSupportFragmentManager(), fragmentList);
        // 设置适配器
        viewPager.setAdapter(pagerAdapter);
        // 将TabLayout和ViewPager进行关联
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 点击 "X"号的事件处理
     * @param view
     */
    public void onClick(View view) {
        if (view.getId() == R.id.record_iv_back) {
            finish();
        }
    }
}
















