package com.luizalabs_desafio.core.customer;

import com.luizalabs_desafio.application.mapper.output.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1, new BigDecimal("100.00"));
    }

    @Test
    void testConvertProductToProductResponse() {
        ProductResponse response = product.convert();

        assertNotNull(response);
        assertEquals(product.getProductId(), response.productId());
        assertEquals(product.getValue(), response.value());
    }

    @Test
    void testAddValueProduct() {
        product.addValueProduct(new BigDecimal("50.5"));
        assertEquals(new BigDecimal("150.50"), product.getValue());
    }
}
