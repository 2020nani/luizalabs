package com.luizalabs_desafio.infrastructure.repository.customer;

import com.luizalabs_desafio.core.customer.entity.CustomerOrderDataJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerOrderJpaRepository extends JpaRepository<CustomerOrderDataJpa, Long> {
    Optional<CustomerOrderDataJpa> findCustomerOrderDataTestByUserId(Integer userId);
}

