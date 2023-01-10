package com.statemachine.demo.service;

import com.statemachine.demo.config.StateMachineConfig;
import com.statemachine.demo.enums.OrderChangeEventEnum;
import com.statemachine.demo.enums.OrderStatusEnum;
import com.statemachine.demo.enums.StateMachineConst;
import com.statemachine.demo.model.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StateMachineService {
    @Autowired
    /*@Qualifier(value = "orderStateMachine")
    private StateMachine<OrderStatusEnum, OrderChangeEventEnum> orderStateMachine;*/

    @Qualifier(value = "orderStateMachine")
    private StateMachineFactory<OrderStatusEnum, OrderChangeEventEnum> stateMachineFactory;

    @Autowired
    private StateMachinePersister persister;

    public void doTransition(OrderChangeEventEnum event, OrderInfo orderInfo) {
        StateMachine<OrderStatusEnum, OrderChangeEventEnum> machine;
        machine = stateMachineFactory.getStateMachine(StateMachineConst.orderStateMachineId);
        try {
            machine.start();
            persister.restore(machine, orderInfo);
            machine.sendEvent(MessageBuilder.withPayload(event)
                    .setHeader("orderinfo", orderInfo).build());
            persister.persist(machine, orderInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            machine.stop();
        }

    }
}
