package com.cy.task;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Future;

/**
 * 异步执行测试学习
 *
 * Created by cy
 * 2017/12/7 10:53
 */
@Component
@Log4j2
public class AsyncTask {

    @Async
    public Future<String> taskOne() throws Exception {
        log.info("taskOne开始睡觉");
        Thread.sleep(5000);
        log.info("taskOne睡完觉");
        return new AsyncResult<>("完成");
    }
    @Async
    public Future<String> taskTwo() throws Exception {
        log.info("taskTwo开始睡觉");
        Thread.sleep(5000);
        log.info("taskTwo睡完觉");
        return new AsyncResult<>("完成");
    }

}
