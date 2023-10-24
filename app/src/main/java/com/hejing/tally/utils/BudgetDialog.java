package com.hejing.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hejing.tally.R;

public class BudgetDialog extends Dialog implements View.OnClickListener{
    // 找到控件
    private ImageView cancelIv;  // 取消的"X"图标
    private Button ensure;  // 确定按钮
    private EditText moneyEt;  // EditText 预算金额输入

    // 定义接口回调传出预算数据
    public interface OnEnsureListener {
        void onEnsure(double money);
    }
    // 声明接口
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public BudgetDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_budget);

        cancelIv = findViewById(R.id.dialog_budget_iv_error);
        ensure = findViewById(R.id.dialog_budget_btn_ensure);
        moneyEt = findViewById(R.id.dialog_budget_et);

        // 设置事件监听
        cancelIv.setOnClickListener(this);
        ensure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_budget_btn_ensure) {  // 点击了确定按钮
            // 获取输入数据
            String data = moneyEt.getText().toString();
            if (TextUtils.isEmpty(data)) {
                Toast.makeText(getContext(), "输入数据不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            double money = Double.parseDouble(data);
            if (money <= 0) {
                Toast.makeText(getContext(), "预算金额必须大于0", Toast.LENGTH_SHORT).show();
                return;
            }
            if (onEnsureListener != null) {
                onEnsureListener.onEnsure(money);  // 传出由对话框得到的预算金额数据
            }
            cancel();
        } else if (view.getId() == R.id.dialog_budget_iv_error) {  // 点击了 "X" ImageView
            cancel();  // 取消对话框即可。
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
        // 延迟0.1秒钟发送一条空消息
        // handler.sendEmptyMessageDelayed(1, 100);
    }

    /**
     * 设置自动打开软键盘的方法
     * 注意: 不能写在setDialogSize()方法内，因为此方法需要等页面加载完毕才进行
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            // 设置自动弹出软键盘的方法
            // 得到软键盘的管理者
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            // 设置为不是永久隐藏
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}





















