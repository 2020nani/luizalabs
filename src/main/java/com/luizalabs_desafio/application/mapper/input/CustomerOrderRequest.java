package com.luizalabs_desafio.application.mapper.input;

import com.luizalabs_desafio.core.customer.CustomerOrderData;
import com.luizalabs_desafio.core.customer.Order;
import com.luizalabs_desafio.core.customer.Product;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CustomerOrderRequest(
        @NotNull
        @Max(9999999999L)
        Integer userId,
        @NotBlank
        @Size(max = 45)
        String userName,
        @NotNull
        @Max(9999999999L)
        Integer orderId,
        @NotNull
        @Max(9999999999L)
        Integer productId,
        @NotNull
        @Max(999999999999L)
        BigDecimal value,
        @NotNull
        LocalDate orderDate
) {
        public CustomerOrderData convert() {
                Product product = new Product(productId, value );
                return CustomerOrderData.builder()
                        .userId(userId)
                        .name(userName)
                        .orders(List.of(Order.builder()
                                        .orderId(orderId)
                                        .total(value)
                                        .date(orderDate)
                                        .products(List.of(product))
                                .build()))
                        .build();
        }
}
