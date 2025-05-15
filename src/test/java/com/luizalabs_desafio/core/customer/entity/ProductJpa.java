package com.luizalabs_desafio.core.customer.entity;

import com.luizalabs_desafio.application.mapper.output.ProductResponse;
import com.luizalabs_desafio.core.customer.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class ProductJpa {
    @Id
    private Integer productId;
    private BigDecimal value;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderJpa order;
    public ProductResponse convert() {
        return new ProductResponse(productId, value);
    }

    public void addValueProduct(BigDecimal value) {
        this.value = this.value.add(value).setScale(2,2);
    }
}
