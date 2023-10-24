package com.hejing.tally.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hejing.tally.R;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tally.db";  // 数据库的名称
    private static final int DB_VERSION = 1;  // 数据库的版本号
    public static final String TABLE_NAME = "type_tb";  // 数据源类型表的名称
    public static final String TABLE_ACCOUNT = "account_tb";  // 账本信息表
    public DBOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 创建数据库的方法，只有项目第一次运行时会被调用
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表示数据源类型的表
        String create_sql = "create table if not exists " + TABLE_NAME + " ("
                + "id integer primary key autoincrement,"
                + "typeName varchar(10),"
                + "imageId integer,"
                + "sImageId integer,"
                + "kind integer);";
        db.execSQL(create_sql);  // 执行建表语句
        insertType(db);

        // 创建记账表
        create_sql = "create table if not exists " + TABLE_ACCOUNT + " ("
                + "id integer primary key autoincrement,"
                + "typeName varchar(10),"
                + "sImageId integer,"
                + "remark varchar(80),"
                + "money double,"
                + "time varchar(60),"
                + "year integer,"
                + "month integer,"
                + "day integer,"
                + "kind integer);";
        db.execSQL(create_sql);  // 执行建表语句


    }

    /**
     * 向type_tb表中插入数据
     * @param db
     */
    private void insertType(SQLiteDatabase db) {
        String insert_sql = "insert into type_tb (typeName,imageId,sImageId,kind) values (?,?,?,?);";
        // 支出类型数据
        db.execSQL(insert_sql, new Object[]{"其它", R.mipmap.ic_qita,R.mipmap.ic_qita_fs,-1});
        db.execSQL(insert_sql, new Object[]{"餐饮", R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,-1});
        db.execSQL(insert_sql, new Object[]{"交通", R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,-1});
        db.execSQL(insert_sql, new Object[]{"购物", R.mipmap.ic_gouwu,R.mipmap.ic_gouwu_fs,-1});
        db.execSQL(insert_sql, new Object[]{"服饰", R.mipmap.ic_gouwu,R.mipmap.ic_fushi_fs,-1});
        db.execSQL(insert_sql, new Object[]{"日用品", R.mipmap.ic_riyongpin,R.mipmap.ic_riyongpin_fs,-1});
        db.execSQL(insert_sql, new Object[]{"娱乐", R.mipmap.ic_yule,R.mipmap.ic_yule_fs,-1});
        db.execSQL(insert_sql, new Object[]{"零食", R.mipmap.ic_lingshi,R.mipmap.ic_lingshi_fs,-1});
        db.execSQL(insert_sql, new Object[]{"烟酒茶", R.mipmap.ic_yanjiu,R.mipmap.ic_yanjiu_fs,-1});
        db.execSQL(insert_sql, new Object[]{"学习", R.mipmap.ic_xuexi,R.mipmap.ic_xuexi_fs,-1});
        db.execSQL(insert_sql, new Object[]{"医疗", R.mipmap.ic_yiliao,R.mipmap.ic_yiliao_fs,-1});
        db.execSQL(insert_sql, new Object[]{"住宅", R.mipmap.ic_zhufang,R.mipmap.ic_zhufang_fs,-1});
        db.execSQL(insert_sql, new Object[]{"水电煤", R.mipmap.ic_shuidianfei,R.mipmap.ic_shuidianfei_fs,-1});
        db.execSQL(insert_sql, new Object[]{"通讯", R.mipmap.ic_tongxun,R.mipmap.ic_tongxun_fs,-1});
        db.execSQL(insert_sql, new Object[]{"人情往来", R.mipmap.ic_renqingwanglai,R.mipmap.ic_renqingwanglai_fs,-1});

        // 收入类型数据
        db.execSQL(insert_sql, new Object[]{"其它", R.mipmap.in_qt,R.mipmap.in_qt_fs,1});
        db.execSQL(insert_sql, new Object[]{"薪资", R.mipmap.in_xinzi,R.mipmap.in_xinzi_fs,1});
        db.execSQL(insert_sql, new Object[]{"奖金", R.mipmap.in_jiangjin,R.mipmap.in_jiangjin_fs,1});
        db.execSQL(insert_sql, new Object[]{"借入", R.mipmap.in_jieru,R.mipmap.in_jieru_fs,1});
        db.execSQL(insert_sql, new Object[]{"收债", R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,1});
        db.execSQL(insert_sql, new Object[]{"利息收入", R.mipmap.in_lixifuji,R.mipmap.in_lixifuji_fs,1});
        db.execSQL(insert_sql, new Object[]{"投资回报", R.mipmap.in_touzi,R.mipmap.in_touzi_fs,1});
        db.execSQL(insert_sql, new Object[]{"二手交易", R.mipmap.in_ershoushebei,R.mipmap.in_ershoushebei_fs,1});
        db.execSQL(insert_sql, new Object[]{"意外所得", R.mipmap.in_yiwai,R.mipmap.in_yiwai_fs,1});
    }


    /**
     * 数据库版更新时会调用该方法
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
































