package com.hejing.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.hejing.tally.AboutActivity;
import com.hejing.tally.HistoryActivity;
import com.hejing.tally.MonthChartActivity;
import com.hejing.tally.R;
import com.hejing.tally.SettingActivity;

public class MoreDialog extends Dialog implements View.OnClickListener {

    private Button aboutBtn;  // 关于
    private Button settingBtn;  // 设置
    private Button recordBtn;  // 记录历史
    private Button infoBtn;  // 账单详情
    private ImageView errorIv;  // "X" ImageView

    public MoreDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_more);

        aboutBtn = findViewById(R.id.dialog_more_btn_about);
        settingBtn = findViewById(R.id.dialog_more_btn_setting);
        recordBtn = findViewById(R.id.dialog_more_btn_record);
        infoBtn = findViewById(R.id.dialog_more_btn_info);
        errorIv = findViewById(R.id.dialog_more_iv_error);

        // 设置点击事件监听
        aboutBtn.setOnClickListener(this);
        settingBtn.setOnClickListener(this);
        recordBtn.setOnClickListener(this);
        infoBtn.setOnClickListener(this);
        errorIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (view.getId() == R.id.dialog_more_btn_about) {  // 关于
            intent.setClass(getContext(), AboutActivity.class);
            getContext().startActivity(intent);  // 跳转到 关于页面
        } else if(view.getId() == R.id.dialog_more_btn_setting) { // 设置
            intent.setClass(getContext(), SettingActivity.class);
            getContext().startActivity(intent);  // 跳转到 设置界面
        } else if(view.getId() == R.id.dialog_more_btn_record) {  // 记录历史
            intent.setClass(getContext(), HistoryActivity.class);
            getContext().startActivity(intent);  // 跳转到 history记录页面
        } else if(view.getId() == R.id.dialog_more_btn_info) {  // 账单详情
            intent.setClass(getContext(), MonthChartActivity.class);
            getContext().startActivity(intent);  // 跳转到账单详情页面
        } else if(view.getId() == R.id.dialog_more_iv_error) {  // 点击 "X" ImageView
            cancel();  // 退出dialog即可
        }
    }

    /**
     * 设置Dialog的尺寸和屏幕尺寸一致
     */
    public void setDialogSize() {
        // 获取当前窗口对象
        Window window = getWindow();
        // 获取窗口对象的参数
        WindowManager.LayoutParams wlp = window.getAttributes();
        // 获取整个屏幕的宽度
        Display d = window.getWindowManager().getDefaultDisplay();
        wlp.width = (int)(d.getWidth());  // 对话框窗口为屏幕窗口
        wlp.gravity = Gravity.BOTTOM;  // 下对齐
        // 设置背景为透明色
        window.setBackgroundDrawableResource(android.R.color.transparent);

        window.setAttributes(wlp);
    }

}





































