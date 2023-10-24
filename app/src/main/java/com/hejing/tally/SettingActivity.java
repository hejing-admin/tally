package com.hejing.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hejing.tally.db.DBManager;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 设置点击事件：
        findViewById(R.id.setting_clear_all_tv).setOnClickListener(this);  // 清空
        findViewById(R.id.setting_iv_back).setOnClickListener(this);  // 返回
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_iv_back) {  // 返回
            finish();
        } else if (view.getId() == R.id.setting_clear_all_tv) {  // 清空
            showDeleteDialog();
        }
    }

    /**
     * 弹出提示删除的对话框alertDialog
     */
    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除提示").setMessage("您确定要删除所有的收支记录吗?\n注意: 删除后无法恢复，请谨慎选择!!")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBManager.deleteAllAccount();
                        Toast.makeText(SettingActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();  // 创建对话框对象
        alertDialog.show();  // 显示对话框
        // 可以合并为: builder.create().show();
    }
}




























