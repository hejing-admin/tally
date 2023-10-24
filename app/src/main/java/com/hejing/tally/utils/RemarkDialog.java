package com.hejing.tally.utils;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hejing.tally.R;

public class RemarkDialog extends Dialog implements View.OnClickListener{

    private EditText et;
    private Button cancelBtn;
    private Button ensureBtn;

    // 定义一个接口
    public interface OnEnsureListener {
        void onEnsure();
    }
    // 初始化接口对象
    OnEnsureListener onEnsureListener;
    /**
     * 设定接口回调方法
     * @param onEnsureListener
     */
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public RemarkDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_remark);  // 设置对话框显示布局
        et = findViewById(R.id.dialog_remark_et);
        cancelBtn = findViewById(R.id.dialog_remark_btn_cancel);
        ensureBtn = findViewById(R.id.dialog_remark_btn_ensure);
        // 设置监听事件
        cancelBtn.setOnClickListener(this);
        ensureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.dialog_remark_btn_cancel) {  // 取消按键
            cancel();
        } else if (view.getId() == R.id.dialog_remark_btn_ensure) {  // 确定按键
            if (onEnsureListener != null) {  // 说明调用了接口回调方法
                onEnsureListener.onEnsure();
            }
            cancel();
        }
    }

    // "获取输入数据"的方法
    public String getEditText() {
        return et.getText().toString().trim();
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
        handler.sendEmptyMessageDelayed(1, 100);
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




















