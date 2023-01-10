package com.statemachine.demo;

import com.statemachine.demo.enums.OrderChangeEventEnum;
import com.statemachine.demo.model.OrderInfo;
import com.statemachine.demo.service.StateMachineService;
import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoTestOne {
    @Autowired
    StateMachineService stateMachineService;

    @Test
    public void getChangeStatus() {
        OrderInfo orderInfo = new OrderInfo("111", 0);
        OrderInfo orderInfo2 = new OrderInfo("222", 0);
        stateMachineService.doTransition(OrderChangeEventEnum.EVENT_PAID, orderInfo);
        new Thread(()->{
            orderInfo.setStatus(1);
            stateMachineService.doTransition(OrderChangeEventEnum.EVENT_DELIVER, orderInfo);
            orderInfo.setStatus(2);
            stateMachineService.doTransition(OrderChangeEventEnum.EVENT_RECEIVE, orderInfo);
        }).start();
        stateMachineService.doTransition(OrderChangeEventEnum.EVENT_PAID, orderInfo2);
        orderInfo2.setStatus(1);
        stateMachineService.doTransition(OrderChangeEventEnum.EVENT_DELIVER, orderInfo2);
        orderInfo2.setStatus(2);
        stateMachineService.doTransition(OrderChangeEventEnum.EVENT_RECEIVE, orderInfo2);
    }
}
