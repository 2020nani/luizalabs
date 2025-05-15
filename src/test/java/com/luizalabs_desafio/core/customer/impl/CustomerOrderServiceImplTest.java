package com.luizalabs_desafio.core.customer.impl;

import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;
import com.luizalabs_desafio.core.customer.CustomerOrderData;
import com.luizalabs_desafio.core.customer.CustomerOrderRepositoryFacade;
import com.luizalabs_desafio.core.customer.Order;
import com.luizalabs_desafio.core.exception.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.luizalabs_desafio.TestMockFactory.mockCustomerOrderData;
import static com.luizalabs_desafio.TestMockFactory.mockCustomerOrderRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerOrderServiceImplTest {

    @Mock
    private CustomerOrderRepositoryFacade customerOrderRepositoryFacade;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CustomerOrderServiceImpl customerOrderService;

    @BeforeEach
    void setUp() {
        customerOrderService = new CustomerOrderServiceImpl(customerOrderRepositoryFacade, mongoTemplate);
    }

    @Test
    void testSaveCustomerOrders_NewOrder() {
        List<CustomerOrderRequest> orders = List.of(mockCustomerOrderRequest());
        when(customerOrderRepositoryFacade.findCustomerOrderByUserId(anyInt())).thenReturn(Optional.empty());
        String response = customerOrderService.saveCustomerOrders(orders);
        assertEquals("Upload and orders save with success", response);
        verify(customerOrderRepositoryFacade, times(1)).saveCustomerOrder(any());
    }


    @Test
    void testSaveCustomerOrders_AddOrderToExistingUser() {
        List<CustomerOrderRequest> orders = List.of(mockCustomerOrderRequest());
        when(customerOrderRepositoryFacade.findCustomerOrderByUserId(anyInt())).thenReturn(Optional.of(mockCustomerOrderData()));
        String response = customerOrderService.saveCustomerOrders(orders);
        assertEquals("Upload and orders save with success", response);
        verify(customerOrderRepositoryFacade, times(1)).saveCustomerOrder(any());
    }

    @Test
    void testSaveCustomerOrders_ExceptionHandling() {
        List<CustomerOrderRequest> orders = List.of(mockCustomerOrderRequest());
        doThrow(new RuntimeException("Database error")).when(customerOrderRepositoryFacade).saveCustomerOrder(any());
        String response = customerOrderService.saveCustomerOrders(orders);
        assertEquals("Error to persist customer orders", response);
    }

    @Test
    void testGetCustomerOrders_WithoutFilter() {
        when(customerOrderRepositoryFacade.getCustomerOrders()).thenReturn(List.of(mockCustomerOrderData()));
        List<CustomerOrderData> orders = customerOrderService.listCustomerOrders(null, null, null);
        assertFalse(orders.isEmpty());
        verify(customerOrderRepositoryFacade, times(1)).getCustomerOrders();
    }

    @Test
    void testGetCustomerOrders_WithFilter() {
        when(mongoTemplate.find(any(),any())).thenReturn(List.of(mockCustomerOrderData()));
        List<CustomerOrderData> orders = customerOrderService.listCustomerOrders(1, null, null);
        assertFalse(orders.isEmpty());
        verify(mongoTemplate, times(1)).find(any(),any());
    }

    @Test
    void testPersistenceAddOrderByUser_CustomerExists_OrderExists() {
        CustomerOrderRequest request = mockCustomerOrderRequest();
        CustomerOrderData customerOrderData = mockCustomerOrderData();
        Order order = customerOrderData.getOrders().get(0);
        when(customerOrderRepositoryFacade.findCustomerOrderByUserId(request.userId())).thenReturn(Optional.of(customerOrderData));
        customerOrderService.persistenceAddOrderByUser(request);
        verify(customerOrderRepositoryFacade, times(1)).findCustomerOrderByUserId(request.userId());
        verify(customerOrderRepositoryFacade, times(1)).saveCustomerOrder(customerOrderData);
        assertEquals(order.getTotal().add(request.value()), order.getTotal());
    }

    @Test
    void testPersistenceAddOrderByUser_CustomerExists_OrderDoesNotExist() {
        CustomerOrderRequest request = mockCustomerOrderRequest();
        CustomerOrderData customerOrderData = mockCustomerOrderData();
        when(customerOrderRepositoryFacade
                .findCustomerOrderByUserId(request.userId()))
                .thenReturn(Optional.of(customerOrderData));
        customerOrderService.persistenceAddOrderByUser(request);

        verify(customerOrderRepositoryFacade, times(1))
                .findCustomerOrderByUserId(request.userId());
        verify(customerOrderRepositoryFacade, times(1))
                .saveCustomerOrder(customerOrderData);
    }

    @Test
    void testPersistenceAddOrderByUser_OrderExists() {
        CustomerOrderRequest request = mockCustomerOrderRequest();
        CustomerOrderData customerOrderData = mockCustomerOrderData();
        Order order = customerOrderData.getOrders().get(0);
        when(customerOrderRepositoryFacade.findCustomerOrderByUserId(request.userId()))
                .thenReturn(Optional.of(customerOrderData));
        customerOrderService.persistenceAddOrderByUser(request);
        verify(customerOrderRepositoryFacade, times(1))
                .findCustomerOrderByUserId(request.userId());
        verify(customerOrderRepositoryFacade, times(1))
                .saveCustomerOrder(customerOrderData);
        assertEquals(order.getTotal().add(request.value()), order.getTotal());
    }

    @Test
    void testPersistenceAddOrderByUser_OrderDoesNotExist() {
        CustomerOrderRequest request = mockCustomerOrderRequest();
        CustomerOrderData customerOrderData = mockCustomerOrderData();
        customerOrderData.getOrders().clear();
        when(customerOrderRepositoryFacade.findCustomerOrderByUserId(request.userId()))
                .thenReturn(Optional.of(customerOrderData));
        customerOrderService.persistenceAddOrderByUser(request);
        verify(customerOrderRepositoryFacade, times(1))
                .findCustomerOrderByUserId(request.userId());
        verify(customerOrderRepositoryFacade, times(1))
                .saveCustomerOrder(customerOrderData);
        customerOrderData.getOrders().forEach(order -> {
            verify(order, never()).addValueProductInOrderValue(request.value());
            verify(order, never()).addOrUpdateProduct(request);
        });
    }

    @Test
    void testPersistenceAddOrderByUser_CustomerDoesNotExist() {
        CustomerOrderRequest request = mockCustomerOrderRequest();
        when(customerOrderRepositoryFacade
                .findCustomerOrderByUserId(request.userId())).thenReturn(Optional.empty());
        assertThrows(
                CustomerNotFoundException.class, () -> customerOrderService
                        .persistenceAddOrderByUser(request)
        );
        verify(customerOrderRepositoryFacade, times(1))
                .findCustomerOrderByUserId(request.userId());
        verify(customerOrderRepositoryFacade, never()).saveCustomerOrder(any());
    }

    @Test
    void testGetCustomerOrderByUserId_NotFound() {
        when(customerOrderRepositoryFacade
                .findCustomerOrderByUserId(anyInt())).thenReturn(Optional.empty());
        assertThrows(
                CustomerNotFoundException.class, () -> customerOrderService
                        .getCustomerOrderByUserId(1)
        );
        verify(customerOrderRepositoryFacade, times(1))
                .findCustomerOrderByUserId(1);
    }

    @Test
    void findOrderByFilters_FilterByOrderId() {
        Integer orderId = 123;

        List<CustomerOrderData> mockOrders = List.of(new CustomerOrderData("1", 456, "Pedido 1", List.of()));

        Query expectedQuery = new Query();
        expectedQuery.addCriteria(Criteria.where("orders").elemMatch(Criteria.where("orderId").is(orderId)));

        when(mongoTemplate.find(expectedQuery, CustomerOrderData.class)).thenReturn(mockOrders);

        List<CustomerOrderData> result = customerOrderService.findOrderByFilters(orderId, null, null);

        assertEquals(1, result.size());
        assertEquals(456, result.get(0).getUserId());
    }

    @Test
    void findOrderByFilters_filterByDateInterval() {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate endDate = LocalDate.of(2025, 5, 10);

        List<CustomerOrderData> mockOrders = List.of(new CustomerOrderData("2", 789, "Pedido 2", List.of()));

        Query expectedQuery = new Query();
        expectedQuery.addCriteria(Criteria.where("orders.date").gte(startDate).lte(endDate));

        when(mongoTemplate.find(expectedQuery, CustomerOrderData.class)).thenReturn(mockOrders);

        List<CustomerOrderData> result = customerOrderService.findOrderByFilters(null, startDate, endDate);

        assertEquals(1, result.size());
        assertEquals(789, result.get(0).getUserId());
    }

    @Test
    void findOrderByFilters_WithOutFilter_ShouldReturn() {
        List<CustomerOrderData> mockOrders = List.of(
                new CustomerOrderData("1", 123, "Pedido 1", List.of()),
                new CustomerOrderData("2", 456, "Pedido 2", List.of())
        );

        when(mongoTemplate.find(new Query(), CustomerOrderData.class)).thenReturn(mockOrders);

        List<CustomerOrderData> result = customerOrderService.findOrderByFilters(null, null, null);

        assertEquals(2, result.size());
    }
}