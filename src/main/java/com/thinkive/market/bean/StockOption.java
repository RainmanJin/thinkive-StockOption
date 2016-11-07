package com.thinkive.market.bean;

public class StockOption {

    private String MDStreamID;                //行情数据类型                   [6]
    private String SecurityID;                //合约编码                          [9]
    private long TotalLongPosition;        //当前合约未平仓数         持仓   今持仓量
    private long TradeVolume;            //成交数量

    private double TotalValueTraded;        //成交金额 分
    private double PreSettlPrice;            //昨日结算价
    private double OpenPrice;                //今日开盘价
    private double AuctionPrice;            //动态参考价格

    private long AuctionQty;                //虚拟匹配数量
    private double HighPrice;                //最高价
    private double LowPrice;                //最低价
    private double TradePrice;                //最新价

    private double BuyPrice1;                //申买价一
    private long BuyVolume1;                //申买量一
    private double SellPrice1;                //
    private long SellVolume1;

    private double BuyPrice2;
    private long BuyVolume2;
    private double SellPrice2;
    private long SellVolume2;

    private double BuyPrice3;
    private long BuyVolume3;
    private double SellPrice3;
    private long SellVolume3;

    private double BuyPrice4;
    private long BuyVolume4;
    private double SellPrice4;
    private long SellVolume4;

    private double BuyPrice5;
    private long BuyVolume5;
    private double SellPrice5;
    private long SellVolume5;
    private double SettlPrice;                //今日结算价
    /*该字段为4位字符串，左起每位表示特定的含义，无定义则填空格。
    第1位：‘S’表示启动（开市前）时段，‘C’表示集合竞价时段，‘T’表示连续交易时段，‘B’表示休市时段，‘E’表示闭市时段，‘V’表示波动性中断，‘P’表示临时停牌。
    第2位：‘0’表示未连续停牌，‘1’表示连续停牌*/
    private String TradingPhaseCode;        //产品实时阶段及标志                   [5]
    private String zhuangtai;
    private String Timestamp;                //时间戳                                                          [13]
    private long thedeal;                //现量
    private long inside;                    //总卖盘
    private long outside;                //总买盘
    private int inoutflag;                // 内外盘标志
    private float thechange;                //笔升跌
    private int dealno;                    //成交明细数量
    private int serno;                    //序号
    private long iRoundLot;                //每张合约的股数
    //基础数据的字段
    private String UnderlyingSecurityID;    //标的证券代码             [7]
    private String UnderlyingSymbol;        //标的证券名称               [9]
    private String market;
    private double ExercisePrice;            //期权行权价,经过除权除息调整后的期权行权价，精确到0.1厘
    private double up;                        //涨跌
    private double uppercent;                //涨跌幅
    private String ListID;                    //[5]1501
    private int LeaveDay;                //距离到期日剩余天数
    private int iPC;                    //1认购                                 0认沽
    private int stockOptionType;
    private String ExpireDate;                //期权到期日               [9]   ---20150128
    private double average;                //均价
    private String ContractSymbol;            //期权合约简称
    private long cangcha;                // 仓差
    private long YesTotalLongPosition;    //当前合约未平仓数 单位是 （张)       昨持仓量
    private long ContractMultiplierUnit;    //合约单位  （一张期权合约所包含的股数）
    /* 第4位：‘0’表示近期未做调整，‘1’表示最近10个交易日内合约发生过调整*/
    private String SecurityStatusFlag;        //期权合约状态信息标签      9
    private String leixing;
    private BaseInfo baseInfo;

    public BaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setBaseInfo(BaseInfo baseInfo) {
        this.baseInfo = baseInfo;
    }

    @Override
    public String toString() {
        return "StockOption{" +
                "MDStreamID='" + MDStreamID + '\'' +
                ", SecurityID='" + SecurityID + '\'' +
                ", TradePrice=" + TradePrice +
                ", BuyPrice1=" + BuyPrice1 +
                ", BuyVolume1=" + BuyVolume1 +
                ", SellPrice1=" + SellPrice1 +
                ", SellVolume1=" + SellVolume1 +
                ", BuyPrice2=" + BuyPrice2 +
                ", BuyVolume2=" + BuyVolume2 +
                ", SellPrice2=" + SellPrice2 +
                ", SellVolume2=" + SellVolume2 +
                ", BuyPrice3=" + BuyPrice3 +
                ", BuyVolume3=" + BuyVolume3 +
                ", SellPrice3=" + SellPrice3 +
                ", SellVolume3=" + SellVolume3 +
                ", BuyPrice4=" + BuyPrice4 +
                ", BuyVolume4=" + BuyVolume4 +
                ", SellPrice4=" + SellPrice4 +
                ", SellVolume4=" + SellVolume4 +
                ", BuyPrice5=" + BuyPrice5 +
                ", BuyVolume5=" + BuyVolume5 +
                ", SellPrice5=" + SellPrice5 +
                ", SellVolume5=" + SellVolume5 +
                '}';
    }

