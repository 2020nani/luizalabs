package com.luizalabs_desafio.core.customer;

import com.luizalabs_desafio.application.mapper.output.OrderDetailsResponse;
import com.luizalabs_desafio.application.mapper.output.CustomerOrderResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customer-order-data")
public class CustomerOrderData {
    @Id
    private String id;
    private Integer userId;
    private String name;
    private List<Order> orders;

    public CustomerOrderResponse convert() {
        List<OrderDetailsResponse> orderDetailsResponses = orders.stream()
                .map(Order::convert).toList();
        return new CustomerOrderResponse(userId,name,orderDetailsResponses);
    }
}
