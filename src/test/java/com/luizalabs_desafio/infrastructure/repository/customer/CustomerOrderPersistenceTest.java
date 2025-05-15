package com.luizalabs_desafio.infrastructure.repository.customer;

import com.luizalabs_desafio.core.customer.CustomerOrderData;
import com.luizalabs_desafio.core.customer.entity.CustomerOrderDataJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.luizalabs_desafio.TestMockFactory.mockCustomerOrderData;
import static com.luizalabs_desafio.TestMockFactory.mockCustomerOrderDataTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class CustomerOrderPersistenceTest {

    @Autowired
    private CustomerOrderJpaRepository customerOrderJpaRepository;

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @InjectMocks
    private CustomerOrderPersistence customerOrderPersistence;

    @Test
    void testSaveCustomerOrder() {
        CustomerOrderData orderData = mockCustomerOrderData();
        when(customerOrderRepository.save(orderData)).thenReturn(orderData);

        CustomerOrderData savedOrder = customerOrderPersistence.saveCustomerOrder(orderData);

        assertNotNull(savedOrder);
        assertEquals(orderData.getUserId(), savedOrder.getUserId());
        verify(customerOrderRepository, times(1)).save(orderData);
    }

    @Test
    void testGetCustomerOrders() {
        List<CustomerOrderData> orders = List.of(mockCustomerOrderData());
        when(customerOrderRepository.findAll()).thenReturn(orders);

        List<CustomerOrderData> retrievedOrders = customerOrderPersistence.getCustomerOrders();

        assertNotNull(retrievedOrders);
        assertEquals(1, retrievedOrders.size());
        verify(customerOrderRepository, times(1)).findAll();
    }

    @Test
    void testFindCustomerOrderById_Found() {
        CustomerOrderData orderData = mockCustomerOrderData();
        when(customerOrderRepository.findById("1")).thenReturn(Optional.of(orderData));

        Optional<CustomerOrderData> retrievedOrder = customerOrderPersistence.findCustomerOrderById("1");

        assertTrue(retrievedOrder.isPresent());
        assertEquals("Joao", retrievedOrder.get().getName());
        verify(customerOrderRepository, times(1)).findById("1");
    }

    @Test
    void testFindCustomerOrderById_NotFound() {
        when(customerOrderRepository.findById("99")).thenReturn(Optional.empty());

        Optional<CustomerOrderData> retrievedOrder = customerOrderPersistence.findCustomerOrderById("99");

        assertFalse(retrievedOrder.isPresent());
        verify(customerOrderRepository, times(1)).findById("99");
    }

    @Test
    void testFindCustomerOrderByUserId_Found() {
        CustomerOrderData orderData = mockCustomerOrderData();
        when(customerOrderRepository.findCustomerOrderDataByUserId(2)).thenReturn(Optional.of(orderData));

        Optional<CustomerOrderData> retrievedOrder = customerOrderPersistence.findCustomerOrderByUserId(2);

        assertTrue(retrievedOrder.isPresent());
        assertEquals("Joao", retrievedOrder.get().getName());
        verify(customerOrderRepository, times(1)).findCustomerOrderDataByUserId(2);
    }

    @Test
    void testFindCustomerOrderByUserId_NotFound() {
        when(customerOrderRepository.findCustomerOrderDataByUserId(99)).thenReturn(Optional.empty());

        Optional<CustomerOrderData> retrievedOrder = customerOrderPersistence.findCustomerOrderByUserId(99);

        assertFalse(retrievedOrder.isPresent());
        verify(customerOrderRepository, times(1)).findCustomerOrderDataByUserId(99);
    }

    /* Integration test coverage */
    @Test
    void testSaveCustomerOrderJpa() {
        CustomerOrderDataJpa order = mockCustomerOrderDataTest();
        CustomerOrderDataJpa savedOrder = customerOrderJpaRepository.save(order);

        assertNotNull(savedOrder);
        assertEquals(order.getUserId(), savedOrder.getUserId());
    }

    @Test
    void testFindCustomerOrderById() {
        CustomerOrderDataJpa order = mockCustomerOrderDataTest();
        customerOrderJpaRepository.save(order);

        Optional<CustomerOrderDataJpa> retrievedOrder = customerOrderJpaRepository.findById(Long.valueOf(order.getId()));

        assertTrue(retrievedOrder.isPresent());
        assertEquals("Joao", retrievedOrder.get().getName());
    }

    @Test
    void testFindCustomerOrderByUserId() {
        CustomerOrderDataJpa order = mockCustomerOrderDataTest();
        customerOrderJpaRepository.save(order);

        Optional<CustomerOrderDataJpa> retrievedOrder = customerOrderJpaRepository.findCustomerOrderDataTestByUserId(order.getUserId());

        assertTrue(retrievedOrder.isPresent());
        assertEquals("Joao", retrievedOrder.get().getName());
    }

    @Test
    void testGetCustomerOrdersJpa() {
        customerOrderJpaRepository.save(mockCustomerOrderDataTest());

        assertEquals(1, customerOrderJpaRepository.findAll().size());
    }
}
