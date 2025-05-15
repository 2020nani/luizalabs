package com.luizalabs_desafio.application.mapper.output;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetailsResponse (
        Integer orderId,
        BigDecimal total,
        String date,
        List<ProductResponse> products
) {
}
