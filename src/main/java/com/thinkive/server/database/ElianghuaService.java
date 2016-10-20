package com.thinkive.server.database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.util.DateHelper;

public class ElianghuaService extends ProjectBaseService
{
    
    private static Logger logger  = Logger.getLogger(ElianghuaService.class);
    
    private static String DB_NAME = "elianghua";
    
    private static String CMS_DB  = "cms";
    
    private int           type_1  = 32768;                                   // 1000 0000 0000 0000
                                                                              
    private int           type_2  = 16384;                                   // 0100 0000 0000 0000
                                                                              
    /**
     * E量化副图数据 7002
     * @param count
     * @param securityCode
     * @param type
     * @return
     */
    
    public List findAuxiliaryChartList(int count, String stockKey, int type)
    {
        logger.debug("findAuxiliaryChartList start.");
        StringBuffer stringBuffer = new StringBuffer();
        if ( type == type_1 ) //  策略1
        {
            stringBuffer.append("select * from (select ");
            if ( count == 0 )
            {
                count = 999999;
            }
            stringBuffer.append(" top " + count);
            stringBuffer
                    .append(" replace(t2.DateKey,'-','') as DateKey, t2.SBSWave,t2.SBSWaveColor,t2.SBSReady,t2.SBSAlarm,t2.SBSRetrate,t2.SBSFocus,t2.SBSStrength ");
            stringBuffer.append(" from DimSecurity t,FactDayMeasure_Strategy1 t2 ");
            stringBuffer
                    .append(" where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc ) t1 order by t1.DateKey");
            
        }
        else if ( type == type_2 ) //  策略2
        {
            logger.debug("findAuxiliaryChartList do.");
            stringBuffer.append("select * from (select ");
            if ( count == 0 )
            {
                count = 999999;
            }
            stringBuffer.append(" top " + count);
            stringBuffer.append(" replace(t2.DateKey,'-','') as DateKey,t2.SBS_K,t2.SBS_D,SBS_J ");
            stringBuffer.append(" from DimSecurity t,FactDayMeasure_Strategy2 t2 ");
            stringBuffer
                    .append(" where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc ) t1 order by t1.DateKey");
        }
        else
        {
            logger.debug("类型匹配错误！");
            return null;
        }
        logger.debug("findAuxiliaryChartList end." + stringBuffer.toString() + "," + stockKey);
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[] { stockKey });
        
    }
    
    //    public int findStockPool()
    //    {
    //        String sql = "select sum(power(2,stockpool-1)) as stockpool from (select distinct stockpool from T_STOCK_CHOOSE)";
    //        return getJdbcTemplate(CMS_DB).queryInt(sql);
    //    }
    /**
     * 从设置中获取需要显示的线的编号和对应字段
     * @param abit  策略编号
     * @param type  主图或副图  1：主图   2：副图
     * @return
     */
    public List findLineFieldNameByAbit(int abit, String type)
    {
        String sql = "select t.field_name,t.line_no from T_STRATEGY_LINE t where t.abit =? and t.state='1' and t.type = ? order by t.line_no desc";
        return getJdbcTemplate(CMS_DB).query(sql, new Object[] { abit, type });
    }
    
    /**
     * 查询信号的序号和
     * @param abit
     * @return
     */
    public int findSignCountByAbit(int abit)
    {
        String sql = "select SUM(t.sign_no) as sbit from T_STRATEGY_SIGN t where t.abit = ? and t.state = '1' ";
        return getJdbcTemplate(CMS_DB).queryInt(sql, new Object[] { abit });
    }
    
    /**
     * 	E量化策略日线数据  7001
     * @param count
     * @param securityCode
     * @param type
     * @return
     */
    public List findDateDataList(int count, String stockKey, int type)
    {
        logger.debug("findDateDataList start.");
        StringBuffer stringBuffer = new StringBuffer();
        if ( type == type_1 ) //策略1
        {
            stringBuffer.append("select * from (select ");
            if ( count == 0 )
            {
                count = 999999;
            }
            stringBuffer.append(" top " + count);
            stringBuffer.append(" replace(t2.DateKey,'-','') as DateKey, t2.KColor,t2.AA,t2.BB,t2.BBColor,t2.Signal ");
            stringBuffer.append(" from DimSecurity t,FactDayMeasure_Strategy1 t2 ");
            stringBuffer
                    .append(" where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc ) t1 order by t1.DateKey");
        }
        else if ( type == type_2 ) //策略2
        {
            stringBuffer.append("select * from (select ");
            if ( count == 0 )
            {
                count = 999999;
            }
            stringBuffer.append(" top " + count);
            stringBuffer
                    .append(" replace(t2.DateKey,'-','') as DateKey,t2.MA,t2.MAColor,t2.Stress,t2.Surpport,t2.Signal ");
            stringBuffer.append(" from DimSecurity t,FactDayMeasure_Strategy2 t2 ");
            stringBuffer
                    .append(" where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc ) t1 order by t1.DateKey");
        }
        else
        {
            logger.debug("类型匹配错误！");
            return null;
        }
        
        logger.debug("findDateDataList end." + stringBuffer.toString() + "," + stockKey);
        
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[] { stockKey });
    }
    
    /**
     * E量化信号搜股
     * @param type
     * @param dateStr
     * @return
     */
    public List findDataBySignal(int type, int infoType, String dateStr)
    {
        StringBuffer stringBuffer = new StringBuffer();
        if ( type == type_1 ) //策略1
        {
            stringBuffer
                    .append("select t.SecurityCode from DimSecurity t,FactDayMeasure_Strategy1 t2 where t2.Signal = ? and t2.DateKey=? and t2.SecurityKey = t.SecurityKey ");
        }
        else if ( type == type_2 ) //策略2
        {
            stringBuffer
                    .append("select t.SecurityCode from DimSecurity t,FactDayMeasure_Strategy2 t2 where t2.Signal = ? and  t2.DateKey=? and t2.SecurityKey = t.SecurityKey ");
        }
        else
        {
            logger.debug("类型匹配错误！");
            return null;
        }
        logger.debug("7003 findDataBySignal end." + stringBuffer.toString() + "," + type + "," + infoType + ","
                + dateStr);
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[] { infoType, dateStr });
    }
    
    /**
     * 取三个交易日
     * @param args
     */
    
    public List findDateList(String stockKey)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer
                .append("select * from (select top 3 replace(t2.DateKey,'-','') as DateKey  from DimSecurity t,FactDayMeasure_Strategy1 t2  where t.SecurityCode = ? and t.SecurityKey = t2.SecurityKey order by t2.DateKey desc) t1 order by t1.DateKey");
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[] { stockKey });
    }
    
    /**
     * 查询所有的股票类型
     * @return
     */
    public List findStockTypeList()
    {
        String sql = "select * from T_STOCK_TYPE where state=1 order by id";
        return getJdbcTemplate(CMS_DB).query(sql);
    }
    
    /**
     * 查询策略列表
     * @return
     */
    public List findStrategyList()
    {
        String sql = "select * from T_STRATEGY t where t.state = '1' order by t.abit desc";
        return getJdbcTemplate(CMS_DB).query(sql);
    }
    
    /**
     * 查询副图列表
     * @return
     */
    public List findStrategyAuxiliaryChartList()
    {
        String sql = "select * from T_STRATEGY_AUXILIARYCHART t where t.state ='1' order by t.dbit desc";
        return getJdbcTemplate(CMS_DB).query(sql);
    }
    
    /**
     * 查询主图线条总数
     * @return
     */
    public int findStrategyLineCount()
    {
        String sql = " select count(*) from T_STRATEGY_LINE t where t.state = '1' and t.type ='1' and t.abit in (select abit from T_STRATEGY where state ='1') ";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }
    
    /**
     * 查询主图线条列表
     * @return
     */
    public List findStrategyLineListById(int id)
    {
        String sql = " select * from T_STRATEGY_LINE t where t.state = '1' and t.type ='1' and t.abit = ? order by t.line_no desc";
        return getJdbcTemplate(CMS_DB).query(sql, new Object[] { id });
    }
    
    /**
     * 查询副图线条总数
     * @return
     */
    public int findStrategyAuxiliaryChartLineCount()
    {
        String sql = " select count(*) from T_STRATEGY_LINE t where t.state = '1' and t.type ='2' and t.abit in (select dbit from T_STRATEGY_AUXILIARYCHART where state ='1')";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }
    
    /**
     * 查询副图线条列表
     * @return
     */
    public List findStrategyAuxiliaryChartLineListById(int id)
    {
        String sql = " select * from T_STRATEGY_LINE t where t.state = '1' and t.type ='2' and t.abit = ? order by t.line_no desc";
        return getJdbcTemplate(CMS_DB).query(sql, new Object[] { id });
    }
    
    /**
     * 查询信号总数
     * @param id
     * @return
     */
    public int findSignCount()
    {
        String sql = " select count(*) from T_STRATEGY_SIGN t where t.state = '1' and t.abit in (select abit from T_STRATEGY where state ='1') ";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }
    
    /**
     * 查询信号列表
     * @param id
     * @return
     */
    public List findSignListById(int id)
    {
        String sql = " select * from T_STRATEGY_SIGN t where t.state = '1' and t.abit = ? order by t.sign_no desc";
        return getJdbcTemplate(CMS_DB).query(sql, new Object[] { id });
    }
    
    /**
     * 获取策略标记
     * @return
     */
    public int findStrategySign()
    {
        String sql = "select sum(t.abit) as total from T_STRATEGY t where t.state = '1' ";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }
    
    /**
     * 获取副图标记
     * @return
     */
    public int findStrategyAuxiliaryChartSign()
    {
        String sql = " select sum(t.dbit) as total from T_STRATEGY_AUXILIARYCHART t where t.state = '1'";
        return getJdbcTemplate(CMS_DB).queryInt(sql);
    }
    
    /**
     * 获取股票代码
     * @param stockType
     * @param count
     * @return
     */
    public List findStockList(String stockType, int count)
    {
        String sqlString = " select t.stocktype,t.stockkey from T_STOCK_CHOOSE t,T_STOCK_TYPE s where t.stocktype = s.id and history_state='1' and s.name = ? order by to_number(t.stockno)";
        return getJdbcTemplate(CMS_DB).query(sqlString, new Object[] { stockType }, count);
    }
    
    public String findInvestmentAdvice(int id)
    {
        String sqlString = "select t.investcontent_t from T_INVESTMENT_ADVICE t where t.content_id = ?";
        logger.info("findInvestmentAdvice : " + sqlString);
        return getJdbcTemplate(CMS_DB).queryString(sqlString, new Object[] { id });
    }
    
    /**
     * @描述：查询文字说明
     * @作者：岳知之
     * @时间：2013-6-14 下午6:36:34
     * @param id
     * @return
     */
    public String findIntroduce(int id)
    {
        String sqlString = "select t.CONTENT from T_WORD_EXPOUND t where t.INTORDUCE_ID = ?";
        logger.info("findInvestmentAdvice : " + sqlString);
        return getJdbcTemplate(CMS_DB).queryString(sqlString, new Object[] { id });
    }
    
    public List findStrategyAbitList()
    {
        String sql = "select abit,sum(abit) over (partition by state) as total from T_STRATEGY where state = '1' order by abit desc";
        return getJdbcTemplate(CMS_DB).query(sql);
    }
    
    /**
     * @描述：获取版本信息
     * @作者：岳知之
     * @时间：2013-6-28 下午6:47:38
     * @return
     */
    public DataRow findVersionInfo()
    {
        String sql = "select * from T_ELH_UPDATE order by id desc";
        return getJdbcTemplate(CMS_DB).queryMap(sql);
    }
    
    /**
     * @描述：查询实时信号数据
     * @作者：岳知之
     * @时间：2013-3-30 下午8:33:49
     * @param count 返回数量
     * @return
     */
    public List findFactRTSignalList(String type, int count)
    {
        String sql = " SELECT   t.*,t2.SecurityCode FROM  FactRTSignal t,DimSecurity t2 where t.SecurityKey=t2.SecurityKey and t.Signal>0  and t.StrategyName=? order by t.TrigeredTime desc";
        List args = new ArrayList();
        args.add(type);
        return getJdbcTemplate(DB_NAME).query(sql, args.toArray(), count);
    }
    
    public List findFactRTSignalList(String type, Timestamp trigeredTime)
    {
        String sql = " SELECT   t.*,t2.SecurityCode FROM  FactRTSignal t,DimSecurity t2 where t.SecurityKey=t2.SecurityKey and t.Signal>0  and t.StrategyName=? and t.TrigeredTime>? order by t.TrigeredTime desc";
        List args = new ArrayList();
        args.add(type);
        args.add(trigeredTime);
        return getJdbcTemplate(DB_NAME).query(sql, args.toArray());
    }
    
    public List findStrategySignalByStockCode(int count, String stockCode)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select  ");
        
        stringBuffer
                .append(" replace(s.DateKey,'-','') as DateKey,s.Signal from DimSecurity t,FactDayMeasure_Strategy1 s where  t.SecurityCode = ? and t.SecurityKey = s.SecurityKey and s.Signal != 0 order by s.DateKey desc");
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[] { stockCode }, count);
    }
    
    public List findStrategy2SignalByStockCode(int count, String stockCode)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select top ");
        stringBuffer.append(count);
        stringBuffer
                .append(" replace(s2.DateKey,'-','') as DateKey,s2.Signal from DimSecurity t,FactDayMeasure_Strategy2 s2 where  t.SecurityCode = ? and t.SecurityKey = s2.SecurityKey and s2.Signal != 0 order by s2.DateKey desc");
        return getJdbcTemplate(DB_NAME).query(stringBuffer.toString(), new Object[] { stockCode });
    }
    
    public static void main(String[] args)
    {
        ElianghuaService elianghuaService = new ElianghuaService();
        Timestamp timestamp = new Timestamp(DateHelper.parseString("2013-02-08 00:03:27").getTime());
        
        List list2 = elianghuaService.findStrategyAbitList();
        List list = elianghuaService.findFactRTSignalList("Strategy2", timestamp);
        
        System.out.println(list.size());
        
        System.out.println(elianghuaService.findDateList("SZ000001").size());
        
    }
    
    public String findStockByStockKey(String stockType)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
