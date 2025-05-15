package com.luizalabs_desafio.core.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerOrderRepositoryFacade {
    public CustomerOrderData saveCustomerOrder(CustomerOrderData customerOrderData);
    public List<CustomerOrderData> getCustomerOrders();
    public Optional<CustomerOrderData> findCustomerOrderById(String id);

    public Optional<CustomerOrderData> findCustomerOrderByUserId(Integer userId);
}
