package com.statemachine.demo.stateMachineListener;

import com.statemachine.demo.enums.OrderChangeEventEnum;
import com.statemachine.demo.enums.OrderStatusEnum;
import com.statemachine.demo.model.BaseResponse;
import com.statemachine.demo.model.OrderInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PayActionListener extends AbstractTransListener {
    @Override
    public BaseResponse onExecute(StateContext<OrderStatusEnum, OrderChangeEventEnum> context) {
        Object orderinfo = context.getMessage().getHeaders().get("orderinfo");
        OrderInfo orderInfo = (OrderInfo) orderinfo;
        log.info("event------------pay"+orderInfo.toString());
        return new BaseResponse();
    }
}
