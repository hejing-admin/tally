package com.hejing.tally.db;

/**
 * 描述记录一条数据的相关内容类
 */
public class AccountBean {
    int id;
    String typeName;  // 类型
    int sImageId;  // 被选中类型的图片
    String remark;  // 备注
    double money;  // 价格
    String time;  // 保存时间字符串
    int year;
    int month;
    int day;
    int kind;  // 类型: 是收入(1)还是支出(-1)

    public AccountBean() {

    }
    public AccountBean(int id, String typeName, int sImageId, String remark, double money, String time, int year, int month, int day, int kind) {
        this.id = id;
        this.typeName = typeName;
        this.sImageId = sImageId;
        this.remark = remark;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}

































