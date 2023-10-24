package com.hejing.tally.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.hejing.tally.R;
import com.hejing.tally.db.AccountBean;
import com.hejing.tally.db.TypeBean;
import com.hejing.tally.utils.KeyBoardUtils;
import com.hejing.tally.utils.RemarkDialog;
import com.hejing.tally.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 记录页面当中的支出模块
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private KeyboardView keyboardView;  // 键盘视图
    private EditText moneyEt;  // 编辑money的视图
    private ImageView typeIv;  // 收入支出类型的图标
    private TextView typeTv;  // 收入支出类型的title
    private TextView remarkTv;  // 备注
    private TextView timeTv;  // 时间
    private GridView typeGv;  // GridView的整体视图
    private List<TypeBean> typeBeanList;  // 公共的数据集合
    private TypeBaseAdapter adapter;  // 适配器实例化对象
    private AccountBean accountBean;  // 将需要插入到记账本当中的数据保存成对象的的形式

    public KeyboardView getKeyboardView() {
        return keyboardView;
    }

    public void setKeyboardView(KeyboardView keyboardView) {
        this.keyboardView = keyboardView;
    }

    public EditText getMoneyEt() {
        return moneyEt;
    }

    public void setMoneyEt(EditText moneyEt) {
        this.moneyEt = moneyEt;
    }

    public ImageView getTypeIv() {
        return typeIv;
    }

    public void setTypeIv(ImageView typeIv) {
        this.typeIv = typeIv;
    }

    public TextView getTypeTv() {
        return typeTv;
    }

    public void setTypeTv(TextView typeTv) {
        this.typeTv = typeTv;
    }

    public TextView getRemarkTv() {
        return remarkTv;
    }

    public void setRemarkTv(TextView remarkTv) {
        this.remarkTv = remarkTv;
    }

    public TextView getTimeTv() {
        return timeTv;
    }

    public void setTimeTv(TextView timeTv) {
        this.timeTv = timeTv;
    }

    public GridView getTypeGv() {
        return typeGv;
    }

    public void setTypeGv(GridView typeGv) {
        this.typeGv = typeGv;
    }

    public List<TypeBean> getTypeBeanList() {
        return typeBeanList;
    }

    public void setTypeBeanList(List<TypeBean> typeBeanList) {
        this.typeBeanList = typeBeanList;
    }

    public TypeBaseAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(TypeBaseAdapter adapter) {
        this.adapter = adapter;
    }

    public AccountBean getAccountBean() {
        return accountBean;
    }

    public void setAccountBean(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    public BaseRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化数据源对象
        setAccountBean(new AccountBean());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // inflate方法的作用是将一个 xml 布局文件变成一个 view 对象
        View view = inflater.inflate(R.layout.fragment_base_record, container, false);

        // 初始化视图
        initView(view);

        // 设置操作的时间记录:
        setInitTime();

        // 从数据库中读取数据并填充到GridView中
        // 该方法需要扩展
        // 该方法由具体的子类来重载实现，以满足支出、收入等不同类型数据源的设置操作要求。
        loadDataToGV();

        // 设置GridView每一项的点击事件
        setGVListener();

        return view;
    }

    /**
     * 设置初始化时间: 获取当前时间，显示在timeTv上，并保存在记录对象中
     * 如果用户没有点击修改时间，则保存到数据库中的年月日就是当前时间
     */
    private void setInitTime() {
        // 将时区设置为东八区
        TimeZone time_ = TimeZone.getTimeZone("Etc/GMT-8");  // 转换为中国时区
        TimeZone.setDefault(time_);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        accountBean.setTime(time);
        timeTv.setText(time);

        // 获取年月日
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);               // 获取当年
        int month = calendar.get(Calendar.MONTH) + 1;         // 获取当月
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH); // 获取当日
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(dayOfMonth);
        // Log.i("ting", "我后执行? month: " + month);
    }

    /**
     * 设置GridView每一项的点击事件
     */
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;  // 更新当前点击项目的位置
                adapter.notifyDataSetInvalidated();  // 提示绘制发生变化
                TypeBean typeBean = typeBeanList.get(position);
                String typeName = typeBean.getTypeName();
                typeTv.setText(typeName);
                accountBean.setTypeName(typeName);  // 更新记录对象
                int sImageId = typeBean.getsImageId();
                typeIv.setImageResource(sImageId);
                accountBean.setsImageId(sImageId);  // 更新记录对象

            }
        });
    }

    /**
     * 从数据库中读取数据并填充到GridView中
     * 该方法需要由具体的子类进行重载，以满足收入、支出等不同数据源类型的设置要求。
     */
    public void loadDataToGV() {
        typeBeanList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeBeanList);
        typeGv.setAdapter(adapter);
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        remarkTv = view.findViewById(R.id.frag_record_tv_remark);
        timeTv = view.findViewById(R.id.frag_record_tv_time);

        remarkTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        // typeIv.setImageResource(R.mipmap.in_qt_fs);  此处不应该在基类中确定，而需要在子类中进行个性化定制

        // 让自定义软键盘显示出来
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();  // 显示软键盘
        // 设置接口，监听确定按钮的点击
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                // 点击了确定按钮之后的操作
                // 获取输入的money，并作逻辑判断
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("0")) {
                    // getActivity(): 返回一个和此fragment绑定的FragmentActivity或者其子类的实例
                    getActivity().finish();
                    return;
                }
                double money = Double.parseDouble(moneyStr);
                accountBean.setMoney(money);
                // 获取记录的信息，保存到数据库
                saveAccountToDB();
                // 返回到上一级页面
                getActivity().finish();
            }
        });
    }

    /**
     * 让子类一定要重写这个方法
     */
    public abstract void saveAccountToDB();

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.frag_record_tv_time) {  // 点击了"时间"
            showTimeDialog();
        } else if (view.getId() == R.id.frag_record_tv_remark) {  // 点击了"备注"
            showRemarkDialog();
        }
    }

    /**
     * 弹出显示时间的对话框
     */
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        // 设置确定按钮被点击了的监听器
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month); // 卧槽，瞎了
                accountBean.setDay(day);
                // 显示正确
                Log.i("ting", "baseRecord中的year: " + year + " month: " + month);
            }
        });
    }

    /**
     * 弹出显示备注的对话框
     */
    private void showRemarkDialog() {
        RemarkDialog dialog = new RemarkDialog(getContext());
        dialog.show();  // 显示对话框样式
        dialog.setDialogSize();  // 设置对话框尺寸格式
        // 对弹出的备注对话框中的确定按钮的点击监听: (回调方式)
        dialog.setOnEnsureListener(new RemarkDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();  // 得到输入的备注信息
                if (!TextUtils.isEmpty(msg)) {  // 如果输入的信息不为空,则显示
                    remarkTv.setText(msg);
                    accountBean.setRemark(msg);
                }
                dialog.cancel();  // 执行完上述操作之后，结束对话框
            }
        });
    }
}





















