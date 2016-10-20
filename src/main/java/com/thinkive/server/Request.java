package com.thinkive.server;

/**
 * 描述:
 * 版权:	 Copyright (c) 2006-2012
 * 公司:	 思迪科技
 * 作者:	 易庆锋
 * 版本:	 2.0
 * 创建日期: 2012-10-29
 * 创建时间: 15:30:10
 */
public interface Request
{
    /**
     * 获取功能号
     *
     * @return
     */
    public int getFuncNo();

    /**
     * 获取分支号ID
     *
     * @return
     */
    public int getBranchNo();

    /**
     * 获取流水号
     *
     * @return
     */
    public int getFlowNo();

    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersionNo();

    /**
     * 获取包体数据
     *
     * @return
     */
    public byte[] getData();


}
