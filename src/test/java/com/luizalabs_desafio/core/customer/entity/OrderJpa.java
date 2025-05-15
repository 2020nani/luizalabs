package com.luizalabs_desafio.core.customer.entity;

import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;
import com.luizalabs_desafio.application.mapper.output.OrderDetailsResponse;
import com.luizalabs_desafio.application.mapper.output.ProductResponse;
import com.luizalabs_desafio.core.customer.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderJpa {
    @Id
    private Integer orderId;
    private BigDecimal total;
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "customer_order_id", nullable = false)
    private CustomerOrderDataJpa customerOrder;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductJpa> products;
    public OrderDetailsResponse convert() {
        List<ProductResponse> productResponses = products.stream()
                .map(ProductJpa::convert).toList();
        return new OrderDetailsResponse(orderId,total,"",productResponses);
    }

    public void addValueProductInOrderValue(BigDecimal value) {
        this.total = total.add(value).setScale(2, 2);
    }

    public void addOrUpdateProduct(CustomerOrderRequest request) {
        Optional<ProductJpa> productFound = products.stream()
                .filter(product -> Objects.equals(product.getProductId(), request.productId()))
                .findFirst();
        productFound.ifPresentOrElse(
                product -> product.addValueProduct(request.value()),
                () -> products.add(ProductJpa.builder()
                                .productId(request.productId())
                                .value(request.value())
                        .build())
        );
    }
}
