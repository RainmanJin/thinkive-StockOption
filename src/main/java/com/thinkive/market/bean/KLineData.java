package com.thinkive.market.bean;


/**
 * @描述: 个股期权K线数据，用于日线 月线 周线 等历史数据存储
 * @版权: Copyright (c) 2015
 * @公司: 思迪科技
 * @作者: 熊攀
 * @版本: 1.0
 * @创建日期: 2015-1-19
 * @创建时间: 上午10:28:51
 */
public class KLineData {
    private int date;    //日期  4

    private float open;    //开盘价  4

    private float high;    //最高价

    private float low;     //最低价

    private float close;   //收盘价

    private double money;   //成交金额  8

    private long volume;  //成交量   8

    private float yesClose; //昨日收盘价
    private int[] ma = new int[6];//xiongpan 2014-11-04

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public float getYesClose() {
        return yesClose;
    }

    public void setYesClose(float yesClose) {
        this.yesClose = yesClose;
    }

    public int[] getMa() {
        return ma;
    }

    public void setMa(int[] ma) {
        this.ma = ma;
    }

}
