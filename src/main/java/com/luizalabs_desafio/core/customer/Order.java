package com.luizalabs_desafio.core.customer;

import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;
import com.luizalabs_desafio.application.mapper.output.OrderDetailsResponse;
import com.luizalabs_desafio.application.mapper.output.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.luizalabs_desafio.core.utils.ValidatorAndFormatter.formatDateFromString;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private Integer orderId;
    private BigDecimal total;
    private LocalDate date;
    private List<Product> products;
    public OrderDetailsResponse convert() {
        List<ProductResponse> productResponses = products.stream()
                .map(Product::convert).toList();
        return new OrderDetailsResponse(orderId,total,formatDateFromString(date),productResponses);
    }

    public void addValueProductInOrderValue(BigDecimal value) {
        this.total = total.add(value);
    }

    public void addOrUpdateProduct(CustomerOrderRequest request) {
        Optional<Product> productFound = products.stream()
                .filter(product -> Objects.equals(product.getProductId(), request.productId()))
                .findFirst();
        productFound.ifPresentOrElse(
                product -> product.addValueProduct(request.value()),
                () -> products.add(new Product(request.productId(), request.value()))
        );
    }
}
