package com.luizalabs_desafio.application.mapper.output;

import java.util.List;

public record CustomerOrderResponse(
        Integer userId,
        String name,
        List<OrderDetailsResponse>orders
){
}
