package com.luizalabs_desafio.infrastructure.repository.customer;

import com.luizalabs_desafio.core.customer.CustomerOrderData;
import com.luizalabs_desafio.core.customer.CustomerOrderRepositoryFacade;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomerOrderPersistence implements CustomerOrderRepositoryFacade {
    private final CustomerOrderRepository customerOrderRepository;

    public CustomerOrderPersistence(CustomerOrderRepository customerOrderRepository) {
        this.customerOrderRepository = customerOrderRepository;
    }

    @Override
    public CustomerOrderData saveCustomerOrder(CustomerOrderData customerOrderData) {
        return customerOrderRepository.save(customerOrderData);
    }

    @Override
    public List<CustomerOrderData> getCustomerOrders() {
        return customerOrderRepository.findAll();
    }

    @Override
    public Optional<CustomerOrderData> findCustomerOrderById(String id) {
        return customerOrderRepository.findById(id);
    }

    @Override
    public Optional<CustomerOrderData> findCustomerOrderByUserId(Integer id) {
        return customerOrderRepository.findCustomerOrderDataByUserId(id);
    }
}
