package com.luizalabs_desafio.core.customer.entity;

import com.luizalabs_desafio.application.mapper.output.CustomerOrderResponse;
import com.luizalabs_desafio.application.mapper.output.OrderDetailsResponse;
import com.luizalabs_desafio.core.customer.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_orders")
public class CustomerOrderDataJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer userId;
    private String name;
    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderJpa> orders;

    public CustomerOrderResponse convert() {
        List<OrderDetailsResponse> orderDetailsResponses = orders.stream()
                .map(OrderJpa::convert).toList();
        return new CustomerOrderResponse(userId,name,orderDetailsResponses);
    }
}
