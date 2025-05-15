package com.luizalabs_desafio.core.customer;

import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;
import com.luizalabs_desafio.application.mapper.output.OrderDetailsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.luizalabs_desafio.TestMockFactory.mockCustomerOrderRequest;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order(
                12345,
                new BigDecimal("100.00"),
                LocalDate.of(2025, 5, 11),
                new ArrayList<>(List.of(new Product(1, new BigDecimal("50.00"))))
        );
    }

    @Test
    void testConvertOrderToOrderDetailsResponse() {
        OrderDetailsResponse response = order.convert();

        assertNotNull(response);
        assertEquals(order.getOrderId(), response.orderId());
        assertEquals(order.getTotal(), response.total());
        assertEquals(1, response.products().size());
    }

    @Test
    void testAddValueProductInOrderValue() {
        order.addValueProductInOrderValue(new BigDecimal("25.50"));

        assertEquals(new BigDecimal("125.50").setScale(2, 2), order.getTotal());
    }

    @Test
    void testAddOrUpdateProduct_ProductExists() {
        CustomerOrderRequest request = mockCustomerOrderRequest();

        order.addOrUpdateProduct(request);

        assertEquals(new BigDecimal("50.00"), order.getProducts().get(0).getValue());
    }

    @Test
    void testAddOrUpdateProduct_ProductDoesNotExist() {
        CustomerOrderRequest request = mockCustomerOrderRequest();

        order.addOrUpdateProduct(request);

        assertEquals(2, order.getProducts().size());
        assertEquals(new BigDecimal("0"), order.getProducts().get(1).getValue());
    }
}
