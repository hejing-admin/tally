package com.hejing.tally.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.hejing.tally.R;

public class KeyBoardUtils {
    private final Keyboard keyboard;  // 自定义键盘
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener {
        void onEnsure();
    }
    OnEnsureListener onEnsureListener;

    /**
     * 设定接口回调方法
     * @param onEnsureListener
     */
    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);  // 取消弹出系统键盘
        keyboard = new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(keyboard);  // 设置要显示的键盘的样式
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener);  // 设置键盘按钮被点击了的监听
    }
    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();
            switch(primaryCode) {
                case Keyboard.KEYCODE_DELETE:  // -5，点击了删除键
                    if (editable != null && editable.length() > 0 && start > 0) {
                        editable.delete(start - 1, start);
                    }
                    break;
                case Keyboard.KEYCODE_CANCEL:  // -3，点击了清零键
                    editable.clear();
                    break;
                case Keyboard.KEYCODE_DONE:  // -4，点击了确定键
                    onEnsureListener.onEnsure(); // 通过接口回调的方法，当点击确定键，可以调用这个方法，传出数据
                    break;
                default:  // 点击了正常的数字键
                    // 其它正常数字，直接插入
                    editable.insert(start, Character.toString((char)primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence charSequence) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    // 显示键盘
    public void showKeyboard() {
        // visible: 可见的
        // invisible: 不可见的
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);  // 显示
        }
    }

    // 隐藏键盘
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);  // 隐藏键盘
        }
    }
}




























