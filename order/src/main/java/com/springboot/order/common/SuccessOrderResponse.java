package com.springboot.order.common;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.springboot.order.dto.OrderDTO;
import lombok.Getter;

@Getter
public class SuccessOrderResponse implements OrderResponse {
    @JsonUnwrapped
    private final OrderDTO order;
    public SuccessOrderResponse(OrderDTO order) {
        this.order = order;
    }

    public OrderDTO getOrder() {
        return order;
    }
}
