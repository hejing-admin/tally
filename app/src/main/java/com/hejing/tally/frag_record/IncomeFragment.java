package com.hejing.tally.frag_record;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hejing.tally.R;
import com.hejing.tally.db.AccountBean;
import com.hejing.tally.db.DBManager;
import com.hejing.tally.db.TypeBean;

import java.util.List;


public class IncomeFragment extends BaseRecordFragment {
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        // 获取数据库中的数据源
        List<TypeBean> inList = DBManager.getTypeList(1);  // 收入数据
        getTypeBeanList().addAll(inList);
        getAdapter().notifyDataSetChanged();  // 适配器更新
        // 设置默认选中的数据源类型的title和icon
        getAccountBean().setTypeName("其它");
        getAccountBean().setsImageId(R.mipmap.in_qt_fs);

        getTypeTv().setText("其它");
        getTypeIv().setImageResource(R.mipmap.in_qt_fs);
    }

    @Override
    public void saveAccountToDB() {
        getAccountBean().setKind(1);  // 收入类型值为1
        // 这里出错了，保存到数据库中的month 值全部变成了10 ?
        // Log.i("ting", "保存到数据库的month: " + getAccountBean().getMonth());
        DBManager.insertItemToAccountTb(getAccountBean());
    }
}












