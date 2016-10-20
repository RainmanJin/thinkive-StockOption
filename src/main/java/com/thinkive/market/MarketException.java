package com.thinkive.market;

public class MarketException extends RuntimeException
{
    public MarketException()
    {
        super();
    }
    
    public MarketException(String message)
    {
        super(message);
    }
    
    public MarketException(String message, Throwable cause)
    {
        super(message, cause);
    }
    
    public MarketException(Throwable cause)
    {
        super(cause);
    }
}