    public String getContractSymbol() {
        return ContractSymbol;
    }

    public void setContractSymbol(String contractSymbol) {
        ContractSymbol = contractSymbol;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getMDStreamID() {
        return MDStreamID;
    }

    public void setMDStreamID(String mDStreamID) {
        MDStreamID = mDStreamID;
    }

    public String getSecurityID() {
        return SecurityID;
    }

    public void setSecurityID(String securityID) {
        SecurityID = securityID;
    }

    public long getTotalLongPosition() {
        return TotalLongPosition;
    }

    public void setTotalLongPosition(long totalLongPosition) {
        TotalLongPosition = totalLongPosition;
    }

    public long getTradeVolume() {
        return TradeVolume;
    }

    public void setTradeVolume(long tradeVolume) {
        TradeVolume = tradeVolume;
    }

    public double getTotalValueTraded() {
        return TotalValueTraded;
    }

    public void setTotalValueTraded(double totalValueTraded) {
        TotalValueTraded = totalValueTraded;
    }

    public double getPreSettlPrice() {
        return PreSettlPrice;
    }

    public void setPreSettlPrice(double preSettlPrice) {
        PreSettlPrice = preSettlPrice;
    }

    public double getOpenPrice() {
        return OpenPrice;
    }

    public void setOpenPrice(double openPrice) {
        OpenPrice = openPrice;
    }

    public double getAuctionPrice() {
        return AuctionPrice;
    }

    public void setAuctionPrice(double auctionPrice) {
        AuctionPrice = auctionPrice;
    }

    public long getAuctionQty() {
        return AuctionQty;
    }

    public void setAuctionQty(long auctionQty) {
        AuctionQty = auctionQty;
    }

    public double getHighPrice() {
        return HighPrice;
    }

    public void setHighPrice(double highPrice) {
        HighPrice = highPrice;
    }

    public double getLowPrice() {
        return LowPrice;
    }

    public void setLowPrice(double lowPrice) {
        LowPrice = lowPrice;
    }

    public double getTradePrice() {
        return TradePrice;
    }

    public void setTradePrice(double tradePrice) {
        TradePrice = tradePrice;
    }

    public double getBuyPrice1() {
        return BuyPrice1;
    }

    public void setBuyPrice1(double buyPrice1) {
        BuyPrice1 = buyPrice1;
    }

    public long getBuyVolume1() {
        return BuyVolume1;
    }

    public void setBuyVolume1(long buyVolume1) {
        BuyVolume1 = buyVolume1;
    }

    public double getSellPrice1() {
        return SellPrice1;
    }

    public void setSellPrice1(double sellPrice1) {
        SellPrice1 = sellPrice1;
    }

    public long getSellVolume1() {
        return SellVolume1;
    }

    public void setSellVolume1(long sellVolume1) {
        SellVolume1 = sellVolume1;
    }

    public double getBuyPrice2() {
        return BuyPrice2;
    }

    public void setBuyPrice2(double buyPrice2) {
        BuyPrice2 = buyPrice2;
    }

    public long getBuyVolume2() {
        return BuyVolume2;
    }

    public void setBuyVolume2(long buyVolume2) {
        BuyVolume2 = buyVolume2;
    }

    public double getSellPrice2() {
        return SellPrice2;
    }

    public void setSellPrice2(double sellPrice2) {
        SellPrice2 = sellPrice2;
    }

    public long getSellVolume2() {
        return SellVolume2;
    }

    public void setSellVolume2(long sellVolume2) {
        SellVolume2 = sellVolume2;
    }

    public double getBuyPrice3() {
        return BuyPrice3;
    }

    public void setBuyPrice3(double buyPrice3) {
        BuyPrice3 = buyPrice3;
    }

    public long getBuyVolume3() {
        return BuyVolume3;
    }

    public void setBuyVolume3(long buyVolume3) {
        BuyVolume3 = buyVolume3;
    }

    public double getSellPrice3() {
        return SellPrice3;
    }

    public void setSellPrice3(double sellPrice3) {
        SellPrice3 = sellPrice3;
    }

    public long getSellVolume3() {
        return SellVolume3;
    }

    public void setSellVolume3(long sellVolume3) {
        SellVolume3 = sellVolume3;
    }

    public double getBuyPrice4() {
        return BuyPrice4;
    }

    public void setBuyPrice4(double buyPrice4) {
        BuyPrice4 = buyPrice4;
    }

    public long getBuyVolume4() {
        return BuyVolume4;
    }

    public void setBuyVolume4(long buyVolume4) {
        BuyVolume4 = buyVolume4;
    }

    public double getSellPrice4() {
        return SellPrice4;
    }

    public void setSellPrice4(double sellPrice4) {
        SellPrice4 = sellPrice4;
    }

    public long getSellVolume4() {
        return SellVolume4;
    }

    public void setSellVolume4(long sellVolume4) {
        SellVolume4 = sellVolume4;
    }

    public double getBuyPrice5() {
        return BuyPrice5;
    }

    public void setBuyPrice5(double buyPrice5) {
        BuyPrice5 = buyPrice5;
    }

    public long getBuyVolume5() {
        return BuyVolume5;
    }

    public void setBuyVolume5(long buyVolume5) {
        BuyVolume5 = buyVolume5;
    }

    public double getSellPrice5() {
        return SellPrice5;
    }

    public void setSellPrice5(double sellPrice5) {
        SellPrice5 = sellPrice5;
    }

    public long getSellVolume5() {
        return SellVolume5;
    }

    public void setSellVolume5(long sellVolume5) {
        SellVolume5 = sellVolume5;
    }

    public double getSettlPrice() {
        return SettlPrice;
    }

    public void setSettlPrice(double settlPrice) {
        SettlPrice = settlPrice;
    }

    public String getTradingPhaseCode() {
        return TradingPhaseCode;
    }

    public void setTradingPhaseCode(String tradingPhaseCode) {
        TradingPhaseCode = tradingPhaseCode;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public long getThedeal() {
        return thedeal;
    }

    public void setThedeal(long thedeal) {
        this.thedeal = thedeal;
    }

    public long getInside() {
        return inside;
    }

    public void setInside(long inside) {
        this.inside = inside;
    }

    public long getOutside() {
        return outside;
    }

    public void setOutside(long outside) {
        this.outside = outside;
    }

    public int getInoutflag() {
        return inoutflag;
    }

    public void setInoutflag(int inoutflag) {
        this.inoutflag = inoutflag;
    }

    public float getThechange() {
        return thechange;
    }

    public void setThechange(float thechange) {
        this.thechange = thechange;
    }

    public int getDealno() {
        return dealno;
    }

    public void setDealno(int dealno) {
        this.dealno = dealno;
    }

    public int getSerno() {
        return serno;
    }

    public void setSerno(int serno) {
        this.serno = serno;
    }

    public String getUnderlyingSecurityID() {
        return UnderlyingSecurityID;
    }

    public void setUnderlyingSecurityID(String underlyingSecurityID) {
        UnderlyingSecurityID = underlyingSecurityID;
    }

    public String getUnderlyingSymbol() {
        return UnderlyingSymbol;
    }

    public void setUnderlyingSymbol(String underlyingSymbol) {
        UnderlyingSymbol = underlyingSymbol;
    }

    public int getStockOptionType() {
        return stockOptionType;
    }

    public void setStockOptionType(int stockOptionType) {
        this.stockOptionType = stockOptionType;
    }

    public String getListID() {
        return ListID;
    }

    public void setListID(String listID) {
        ListID = listID;
    }

    public int getLeaveDay() {
        return LeaveDay;
    }

    public void setLeaveDay(int leaveDay) {
        LeaveDay = leaveDay;
    }

    public int getiPC() {
        return iPC;
    }

    public void setiPC(int iPC) {
        this.iPC = iPC;
    }

    public double getExercisePrice() {
        return ExercisePrice;
    }

    public void setExercisePrice(double exercisePrice) {
        ExercisePrice = exercisePrice;
    }

    public double getUp() {
        return up;
    }

    public void setUp(double up) {
        this.up = up;
    }

    public double getUppercent() {
        return uppercent;
    }

    public void setUppercent(double uppercent) {
        this.uppercent = uppercent;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    public long getCangcha() {
        return cangcha;
    }

    public void setCangcha(long cangcha) {
        this.cangcha = cangcha;
    }

    public long getYesTotalLongPosition() {
        return YesTotalLongPosition;
    }

    public void setYesTotalLongPosition(long yesTotalLongPosition) {
        YesTotalLongPosition = yesTotalLongPosition;
    }

    public long getContractMultiplierUnit() {
        return ContractMultiplierUnit;
    }

    public void setContractMultiplierUnit(long contractMultiplierUnit) {
        ContractMultiplierUnit = contractMultiplierUnit;
    }

    public String getZhuangtai() {
        return zhuangtai;
    }

    public void setZhuangtai(String zhuangtai) {
        this.zhuangtai = zhuangtai;
    }

    public String getSecurityStatusFlag() {
        return SecurityStatusFlag;
    }

    public void setSecurityStatusFlag(String securityStatusFlag) {
        SecurityStatusFlag = securityStatusFlag;
    }

    public String getLeixing() {
        return leixing;
    }

    public void setLeixing(String leixing) {
        this.leixing = leixing;
    }

    public long getiRoundLot() {
        return iRoundLot;
    }

    public void setiRoundLot(long iRoundLot) {
        this.iRoundLot = iRoundLot;
    }

    /**
     * @作者：岳知之
     * @时间：2013-1-21 下午10:50:01
     */
    public Comparable getDataBySort(int sort) {
        Comparable data = null;
        switch (sort) {
            case 35:
                data = ExercisePrice;//期权行权价
                break;
            case 34:
                data = SecurityID;//合约编码
                break;
            case 24:
                data = UnderlyingSecurityID;
                break;
            case 23:
                data = market;
                break;
            case 22:
                data = UnderlyingSymbol;
                break;

            case 1:
                data = uppercent;//涨幅
                break;
            case 2:
                data = TradePrice;//最新价
                break;
            case 3:
                data = up;//涨跌
                break;
            case 4:
                if (baseInfo != null) {
                    data = baseInfo.getDailyPriceUpLimit();
                }
                break;
            case 5:
                if (baseInfo != null) {
                    data = baseInfo.getDailyPriceDownLimit();
                }
                break;

            case 6:
                data = TradeVolume;//成交量
                break;

            case 9:
                data = OpenPrice;//今开
                break;
            case 10:
                data = HighPrice;//最高
                break;
            case 11:
                data = LowPrice;//最低
                break;

            case 12:
                data = PreSettlPrice;//昨天结算价  昨结
                break;
            case 14:
                data = TotalValueTraded;//成交金额
                break;

            case 17:
                data = average;//均价
                break;

            case 18:
                data = inside;//内盘
                break;
            case 19:
                data = outside;//外盘
                break;
            case 25:
                data = thedeal;//现量
                break;

            case 36:
                data = SettlPrice;//今日结算价  结算
                break;
            case 37:
                data = ExpireDate;//到期
                break;
            case 38:
                data = iPC;//类型  认购 或者 认沽
                break;
            case 39:
                data = ContractSymbol;//期权合约名称
                break;
            case 41:
                data = leixing;//类型
                break;

            case 42:
                data = ContractMultiplierUnit;//合约单位
                break;
            case 43:
                data = zhuangtai;//状态
                break;

            case 44:
                data = TotalLongPosition;//持仓
                break;
            case 45:
                data = cangcha;//仓差
                break;
            case 46:
                data = LeaveDay;//剩余
                break;

            case 51:
                data = BuyPrice1;//买一价
                break;
            case 52:
                data = BuyPrice2;//买二价
                break;
            case 53:
                data = BuyPrice3;//买三价
                break;
            case 54:
                data = BuyPrice4;//买四价
                break;
            case 55:
                data = BuyPrice5;//买五价
                break;

            case 56:
                data = SellPrice1;//卖一价
                break;
            case 57:
                data = SellPrice2;//卖二价
                break;
            case 58:
                data = SellPrice3;//卖三价
                break;
            case 59:
                data = SellPrice4;//卖四价
                break;
            case 60:
                data = SellPrice5;//卖五价
                break;

            case 61:
                data = BuyVolume1;//买一量
                break;
            case 62:
                data = BuyVolume2;//买二量
                break;
            case 63:
                data = BuyVolume3;//买三量
                break;
            case 64:
                data = BuyVolume4;//买四量
                break;
            case 65:
                data = BuyVolume5;//买五量
                break;

            case 66:
                data = SellVolume1;//卖一量
                break;
            case 67:
                data = SellVolume2;//卖二量
                break;
            case 68:
                data = SellVolume3;//卖三量
                break;
            case 69:
                data = SellVolume4;//卖四量
                break;
            case 70:
                data = SellVolume5;//卖五量
                break;
        }

        if (data == null) {
            data = "";
        }

        return data;
    }

}
