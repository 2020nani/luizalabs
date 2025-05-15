package com.luizalabs_desafio;
import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;
import com.luizalabs_desafio.core.customer.CustomerOrderData;
import com.luizalabs_desafio.application.mapper.output.CustomerOrderResponse;
import com.luizalabs_desafio.core.customer.entity.CustomerOrderDataJpa;
import com.luizalabs_desafio.core.customer.Order;
import com.luizalabs_desafio.core.customer.Product;
import com.luizalabs_desafio.core.customer.entity.OrderJpa;
import com.luizalabs_desafio.core.customer.entity.ProductJpa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestMockFactory {
    public static String USER_NAME = "Joao";
    public static Integer USER_ID = 1;
    public static CustomerOrderData mockCustomerOrderData() {
        List<Order> orders = new ArrayList<>();
        orders.add(mockOrder());
        return CustomerOrderData.builder()
                .userId(USER_ID)
                .name(USER_NAME)
                .orders(orders)
                .build();
    }

    public static CustomerOrderDataJpa mockCustomerOrderDataTest() {
        List<OrderJpa> orders = new ArrayList<>();
        orders.add(mockOrderJpa());
        return CustomerOrderDataJpa.builder()
                .userId(USER_ID)
                .name(USER_NAME)
                .orders(orders)
                .build();
    }

    public static Order mockOrder() {
        return new Order(12345, new BigDecimal("512.48"), LocalDate.of(2020, 12, 1), mockProducts());
    }

    public static OrderJpa mockOrderJpa() {
        return OrderJpa.builder()
                .orderId(12345)
                .total(new BigDecimal("512.48"))
                .date(LocalDate.of(2020, 12, 1))
                .products(mockProductsJpa())
                .build();
    }

    public static List<Product> mockProducts() {
        return List.of(
                new Product(111, new BigDecimal("256.24")),
                new Product(122, new BigDecimal("256.24"))
        );
    }

    public static List<ProductJpa> mockProductsJpa() {
        return List.of(
                ProductJpa.builder()
                        .productId(111)
                        .value(new BigDecimal("256.24"))
                        .build(),
                ProductJpa.builder()
                        .productId(112)
                        .value(new BigDecimal("256.24"))
                        .build()
        );
    }

    public static CustomerOrderResponse mockCustomerOrderResponse() {
        return new CustomerOrderResponse(USER_ID, USER_NAME, List.of(mockOrder().convert()));
    }

    public static CustomerOrderRequest mockCustomerOrderRequest() {
        return new CustomerOrderRequest(USER_ID,USER_NAME,12,13,BigDecimal.ZERO,LocalDate.now());
    }
}
