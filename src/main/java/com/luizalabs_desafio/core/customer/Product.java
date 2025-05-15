package com.luizalabs_desafio.core.customer;

import com.luizalabs_desafio.application.mapper.output.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Integer productId;
    private BigDecimal value;
    public ProductResponse convert() {
        return new ProductResponse(productId, value);
    }

    public void addValueProduct(BigDecimal value) {
        this.value = this.value.add(value);
    }
}
