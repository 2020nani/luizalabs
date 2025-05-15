package com.luizalabs_desafio.infrastructure.repository.customer;

import com.luizalabs_desafio.core.customer.CustomerOrderData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerOrderRepository extends MongoRepository<CustomerOrderData, String> {
    Optional<CustomerOrderData> findCustomerOrderDataByUserId(Integer userId);
}
