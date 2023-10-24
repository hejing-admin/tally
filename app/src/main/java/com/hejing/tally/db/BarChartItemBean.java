package com.hejing.tally.db;

/**
 * 用于描述绘制柱状图时每一根柱子表示的对象
 */
public class BarChartItemBean {
    private int year;
    private int month;
    private int day;
    private double summoney;

    public BarChartItemBean() {

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

    public double getSummoney() {
        return summoney;
    }

    public void setSummoney(double summoney) {
        this.summoney = summoney;
    }

    public BarChartItemBean(int year, int month, int day, double summoney) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.summoney = summoney;
    }
}


























