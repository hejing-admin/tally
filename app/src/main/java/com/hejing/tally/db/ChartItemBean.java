package com.hejing.tally.db;

public class ChartItemBean {
    private int sImageId;  // 图标
    private String typeName;  // 名称
    private double ratio;  // 所占比例
    private double totalMoney;  // 此项的总钱数

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public ChartItemBean() {

    }
    public ChartItemBean(int sImageId, String typeName, double ratio, double totalMoney) {
        this.sImageId = sImageId;
        this.typeName = typeName;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }


}


























