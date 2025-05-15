package com.luizalabs_desafio.core.customer;

import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;

import java.time.LocalDate;
import java.util.List;

public interface CustomerOrderService {
    String saveCustomerOrders(List<CustomerOrderRequest> users);
    List<CustomerOrderData> listCustomerOrders(Integer orderId, LocalDate startDate, LocalDate endDate);
    CustomerOrderData getCustomerOrderByUserId(Integer id);
}
