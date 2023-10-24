package com.hejing.tally.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hejing.tally.utils.CalculateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责管理数据库的类:
 *      主要对于表中的内容进行操作: 增删改查
 */
public class DBManager {
    private static SQLiteDatabase db;  // 数据库对象

    // 初始化数据库对象
    public static void initDB(Context context) {
        DBOpenHelper dbHelper = new DBOpenHelper(context);  // 得到帮助类对象
        db = dbHelper.getWritableDatabase();  // 得到数据库对象: 既可读也可写
    }

    /**
     * 读取数据库当中的数据，写入内存集合中
     *      kind: 根据kind数据类型进行查找数据，为1表示收入，为-1表示支出
     */
    // 此处的 @SuppressLint("Range")作用是消除方法内的警告信息
    @SuppressLint("Range")
    public static List<TypeBean> getTypeList(int kind) {
        List<TypeBean> list = new ArrayList<>();
        // 读取 type_tb 中的数据
        String read_sql = "select * from type_tb where kind = " + kind;
        Cursor cursor = db.rawQuery(read_sql, null);// 第二个参数是占位符
        // 循环读取游标内容，存储到对象当中
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind1 = cursor.getInt(cursor.getColumnIndex("kind"));
            TypeBean typeBean = new TypeBean(id, typeName, imageId, sImageId, kind1);
            list.add(typeBean);
        }
        cursor.close();
        return list;
    }

    /**
     * 向记账表中插入一条元素
     */
    public static void insertItemToAccountTb(AccountBean bean) {
        // 出错了，这里的getMonth()，说明保存到数据库的数据源month全是10 ,什么原因?
        // Log.i("ting", "database: month: " + bean.getMonth());
        ContentValues values = new ContentValues();
        values.put("typeName", bean.getTypeName());
        values.put("sImageId", bean.getsImageId());
        values.put("remark", bean.getRemark());
        values.put("money", bean.getMoney());
        values.put("time", bean.getTime());
        values.put("year", bean.getYear());
        values.put("month", bean.getMonth());
        values.put("day", bean.getDay());
        values.put("kind", bean.getKind());
        db.insert("account_tb", null, values);
        // Log.i("ting", "insertItemToAccountTb: ok!!!");
    }

    /**
     *  获取记账表中某一天的所有支出或者收入情况
     */
    @SuppressLint("range")
    public static List<AccountBean> getAccountListOneDayFromAccountTb(int year, int month, int day) {
        List<AccountBean> list = new ArrayList<>();
        String query_sql = "select * from account_tb where year=? and month=? and day=? order by id desc";  // 就近排列
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", month + "", day + ""});
        // 遍历查询出来的数据
        while (cursor.moveToNext()) {
             int id = cursor.getInt(cursor.getColumnIndex("id"));
             String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
             String time = cursor.getString(cursor.getColumnIndex("time"));
             String remark = cursor.getString(cursor.getColumnIndex("remark"));
             int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
             int kind = cursor.getInt(cursor.getColumnIndex("kind"));
             double money = cursor.getDouble(cursor.getColumnIndex("money"));
             AccountBean accountBean = new AccountBean(id, typeName, sImageId, remark, money, time, year, month, day, kind);
             list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /**
     *  获取记账表中某一月的所有支出或者收入情况
     */
    @SuppressLint("range")
    public static List<AccountBean> getAccountListOneMonthFromAccountTb(int year, int month) {
        List<AccountBean> list = new ArrayList<>();
        String query_sql = "select * from account_tb where year=? and month=? order by id desc";  // 就近排列
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", month + ""});
        Log.i("ting", year + ": " + month + "这这这");
        // 遍历查询出来的数据
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            double money = cursor.getDouble(cursor.getColumnIndex("money"));

            AccountBean accountBean = new AccountBean(id, typeName, sImageId, remark, money, time, year, month, day, kind);
            list.add(accountBean);
            Log.i("ting", time + ": " + money + ": " + typeName + "执行了");
            Log.i("ting", "牛逼" + cursor.getInt(cursor.getColumnIndex("month")));
        }

        cursor.close();
        return list;
    }

    /**
     * 获取某一天的支出或者收入的总金额 kind: 支出==-1， 收入==1
     */
    @SuppressLint("range")
    public static double getSumMoneyOneDayForKind(int year, int month, int day, int kind) {
        double total = 0.0;
        String query_sql = "select sum(money) from account_tb where year=? and " +
                "month=? and day=? and kind=?";
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", month + "", day + "", +kind + ""});
        // 遍历:
        if (cursor.moveToNext()) {
            double money = cursor.getDouble(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        cursor.close();
        return total;
    }

    /**
     * 获取某一月的支出或者收入的总金额 kind: 支出==-1， 收入==1
     */
    @SuppressLint("range")
    public static double getSumMoneyOneMonthForKind(int year, int month, int kind) {
        double total = 0.0;
        String query_sql = "select sum(money) from account_tb where year=? and " +
                "month=? and kind=?";
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", month + "", +kind + ""});
        // 遍历:
        if (cursor.moveToNext()) {
            double money = cursor.getDouble(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        cursor.close();
        return total;
    }

    /**
     * 统计某月份支出或者收入情况有多少条？
     */
    @SuppressLint("range")
    public static int getCountItemOneMonthForKind(int year, int month, int kind) {
        int total = 0;
        String query_sql = "select count(money) from account_tb where year=? and month=? and kind=?";
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToNext()) {
            int count = cursor.getInt(cursor.getColumnIndex("count(money)"));  // 得到查询出来的数量
            total = count;
        }
        return total;
    }

    /**
     * 获取某一年的支出或者收入的总金额 kind: 支出==-1， 收入==1
     */
    @SuppressLint("range")
    public static double getSumMoneyOneYearForKind(int year, int kind) {
        double total = 0.0;
        String query_sql = "select sum(money) from account_tb where year=? and kind=?";
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", +kind + ""});
        // 遍历:
        if (cursor.moveToNext()) {
            double money = cursor.getDouble(cursor.getColumnIndex("sum(money)"));
            total = money;
        }
        cursor.close();
        return total;
    }

    /**
     * 对于每一种类型(typeName)，查询指定年份和月份下收入或支出的总钱数
     */
    @SuppressLint("range")
    public static List<ChartItemBean> getChartListFromAccountTb(int year, int month, int kind) {
        List<ChartItemBean> list = new ArrayList<>();
        // 求出支出或收入的总钱数
        double sumMoneyOneMonth = getSumMoneyOneMonthForKind(year, month, kind);
        String query_sql = "select typeName,sImageId,sum(money)as total from account_tb "
                + "where year=? and month=? and kind=? group by typeName order by total desc";
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            double total = cursor.getDouble(cursor.getColumnIndex("total"));
            // 计算所占百分比 ratio = total / sumMoneyOneMonth, 保留四位小数
            double ratio = CalculateUtils.div(total, sumMoneyOneMonth);
            ChartItemBean bean = new ChartItemBean(sImageId, typeName, ratio, total);
            list.add(bean);
        }
        cursor.close();
        return list;
    }


    /**
     * 根据传入的id，删除account_tb表中的一条数据
     */
    public static int deleteItemFromAccountTbById(int id) {
        int i = db.delete("account_tb", "id=?", new String[]{id + ""});
        return i;
    }

    /**
     * 根据备注 remark 搜索收入或者支出的情况列表
     */
    @SuppressLint("range")
    public static List<AccountBean> getAccountListByRemarkFromAccountTb(String remark_msg) {
        List<AccountBean> list = new ArrayList<>();
        // 模糊查询
        String query_sql = "select * from account_tb where remark like '%" + remark_msg + "%'";
        Cursor cursor = db.rawQuery(query_sql, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String typeName = cursor.getString(cursor.getColumnIndex("typeName"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            int sImageId = cursor.getInt(cursor.getColumnIndex("sImageId"));
            String remark = cursor.getString(cursor.getColumnIndex("remark"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            double money = cursor.getDouble(cursor.getColumnIndex("money"));
            AccountBean accountBean =
                    new AccountBean(id, typeName, sImageId, remark, money, time, year, month, day, kind);
            list.add(accountBean);
        }
        cursor.close();
        return list;
    }

    /**
     * 查询记账表中有几个年份的信息
     */
    @SuppressLint("range")
    public static List<Integer> getYearListFromAccountTb() {
        List<Integer> list = new ArrayList<>();
        String query_sql = "select distinct(year) from account_tb order by year asc";
        Cursor cursor = db.rawQuery(query_sql, null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            list.add(year);
        }
        cursor.close();
        return list;
    }

    /**
     * 获取这个月中收入支出最大的金额
     */
    @SuppressLint("range")
    public static double getMaxMoneyOneDayInMonth(int year, int month, int kind) {
        double money = 0.0;
        String query_sql = "select sum(money) from account_tb where year=? " +
                "and month=? and kind=? group by day order by sum(money) desc";
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            money = cursor.getDouble(cursor.getColumnIndex("sum(money)"));
        }
        return money;
    }

    /**
     * 根据指定月份获取每日收入或者支出的总钱数
     */
    @SuppressLint("range")
    public static List<BarChartItemBean> getSumMoneyOneDayInMonth(int year, int month, int kind) {
        List<BarChartItemBean> list = new ArrayList<>();
        String query_sql = "select day,sum(money) from account_tb where year=? and month=? " +
                "and kind=? group by day";
        Cursor cursor = db.rawQuery(query_sql, new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            double sumMoney = cursor.getDouble(cursor.getColumnIndex("sum(money)"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, sumMoney);
            list.add(itemBean);
        }
        cursor.close();
        return list;

    }

    /**
     * 删除account_tb表格中的所有数据
     */
    public static void deleteAllAccount() {
        String del_sql = "delete from account_tb";
        db.execSQL(del_sql);
    }
}






























