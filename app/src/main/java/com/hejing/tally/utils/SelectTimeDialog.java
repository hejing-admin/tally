package com.hejing.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hejing.tally.R;

/**
 * 在记录页面弹出时间对话框
 */
public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    private EditText hourEt;
    private EditText minuteEt;
    private DatePicker datePicker;
    private Button ensureBtn;
    private Button cancelBtn;

    // 设置回调接口进行确定按钮的监听
    public interface OnEnsureListener {
        public void onEnsure(String time, int year, int month, int day);
    }
    OnEnsureListener onEnsureListener;

    // 设置回调监听方法
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public SelectTimeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time);

        hourEt = findViewById(R.id.dialog_time_et_hour);
        minuteEt = findViewById(R.id.dialog_time_et_minute);
        datePicker = findViewById(R.id.dialog_time_dp);

        ensureBtn = findViewById(R.id.dialog_time_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_time_btn_cancel);
        // 设置按钮点击事件监听
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        // 隐藏头布局
        hideDatePickerHeader();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_time_btn_cancel) {  // 点击取消
            cancel();
        } else if (view.getId() == R.id.dialog_time_btn_ensure) { // 点击了确定按钮
            int year = datePicker.getYear();
            int month = datePicker.getMonth() + 1;
            String monthStr = String.valueOf(month);
            monthStr = month < 10 ? "0" + month: monthStr;
            int day = datePicker.getDayOfMonth();
            String dayStr = String.valueOf(day);
            dayStr = day < 10 ? "0" + day: dayStr;
            // 获取输入的小时和分钟
            String hourStr = hourEt.getText().toString();
            String minuteStr = minuteEt.getText().toString();
            int hour = 0;
            if (!TextUtils.isEmpty(hourStr)) {
                hour = Integer.parseInt(hourStr);
                hour = hour % 24;
            }
            int minute = 0;
            if (!TextUtils.isEmpty(minuteStr)) {
                minute = Integer.parseInt(minuteStr);
                minute = minute % 60;
            }
            hourStr = hour < 10 ? "0" + hour: hourStr;
            minuteStr = minute < 10 ? "0" + minute: minuteStr;

            String timeFormat = year + "年" + monthStr + "月" + dayStr + "日 " + hourStr
                    + ":" + minuteStr;
            // 这里没问题
            Log.i("ting", "从datePicker中取出的的year: " + year + " 和month: " + month);

            if (onEnsureListener != null) {
                onEnsureListener.onEnsure(timeFormat, year, month, day);
            }
            cancel();
        }
    }

    // 隐藏DatePicker 的头布局
    private void hideDatePickerHeader() {
        ViewGroup rootView = (ViewGroup) datePicker.getChildAt(0);
        if (rootView == null) {
            return;
        }
        View headerView = rootView.getChildAt(0);  // 得到头布局
        if (headerView == null) {
            return;
        }
        headerView.setVisibility(View.GONE);

    }
}


































