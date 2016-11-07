/*
 * Copyright (c) 2009 Your Corporation. All Rights Reserved.
 */

package com.thinkive.service.service;

import com.thinkive.base.jdbc.DataRow;

import org.apache.log4j.Logger;

import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:  资讯文件的操作
 * 版权:	 Copyright (c) 2008
 * 公司:	 思迪科技
 * 作者:	 岳知之
 * 版本:	 1.0
 * 创建日期: 2009-2-11
 * 创建时间: 下午04:50:03
 */
public class InfoFileService {
    /**
     * 每个及节点的长度
     */
    public final static int STRUCT_LENGTH = 256;
    /**
     * 文件头长度
     */
    public final static int HEAD_LENGTH = 16;
    private static Logger logger = Logger.getLogger(InfoFileService.class);

    public static void main(String[] args) {
        InfoFileService fileService = new InfoFileService();
        List list = fileService.findInfoListByPath("D:\\演示\\data\\report\\info.idx");

    }

    public static int readInt(byte abyte0[], int i) {
        int j = abyte0[i] & 0xff;
        int k = abyte0[i + 1] & 0xff;
        int l = abyte0[i + 2] & 0xff;
        int i1 = abyte0[i + 3] & 0xff;
        int j1 = j | k << 8 | l << 16 | i1 << 24;
        return j1;
    }

    public static int readShort(byte abyte0[], int i) {
        int j = abyte0[i] & 0xff;
        int k = abyte0[i + 1] & 0xff;
        int l = j | k << 8;
        return l;
    }

    public static float readFloat(byte abyte0[], int i) {
        int j = (abyte0[i + 3] & 0x80) >> 7;
        int k = (abyte0[i + 3] & 0x7f) << 1 | (abyte0[i + 2] & 0x80) >> 7;
        int l = (abyte0[i + 2] & 0x7f | 0x80) << 16 | (abyte0[i + 1] & 0xff) << 8 | abyte0[i] & 0xff;
        int i1 = (23 - k) + 127;
        float f = l;
        if (i1 > 0) {
            for (int j1 = 0; j1 < i1; j1++) {
                f /= 2.0F;
            }

        } else {
            for (int k1 = 0; k1 > i1; k1--) {
                f *= 2.0F;
            }

        }
        if (j != 0) {
            f = -f;
        }
        if ((double) f < 1.0000000000000001E-005D) {
            f = 0.0F;
        }
        return f;
    }

    /**
     * 描述：根据索引位置查找节点信息
     * 作者：岳知之
     * 时间：2009-2-11 下午05:23:58
     *
     * @param filePath 文件路径
     * @param index    索引位置
     */
    public DataRow findNodeInfoByIndex(String filePath, int index) {
        DataRow info = new DataRow();
        RandomAccessFile raFile = null;
        try {
            raFile = new RandomAccessFile(filePath, "r");
            int fileLength = (int) raFile.length();
            byte head[] = new byte[HEAD_LENGTH];
            int num = fileLength / STRUCT_LENGTH;
            raFile.seek(0);
            raFile.readFully(head);
            // 得到栏目的长度
            int count = readShort(head, 10);
            if (num <= count) {
                byte textIndex[] = new byte[STRUCT_LENGTH];
                raFile.seek(index * STRUCT_LENGTH + HEAD_LENGTH);
                raFile.readFully(textIndex, 0, STRUCT_LENGTH);
                String name = new String(textIndex, 11, 64).trim();
                String path = new String(textIndex, 75, 80).trim();
                int date = readInt(textIndex, 3);
                int time = readInt(textIndex, 7);
                int offset = readInt(textIndex, 155);//偏移
                int length = readInt(textIndex, 159);//长度
                info.set("name", name);
                info.set("path", path);
                info.set("date", date);
                info.set("time", time);
                info.set("offset", offset);
                info.set("length", length);
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {

                if (raFile != null) {
                    raFile.close();
                }
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
        return info;
    }

    /**
     * 描述：
     * 作者：岳知之
     * 时间：2009-2-11 下午04:51:22
     */
    public String findContentByPath(String filePath, int offset, int length) {
        String text = "";
        RandomAccessFile raFile = null;
        try {
            byte content[] = new byte[length];
            raFile = new RandomAccessFile(filePath, "r");
            raFile.seek(offset);
            raFile.readFully(content);
            text = new String(content);
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {
                if (raFile != null) {
                    raFile.close();
                }
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
        return text;
    }

    /**
     * 描述：读取资讯文件的索引文件，返回节点信息
     * 作者：岳知之
     * 时间：2009-2-11 下午04:51:25
     */
    public List findInfoListByPath(String filePath) {
        List infoList = new ArrayList();
        RandomAccessFile raFile = null;
        try {
            raFile = new RandomAccessFile(filePath, "r");
            int fileLength = (int) raFile.length();
            byte head[] = new byte[HEAD_LENGTH];
            int num = fileLength / STRUCT_LENGTH;
            raFile.seek(0);
            raFile.readFully(head);
            // 得到栏目的长度
            int count = readShort(head, 10);
            raFile.seek(16);
            if (num <= count) {
                for (int p = 0; p < num; p++) {
                    byte textIndex[] = new byte[STRUCT_LENGTH];

                    raFile.seek(p * STRUCT_LENGTH + HEAD_LENGTH);
                    raFile.readFully(textIndex, 0, STRUCT_LENGTH);
                    String title = new String(textIndex, 11, 64).trim();
                    if (title.indexOf(0) > 0) {
                        title = title.substring(0, title.indexOf(0));
                    }
                    String path = new String(textIndex, 75, 80).trim();
                    int date = readInt(textIndex, 3);
                    int time = readInt(textIndex, 7);
                    //int time2 = readInt(textIndex, 61);
                    int offset = readInt(textIndex, 155);//偏移
                    int length = readInt(textIndex, 159);//长度

                    DataRow dataRow = new DataRow();
                    dataRow.set("title", title);
                    dataRow.set("date", date);
                    dataRow.set("time", time);
                    dataRow.set("offset", offset);
                    dataRow.set("length", length);
                    infoList.add(dataRow);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            try {

                if (raFile != null) {
                    raFile.close();
                }
            } catch (Exception ex) {
                logger.error("", ex);
            }
        }
        return infoList;
    }
}
