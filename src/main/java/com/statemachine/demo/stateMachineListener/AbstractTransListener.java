package com.statemachine.demo.stateMachineListener;

import com.statemachine.demo.enums.OrderChangeEventEnum;
import com.statemachine.demo.enums.OrderStatusEnum;
import com.statemachine.demo.model.BaseResponse;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

public abstract class AbstractTransListener implements Action<OrderStatusEnum, OrderChangeEventEnum> {
    @Override
    public void execute(StateContext<OrderStatusEnum, OrderChangeEventEnum> stateContext) {

        BaseResponse response = onExecute(stateContext);

        setResponse(stateContext, response);
    }

    /**
     * 设置响应
     *
     * @param context
     * @param response
     */
    public void setResponse(StateContext<OrderStatusEnum, OrderChangeEventEnum> context, BaseResponse response) {
        context.getStateMachine().getExtendedState().getVariables().put(BaseResponse.class, response);
    }

    /**
     * 转换执行代码
     * @param context
     * @return
     */
    public abstract BaseResponse onExecute(StateContext<OrderStatusEnum, OrderChangeEventEnum> context);

}
