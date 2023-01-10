package com.statemachine.demo.config;

import com.statemachine.demo.enums.OrderChangeEventEnum;
import com.statemachine.demo.enums.OrderStatusEnum;
import com.statemachine.demo.enums.StateMachineConst;
import com.statemachine.demo.model.OrderInfo;
import com.statemachine.demo.stateMachineListener.DeliverActionListener;
import com.statemachine.demo.stateMachineListener.PayActionListener;
import com.statemachine.demo.stateMachineListener.ReceiveActionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.function.Predicate;

@Configuration
@Component
//@EnableStateMachine(name = "orderStateMachine")
@EnableStateMachineFactory(name = "orderStateMachine")

public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStatusEnum, OrderChangeEventEnum> {

    @Autowired
    PayActionListener payActionListener;
    @Autowired
    DeliverActionListener deliverActionListener;
    @Autowired
    ReceiveActionListener receiveActionListener;

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatusEnum, OrderChangeEventEnum> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatusEnum.UNPAY)
                .states(EnumSet.allOf(OrderStatusEnum.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatusEnum, OrderChangeEventEnum> transitions) throws Exception {
        transitions.withExternal().source(OrderStatusEnum.UNPAY).target(OrderStatusEnum.DELIVER)
                .event(OrderChangeEventEnum.EVENT_PAID).action(payActionListener).and()
                .withExternal().source(OrderStatusEnum.DELIVER).target(OrderStatusEnum.RECEIVE)
                .event(OrderChangeEventEnum.EVENT_DELIVER).action(deliverActionListener).and()
                .withExternal().source(OrderStatusEnum.RECEIVE).target(OrderStatusEnum.FINISH)
                .event(OrderChangeEventEnum.EVENT_RECEIVE).action(receiveActionListener);
    }

    @Bean
    public StateMachinePersister<OrderStatusEnum, OrderChangeEventEnum, OrderInfo> persister(){
        return new DefaultStateMachinePersister<>(new StateMachinePersist<OrderStatusEnum, OrderChangeEventEnum,
                OrderInfo>(){
            @Override
            public void write(StateMachineContext<OrderStatusEnum, OrderChangeEventEnum> stateMachineContext, OrderInfo orderInfo) throws Exception {

            }

            @Override
            public StateMachineContext<OrderStatusEnum, OrderChangeEventEnum> read(OrderInfo orderInfo) throws Exception {
                StateMachineContext<OrderStatusEnum, OrderChangeEventEnum> result =
                        new DefaultStateMachineContext<>(OrderStatusEnum.getByValue(Byte.valueOf(orderInfo.getStatus()+"")), null,
                                null, null, null, StateMachineConst.orderStateMachineId);
                return result;

            }
        });
    }
}
