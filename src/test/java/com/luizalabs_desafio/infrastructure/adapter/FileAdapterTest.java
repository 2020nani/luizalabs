package com.luizalabs_desafio.infrastructure.adapter;

import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;
import com.luizalabs_desafio.core.exception.InvalidFileFormatException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileAdapterTest {
    private static String MOCK_LINE_FILE = "0000000070         Palmer Prosacco00000007530000000003     10.0020210308";
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testParseFileToUserRequest_ValidFile() {
        String validFileContent = MOCK_LINE_FILE;
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain",
                validFileContent.getBytes(StandardCharsets.UTF_8));

        List<CustomerOrderRequest> requests = FileAdapter.parseFileToUserRequest(file, validator);

        assertNotNull(requests);
        assertEquals(1, requests.size());
        assertEquals("Palmer Prosacco", requests.get(0).userName());
    }

    @Test
    void testParseFileToUserRequest_InvalidFileFormat() {
        String invalidFileContent = "INVALID CONTENT";
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain",
                invalidFileContent.getBytes(StandardCharsets.UTF_8));

        assertThrows(InvalidFileFormatException.class, () -> FileAdapter.parseFileToUserRequest(file, validator));
    }

    @Test
    void testParseLine_ValidInput() {
        String validInput = MOCK_LINE_FILE;

        CustomerOrderRequest request = FileAdapter.parseLine(validInput);

        assertNotNull(request);
        assertEquals(70, request.userId());
        assertEquals("Palmer Prosacco", request.userName());
        assertEquals(753, request.orderId());
        assertEquals(3, request.productId());
        assertEquals(10.00, request.value().doubleValue());
        assertEquals(LocalDate.of(2021, 3,8), request.orderDate());
    }

    @Test
    void testParseLine_InvalidInput() {
        String invalidInput = "INVALID LINE";

        assertThrows(InvalidFileFormatException.class, () -> FileAdapter.parseLine(invalidInput));
    }
}
