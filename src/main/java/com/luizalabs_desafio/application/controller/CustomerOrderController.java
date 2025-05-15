package com.luizalabs_desafio.application.controller;

import com.luizalabs_desafio.application.mapper.output.CustomerOrderResponse;
import com.luizalabs_desafio.core.customer.CustomerOrderData;
import com.luizalabs_desafio.core.customer.CustomerOrderService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Validator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

import static com.luizalabs_desafio.infrastructure.adapter.FileAdapter.parseFileToUserRequest;

@RestController
public class CustomerOrderController {
    private final CustomerOrderService customerOrderService;
    private final Validator validator;

    public CustomerOrderController(CustomerOrderService customerOrderService, Validator validator) {
        this.customerOrderService = customerOrderService;
        this.validator = validator;
    }

    @PostMapping(value= "/upload-file-order", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadFileOrder(
            @Parameter(description = "Arquivo a ser enviado")
            @RequestPart("file") MultipartFile file) {

        return customerOrderService.saveCustomerOrders(parseFileToUserRequest(file, validator));
    }

    @GetMapping("/user/order")
    public List<CustomerOrderResponse> getUsersAndOrders(
            @RequestParam(required = false)
            @Parameter(description = "ID do pedido") Integer orderId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Data de início no formato yyyy-MM-dd ex(2020-10-10)")
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Data de fim no formato yyyy-MM-dd ex(2020-10-10)")
            LocalDate endDate
    ) {

        return customerOrderService.
                listCustomerOrders(orderId,startDate,endDate).stream()
                .map(CustomerOrderData::convert)
                .toList();
    }

    @GetMapping("/order/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOrderResponse getOrderByUserId(
            @PathVariable("userId")
            @Parameter(
                    name = "userId",
                    description = "ID do usuário",
                    example = "123"
            )
            Integer userId
    ) {
        return customerOrderService.getCustomerOrderByUserId(userId).convert();
    }
}
