package com.thinkive.market.bean;


/**
 * @����: ������ȨK�����ݣ��������� ���� ���� ����ʷ���ݴ洢
 * @��Ȩ: Copyright (c) 2015
 * @��˾: ˼�ϿƼ�
 * @����: ����
 * @�汾: 1.0
 * @��������: 2015-1-19
 * @����ʱ��: ����10:28:51
 */
public class KLineData {
    private int date;    //����  4

    private float open;    //���̼�  4

    private float high;    //��߼�

    private float low;     //��ͼ�

    private float close;   //���̼�

    private double money;   //�ɽ����  8

    private long volume;  //�ɽ���   8

    private float yesClose; //�������̼�
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
