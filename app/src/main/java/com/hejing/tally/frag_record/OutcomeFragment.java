package com.hejing.tally.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hejing.tally.R;
import com.hejing.tally.db.AccountBean;
import com.hejing.tally.db.DBManager;
import com.hejing.tally.db.TypeBean;
import com.hejing.tally.utils.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OutcomeFragment extends BaseRecordFragment {
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        // 获取数据库中的数据源
        List<TypeBean> outList = DBManager.getTypeList(-1);  // 支出数据
        getTypeBeanList().addAll(outList);
        getAdapter().notifyDataSetChanged();  // 适配器更新
        // 设置默认选中的数据源类型的title和icon
        getAccountBean().setTypeName("其它");
        getAccountBean().setsImageId(R.mipmap.ic_qita_fs);
        getTypeTv().setText("其它");
        getTypeIv().setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {
        getAccountBean().setKind(-1);  // 支出值为-1
        DBManager.insertItemToAccountTb(getAccountBean());
    }
}





















