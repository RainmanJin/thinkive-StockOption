package com.thinkive.market.service.works;

public interface ITask {
    void init(String param) throws Exception;

    void update() throws Exception;

    void clear() throws Exception;
}
