package com.luizalabs_desafio.core.customer.impl;

import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;
import com.luizalabs_desafio.core.customer.*;
import com.luizalabs_desafio.core.exception.CustomerNotFoundException;
import com.luizalabs_desafio.core.exception.InvalidRangeDateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
@Slf4j
public class CustomerOrderServiceImpl implements CustomerOrderService {
    private final CustomerOrderRepositoryFacade customerOrderRepositoryFacade;
    private final MongoTemplate mongoTemplate;

    public CustomerOrderServiceImpl(CustomerOrderRepositoryFacade customerOrderRepositoryFacade, MongoTemplate mongoTemplate) {
        this.customerOrderRepositoryFacade = customerOrderRepositoryFacade;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String saveCustomerOrders(List<CustomerOrderRequest> orders) {
        log.info("Start persistence customer orders");
        try {
            for(CustomerOrderRequest request : orders) {
                if(!customerOrderRepositoryFacade.findCustomerOrderByUserId(request.userId()).isEmpty()) {
                    persistenceAddOrderByUser(request);
                } else {
                    persistenceNewOrderByUser(request);
                }
            }
            log.info("Customer orders save with success");
            return "Upload and orders save with success";
        } catch (Exception e) {
            log.error("Error to persist customer orders");
            return "Error to persist customer orders";
        }

    }

    @Override
    public List<CustomerOrderData> listCustomerOrders(Integer orderId, LocalDate startDate, LocalDate endDate) {
        if(nonNull(startDate) && nonNull(endDate) && !validRangeDate(startDate,endDate)) {
            throw new InvalidRangeDateException("The start date must be earlier than or equal to the end date.");

        }
        if(isNull(orderId) && (isNull(startDate) || isNull(endDate))) {
            return customerOrderRepositoryFacade.getCustomerOrders();
        }

        return findOrderByFilters(orderId, startDate, endDate);
    }

    private boolean validRangeDate(LocalDate startDate, LocalDate endDate) {
        return startDate.isBefore(endDate) || startDate.isEqual(endDate);
    }

    @Override
    public CustomerOrderData getCustomerOrderByUserId(Integer id) {
        return customerOrderRepositoryFacade.findCustomerOrderByUserId(id).orElseThrow(
                () -> new CustomerNotFoundException(
                        String.format("Customer Order Not exists with userId: %s",id)
                )
        );
    }

    public void persistenceAddOrderByUser(CustomerOrderRequest request) {
        CustomerOrderData customerOrderData = customerOrderRepositoryFacade
                .findCustomerOrderByUserId(request.userId())
                .orElseThrow(
                        () -> new CustomerNotFoundException(
                                String.format("Customer Order Not exists with userId: %s",request.userId())
                        )
                );
        Optional<Order> orderFound = customerOrderData.getOrders().stream()
                .filter(order -> Objects.equals(order.getOrderId(), request.orderId()))
                .findFirst();
        orderFound.ifPresent(order -> {
            order.addValueProductInOrderValue(request.value());
            order.addOrUpdateProduct(request);
        });

        customerOrderRepositoryFacade.saveCustomerOrder(customerOrderData);
    }

    public void persistenceNewOrderByUser(CustomerOrderRequest request) {
        CustomerOrderData customerOrderData = request.convert();
        customerOrderRepositoryFacade.saveCustomerOrder(customerOrderData);
    }

    public List<CustomerOrderData> findOrderByFilters(Integer orderId, LocalDate startDate, LocalDate endDate) {
        Query query = new Query();

        if (nonNull(orderId)) {
            query.addCriteria(Criteria.where("orders").elemMatch(Criteria.where("orderId").is(orderId)));
        }

        if (nonNull(startDate) && nonNull(endDate)) {
            query.addCriteria(Criteria.where("orders.date").gte(startDate).lte(endDate));
        }

        return mongoTemplate.find(query, CustomerOrderData.class);
    }

}
