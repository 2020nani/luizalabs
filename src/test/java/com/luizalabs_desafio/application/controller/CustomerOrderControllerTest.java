package com.luizalabs_desafio.application.controller;

import com.luizalabs_desafio.application.mapper.output.CustomerOrderResponse;
import com.luizalabs_desafio.core.customer.CustomerOrderData;
import com.luizalabs_desafio.core.customer.CustomerOrderService;
import com.luizalabs_desafio.infrastructure.adapter.FileAdapter;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.luizalabs_desafio.TestMockFactory.mockCustomerOrderData;
import static com.luizalabs_desafio.TestMockFactory.mockCustomerOrderResponse;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerOrderControllerTest {

    @Mock
    private CustomerOrderService customerOrderService;

    @Mock
    private Validator validator;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private CustomerOrderController customerOrderController;

    @BeforeEach
    void setUp() {
        customerOrderController = new CustomerOrderController(customerOrderService, validator);
    }

    @Test
    void testUploadFileOrder() {
        try (MockedStatic<FileAdapter> mockedStatic = mockStatic(FileAdapter.class)) {
            mockedStatic.when(() -> FileAdapter.parseFileToUserRequest(file, validator))
                    .thenReturn(List.of(mockCustomerOrderData()));

            when(customerOrderService.saveCustomerOrders(any())).thenReturn("Order Saved");

            String response = customerOrderController.uploadFileOrder(file);

            assertEquals("Order Saved", response);
            verify(customerOrderService, times(1)).saveCustomerOrders(any());
        }
    }

    @Test
    void testGetUsersAndOrdersWithoutFilter() {
        when(customerOrderService.listCustomerOrders(null, null, null)).thenReturn(List.of(mockCustomerOrderData()));

        List<CustomerOrderResponse> response = customerOrderController.getUsersAndOrders(null,null,null);

        assertNotNull(response);
        verify(customerOrderService, times(1)).listCustomerOrders(null, null, null);
    }

    @Test
    void testGetOrderByUserId() {
        CustomerOrderData orderData = mock(CustomerOrderData.class);
        when(orderData.convert()).thenReturn(mockCustomerOrderResponse());
        when(customerOrderService.getCustomerOrderByUserId(1)).thenReturn(orderData);

        CustomerOrderResponse response = customerOrderController.getOrderByUserId(1);

        assertNotNull(response);
        assertEquals(1, response.userId());
        verify(customerOrderService, times(1)).getCustomerOrderByUserId(1);
    }
}
