package com.thinkive.market.bean;

public class StockOption {

    private String MDStreamID;                //������������                   [6]
    private String SecurityID;                //��Լ����                          [9]
    private long TotalLongPosition;        //��ǰ��Լδƽ����         �ֲ�   ��ֲ���
    private long TradeVolume;            //�ɽ�����

    private double TotalValueTraded;        //�ɽ���� ��
    private double PreSettlPrice;            //���ս����
    private double OpenPrice;                //���տ��̼�
    private double AuctionPrice;            //��̬�ο��۸�

    private long AuctionQty;                //����ƥ������
    private double HighPrice;                //��߼�
    private double LowPrice;                //��ͼ�
    private double TradePrice;                //���¼�

    private double BuyPrice1;                //�����һ
    private long BuyVolume1;                //������һ
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
    private double SettlPrice;                //���ս����
    /*���ֶ�Ϊ4λ�ַ���������ÿλ��ʾ�ض��ĺ��壬�޶�������ո�
    ��1λ����S����ʾ����������ǰ��ʱ�Σ���C����ʾ���Ͼ���ʱ�Σ���T����ʾ��������ʱ�Σ���B����ʾ����ʱ�Σ���E����ʾ����ʱ�Σ���V����ʾ�������жϣ���P����ʾ��ʱͣ�ơ�
    ��2λ����0����ʾδ����ͣ�ƣ���1����ʾ����ͣ��*/
    private String TradingPhaseCode;        //��Ʒʵʱ�׶μ���־                   [5]
    private String zhuangtai;
    private String Timestamp;                //ʱ���                                                          [13]
    private long thedeal;                //����
    private long inside;                    //������
    private long outside;                //������
    private int inoutflag;                // �����̱�־
    private float thechange;                //������
    private int dealno;                    //�ɽ���ϸ����
    private int serno;                    //���
    private long iRoundLot;                //ÿ�ź�Լ�Ĺ���
    //�������ݵ��ֶ�
    private String UnderlyingSecurityID;    //���֤ȯ����             [7]
    private String UnderlyingSymbol;        //���֤ȯ����               [9]
    private String market;
    private double ExercisePrice;            //��Ȩ��Ȩ��,������Ȩ��Ϣ���������Ȩ��Ȩ�ۣ���ȷ��0.1��
    private double up;                        //�ǵ�
    private double uppercent;                //�ǵ���
    private String ListID;                    //[5]1501
    private int LeaveDay;                //���뵽����ʣ������
    private int iPC;                    //1�Ϲ�                                 0�Ϲ�
    private int stockOptionType;
    private String ExpireDate;                //��Ȩ������               [9]   ---20150128
    private double average;                //����
    private String ContractSymbol;            //��Ȩ��Լ���
    private long cangcha;                // �ֲ�
    private long YesTotalLongPosition;    //��ǰ��Լδƽ���� ��λ�� ����)       ��ֲ���
    private long ContractMultiplierUnit;    //��Լ��λ  ��һ����Ȩ��Լ�������Ĺ�����
    /* ��4λ����0����ʾ����δ����������1����ʾ���10���������ں�Լ����������*/
    private String SecurityStatusFlag;        //��Ȩ��Լ״̬��Ϣ��ǩ      9
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
     * @���ߣ���֪֮
     * @ʱ�䣺2013-1-21 ����10:50:01
     */
    public Comparable getDataBySort(int sort) {
        Comparable data = null;
        switch (sort) {
            case 35:
                data = ExercisePrice;//��Ȩ��Ȩ��
                break;
            case 34:
                data = SecurityID;//��Լ����
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
                data = uppercent;//�Ƿ�
                break;
            case 2:
                data = TradePrice;//���¼�
                break;
            case 3:
                data = up;//�ǵ�
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
                data = TradeVolume;//�ɽ���
                break;

            case 9:
                data = OpenPrice;//��
                break;
            case 10:
                data = HighPrice;//���
                break;
            case 11:
                data = LowPrice;//���
                break;

            case 12:
                data = PreSettlPrice;//��������  ���
                break;
            case 14:
                data = TotalValueTraded;//�ɽ����
                break;

            case 17:
                data = average;//����
                break;

            case 18:
                data = inside;//����
                break;
            case 19:
                data = outside;//����
                break;
            case 25:
                data = thedeal;//����
                break;

            case 36:
                data = SettlPrice;//���ս����  ����
                break;
            case 37:
                data = ExpireDate;//����
                break;
            case 38:
                data = iPC;//����  �Ϲ� ���� �Ϲ�
                break;
            case 39:
                data = ContractSymbol;//��Ȩ��Լ����
                break;
            case 41:
                data = leixing;//����
                break;

            case 42:
                data = ContractMultiplierUnit;//��Լ��λ
                break;
            case 43:
                data = zhuangtai;//״̬
                break;

            case 44:
                data = TotalLongPosition;//�ֲ�
                break;
            case 45:
                data = cangcha;//�ֲ�
                break;
            case 46:
                data = LeaveDay;//ʣ��
                break;

            case 51:
                data = BuyPrice1;//��һ��
                break;
            case 52:
                data = BuyPrice2;//�����
                break;
            case 53:
                data = BuyPrice3;//������
                break;
            case 54:
                data = BuyPrice4;//���ļ�
                break;
            case 55:
                data = BuyPrice5;//�����
                break;

            case 56:
                data = SellPrice1;//��һ��
                break;
            case 57:
                data = SellPrice2;//������
                break;
            case 58:
                data = SellPrice3;//������
                break;
            case 59:
                data = SellPrice4;//���ļ�
                break;
            case 60:
                data = SellPrice5;//�����
                break;

            case 61:
                data = BuyVolume1;//��һ��
                break;
            case 62:
                data = BuyVolume2;//�����
                break;
            case 63:
                data = BuyVolume3;//������
                break;
            case 64:
                data = BuyVolume4;//������
                break;
            case 65:
                data = BuyVolume5;//������
                break;

            case 66:
                data = SellVolume1;//��һ��
                break;
            case 67:
                data = SellVolume2;//������
                break;
            case 68:
                data = SellVolume3;//������
                break;
            case 69:
                data = SellVolume4;//������
                break;
            case 70:
                data = SellVolume5;//������
                break;
        }

        if (data == null) {
            data = "";
        }

        return data;
    }

}
