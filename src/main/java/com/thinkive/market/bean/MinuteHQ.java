package com.thinkive.market.bean;

/**
 * ����: ������Ȩ��ʱ��������
 * ��Ȩ: Copyright (c) 2007
 * ��˾: ˼�ϿƼ�
 * ����: ����
 * �汾: 1.0
 * ��������: 2015-1-17
 * ����ʱ��: 15:56:01
 */
public class MinuteHQ {

    //private int date;// ���� ���շ�ʱ��Ҫ����ֶ� xiongpan 214-10-07

    private short minute; // ������
    private float now; // �ּ�
    private int thedeal; // �ɽ���
    private float average; // ����

    private float yesterday;
    private long TotalLongPosition;//��ǰ�ֲ���


    private float lead;

    public long getTotalLongPosition() {
        return TotalLongPosition;
    }

    public void setTotalLongPosition(long totalLongPosition) {
        TotalLongPosition = totalLongPosition;
    }


    public float getYesterday() {
        return yesterday;
    }

    public void setYesterday(float yesterday) {
        this.yesterday = yesterday;
    }

    /**
     * ��÷�����
     */
    public short getMinute() {
        return minute;
    }

    /**
     * ���÷�����
     */
    public void setMinute(short minute) {
        this.minute = minute;
    }

    /**
     * ����ּ�
     */
    public float getNow() {
        return now;
    }

    /**
     * �����ּ�
     */
    public void setNow(float now) {
        this.now = now;
    }

    /**
     * ��óɽ���
     */
    public int getThedeal() {
        return thedeal;
    }

    /**
     * ���óɽ���
     */
    public void setThedeal(int thedeal) {
        this.thedeal = thedeal;
    }

    /**
     * ��þ���
     */
    public float getAverage() {
        return average;
    }

    /**
     * ���þ���
     */
    public void setAverage(float average) {
        this.average = average;
    }

    public float getLead() {
        return lead;
    }

    public void setLead(float lead) {
        this.lead = lead;
    }

}
