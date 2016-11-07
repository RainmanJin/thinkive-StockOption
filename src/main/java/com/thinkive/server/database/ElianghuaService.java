package com.thinkive.server.database;

import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.DateHelper;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ElianghuaService extends ProjectBaseService {

    private static Logger logger = Logger.getLogger(ElianghuaService.class);

    private static String DB_NAME = "elianghua";

    private static String CMS_DB = "cms";

    private int type_1 = 32768;                                   // 1000 0000 0000 0000

    private int type_2 = 16384;                                   // 0100 0000 0000 0000

    public static void main(String[] args) {
        ElianghuaService elianghuaService = new ElianghuaService();
        Timestamp timestamp = new Timestamp(DateHelper.parseString("2013-02-08 00:03:27").getTime());

        List list2 = elianghuaService.findStrategyAbitList();
        List list = elianghuaService.findFactRTSignalList("Strategy2", timestamp);

        System.out.println(list.size());

        System.out.println(elianghuaService.findDateList("SZ000001").size());

    }

    //    public int findStockPool()
    //    {
    //        String sql = "select sum(power(2,stockpool-1)) as stockpool from (select distinct stockpool from T_STOCK_CHOOSE)";
    //        return getJdbcTemplate(CMS_DB).queryInt(sql);
    //    }

    /**
     * E������ͼ���� 7002
     */

    public List findAuxiliaryChartList(int count, String stockKey, int type) {
        logger.debug("findAuxiliaryChartList start.");
        StringBuffer stringBuffer = new StringBuffer();
        if (type == type_1) //  ����1
        {
            stringBuffer.append("select * from (select ");
            if (count == 0) {
                count = 999999;
            }
            stringBuffer.append(" top " + count);
            stringBuffer
                    .append(" replace(t2.DateKey,'-','') as DateKey, t2.SBSWave,t2.SBSWaveColor,t2.SBSReady,t2.SBSAlarm,t2.SBSRetrate,t2.SBSFocus,t2.SBSStrength ");
            stringBuffer.append(" from DimSecurity t,FactDayMeasure_Strategy1 t2 ");
            stringBuffer
                    .append(" where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc ) t1 order by t1.DateKey");

        } else if (type == type_2) //  ����2
        {
            logger.debug("findAuxiliaryChartList do.");
            stringBuffer.append("select * from (select ");
            if (count == 0) {
                count = 999999;
            }
            stringBuffer.append(" top " + count);
            stringBuffer.append(" replace(t2.DateKey,'-','') as DateKey,t2.SBS_K,t2.SBS_D,SBS_J ");
            stringBuffer.append(" from DimSecurity t,FactDayMeasure_Strategy2 t2 ");
            stringBuffer
                    .append(" where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc ) t1 order by t1.DateKey");
        } else {
            logger.debug("����ƥ�����");
            return null;
        }
        logger.debug("findAuxiliaryChartList end." + stringBuffer.toString() + "," + stockKey);
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[]{stockKey});

    }

    /**
     * �������л�ȡ��Ҫ��ʾ���ߵı�źͶ�Ӧ�ֶ�
     *
     * @param abit ���Ա��
     * @param type ��ͼ��ͼ  1����ͼ   2����ͼ
     */
    public List findLineFieldNameByAbit(int abit, String type) {
        String sql = "select t.field_name,t.line_no from T_STRATEGY_LINE t where t.abit =? and t.state='1' and t.type = ? order by t.line_no desc";
        return getJdbcTemplate(CMS_DB).query(sql, new Object[]{abit, type});
    }

    /**
     * ��ѯ�źŵ���ź�
     */
    public int findSignCountByAbit(int abit) {
        String sql = "select SUM(t.sign_no) as sbit from T_STRATEGY_SIGN t where t.abit = ? and t.state = '1' ";
        return getJdbcTemplate(CMS_DB).queryInt(sql, new Object[]{abit});
    }

    /**
     * E����������������  7001
     */
    public List findDateDataList(int count, String stockKey, int type) {
        logger.debug("findDateDataList start.");
        StringBuffer stringBuffer = new StringBuffer();
        if (type == type_1) //����1
        {
            stringBuffer.append("select * from (select ");
            if (count == 0) {
                count = 999999;
            }
            stringBuffer.append(" top " + count);
            stringBuffer.append(" replace(t2.DateKey,'-','') as DateKey, t2.KColor,t2.AA,t2.BB,t2.BBColor,t2.Signal ");
            stringBuffer.append(" from DimSecurity t,FactDayMeasure_Strategy1 t2 ");
            stringBuffer
                    .append(" where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc ) t1 order by t1.DateKey");
        } else if (type == type_2) //����2
        {
            stringBuffer.append("select * from (select ");
            if (count == 0) {
                count = 999999;
            }
            stringBuffer.append(" top " + count);
            stringBuffer
                    .append(" replace(t2.DateKey,'-','') as DateKey,t2.MA,t2.MAColor,t2.Stress,t2.Surpport,t2.Signal ");
            stringBuffer.append(" from DimSecurity t,FactDayMeasure_Strategy2 t2 ");
            stringBuffer
                    .append(" where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc ) t1 order by t1.DateKey");
        } else {
            logger.debug("����ƥ�����");
            return null;
        }

        logger.debug("findDateDataList end." + stringBuffer.toString() + "," + stockKey);

        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[]{stockKey});
    }

    /**
     * E�����ź��ѹ�
     */
    public List findDataBySignal(int type, int infoType, String dateStr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (type == type_1) //����1
        {
            stringBuffer
                    .append("select t.SecurityCode from DimSecurity t,FactDayMeasure_Strategy1 t2 where t2.Signal = ? and t2.DateKey=? and t2.SecurityKey = t.SecurityKey ");
        } else if (type == type_2) //����2
        {
            stringBuffer
                    .append("select t.SecurityCode from DimSecurity t,FactDayMeasure_Strategy2 t2 where t2.Signal = ? and  t2.DateKey=? and t2.SecurityKey = t.SecurityKey ");
        } else {
            logger.debug("����ƥ�����");
            return null;
        }
        logger.debug("7003 findDataBySignal end." + stringBuffer.toString() + "," + type + "," + infoType + ","
                + dateStr);
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[]{infoType, dateStr});
    }

    /**
     * ȡ����������
     */

    public List findDateList(String stockKey) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
                .append("select * from (select top 3 replace(t2.DateKey,'-','') as DateKey  from DimSecurity t,FactDayMeasure_Strategy1 t2  where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc) t1 order by t1.DateKey");
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[]{stockKey});
    }

    /**
     * ��ѯ���еĹ�Ʊ����
     */
    public List findStockTypeList() {
        String sql = "select * from T_STOCK_TYPE where state=1 order by id";
        return getJdbcTemplate(CMS_DB).query(sql);
    }

    /**
     * ��ѯ�����б�
     */
    public List findStrategyList() {
        String sql = "select * from T_STRATEGY t where t.state = '1' order by t.abit desc";
        return getJdbcTemplate(CMS_DB).query(sql);
    }

    /**
     * ��ѯ��ͼ�б�
     */
    public List findStrategyAuxiliaryChartList() {
        String sql = "select * from T_STRATEGY_AUXILIARYCHART t where t.state ='1' order by t.dbit desc";
        return getJdbcTemplate(CMS_DB).query(sql);
    }

    /**
     * ��ѯ��ͼ��������
     */
    public int findStrategyLineCount() {
        String sql = " select count(*) from T_STRATEGY_LINE t where t.state = '1' and t.type ='1' and t.abit in (select abit from T_STRATEGY where state ='1') ";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }

    /**
     * ��ѯ��ͼ�����б�
     */
    public List findStrategyLineListById(int id) {
        String sql = " select * from T_STRATEGY_LINE t where t.state = '1' and t.type ='1' and t.abit = ? order by t.line_no desc";
        return getJdbcTemplate(CMS_DB).query(sql, new Object[]{id});
    }

    /**
     * ��ѯ��ͼ��������
     */
    public int findStrategyAuxiliaryChartLineCount() {
        String sql = " select count(*) from T_STRATEGY_LINE t where t.state = '1' and t.type ='2' and t.abit in (select dbit from T_STRATEGY_AUXILIARYCHART where state ='1')";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }

    /**
     * ��ѯ��ͼ�����б�
     */
    public List findStrategyAuxiliaryChartLineListById(int id) {
        String sql = " select * from T_STRATEGY_LINE t where t.state = '1' and t.type ='2' and t.abit = ? order by t.line_no desc";
        return getJdbcTemplate(CMS_DB).query(sql, new Object[]{id});
    }

    /**
     * ��ѯ�ź�����
     */
    public int findSignCount() {
        String sql = " select count(*) from T_STRATEGY_SIGN t where t.state = '1' and t.abit in (select abit from T_STRATEGY where state ='1') ";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }

    /**
     * ��ѯ�ź��б�
     */
    public List findSignListById(int id) {
        String sql = " select * from T_STRATEGY_SIGN t where t.state = '1' and t.abit = ? order by t.sign_no desc";
        return getJdbcTemplate(CMS_DB).query(sql, new Object[]{id});
    }

    /**
     * ��ȡ���Ա��
     */
    public int findStrategySign() {
        String sql = "select sum(t.abit) as total from T_STRATEGY t where t.state = '1' ";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }

    /**
     * ��ȡ��ͼ���
     */
    public int findStrategyAuxiliaryChartSign() {
        String sql = " select sum(t.dbit) as total from T_STRATEGY_AUXILIARYCHART t where t.state = '1'";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }

    /**
     * ��ȡ��Ʊ����
     */
    public List findStockList(String stockType, int count) {
        String sqlString = " select t.stocktype,t.stockkey from T_STOCK_CHOOSE t,T_STOCK_TYPE s where t.stocktype = s.id and history_state='1' and s.name = ? order by to_number(t.stockno)";
        return getJdbcTemplate(CMS_DB).query(sqlString, new Object[]{stockType}, count);
    }

    public String findInvestmentAdvice(int id) {
        String sqlString = "select t.investcontent_t from T_INVESTMENT_ADVICE t where t.content_id = ?";
        logger.info("findInvestmentAdvice : " + sqlString);
        return getJdbcTemplate(CMS_DB).queryString(sqlString, new Object[]{id});
    }

    /**
     * @��������ѯ����˵��
     * @���ߣ���֪֮
     * @ʱ�䣺2013-6-14 ����6:36:34
     */
    public String findIntroduce(int id) {
        String sqlString = "select t.CONTENT from T_WORD_EXPOUND t where t.INTORDUCE_ID = ?";
        logger.info("findInvestmentAdvice : " + sqlString);
        return getJdbcTemplate(CMS_DB).queryString(sqlString, new Object[]{id});
    }

    public List findStrategyAbitList() {
        String sql = "select abit,sum(abit) over (partition by state) as total from T_STRATEGY where state = '1' order by abit desc";
        return getJdbcTemplate(CMS_DB).query(sql);
    }

    /**
     * @��������ȡ�汾��Ϣ
     * @���ߣ���֪֮
     * @ʱ�䣺2013-6-28 ����6:47:38
     */
    public DataRow findVersionInfo() {
        String sql = "select * from T_ELH_UPDATE order by id desc";
        return getJdbcTemplate(CMS_DB).queryMap(sql);
    }

    /**
     * @param count ��������
     * @��������ѯʵʱ�ź�����
     * @���ߣ���֪֮
     * @ʱ�䣺2013-3-30 ����8:33:49
     */
    public List findFactRTSignalList(String type, int count) {
        String sql = " SELECT   t.*,t2.SecurityCode FROM  FactRTSignal t,DimSecurity t2 where t.SecurityKey=t2.SecurityKey and t.Signal>0  and t.StrategyName=? order by t.TrigeredTime desc";
        List args = new ArrayList();
        args.add(type);
        return getJdbcTemplate(DB_NAME).query(sql, args.toArray(), count);
    }

    public List findFactRTSignalList(String type, Timestamp trigeredTime) {
        String sql = " SELECT   t.*,t2.SecurityCode FROM  FactRTSignal t,DimSecurity t2 where t.SecurityKey=t2.SecurityKey and t.Signal>0  and t.StrategyName=? and t.TrigeredTime>? order by t.TrigeredTime desc";
        List args = new ArrayList();
        args.add(type);
        args.add(trigeredTime);
        return getJdbcTemplate(DB_NAME).query(sql, args.toArray());
    }

    public List findStrategySignalByStockCode(int count, String stockCode) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select  ");

        stringBuffer
                .append(" replace(s.DateKey,'-','') as DateKey,s.Signal from DimSecurity t,FactDayMeasure_Strategy1 s where  t.SecurityCode = ? and t.SecurityKey = s.SecurityKey and s.Signal != 0 order by s.DateKey desc");
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[]{stockCode}, count);
    }

    public List findStrategy2SignalByStockCode(int count, String stockCode) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select top ");
        stringBuffer.append(count);
        stringBuffer
                .append(" replace(s2.DateKey,'-','') as DateKey,s2.Signal from DimSecurity t,FactDayMeasure_Strategy2 s2 where  t.SecurityCode = ? and t.SecurityKey = s2.SecurityKey and s2.Signal != 0 order by s2.DateKey desc");
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[]{stockCode});
    }

    public String findStockByStockKey(String stockType) {
        // TODO Auto-generated method stub
        return null;
    }

}
