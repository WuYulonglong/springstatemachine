package com.statemachine.demo.enums;

public enum OrderStatusEnum {
    UNPAY(0),
    DELIVER(1),
    RECEIVE(2),
    FINISH(3);

    int i;
    OrderStatusEnum(Integer i){
        this.i=i;
    }

    public static OrderStatusEnum getByValue(byte value) {
        for (OrderStatusEnum statusEnum : values()) {
            if (statusEnum.i == value) {
                return statusEnum;
            }
        }
        return null;
    }
}
