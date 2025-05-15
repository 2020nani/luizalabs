package com.luizalabs_desafio.application.advicer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FieldErrorOutputDtoTest {

    @Test
    void testConstructorWithFieldAndMessage() {
        FieldErrorOutputDto errorDto = new FieldErrorOutputDto("fieldName", "Error message");

        assertNotNull(errorDto);
        assertEquals("fieldName", errorDto.getField());
        assertEquals("Error message", errorDto.getMessage());
    }

    @Test
    void testConstructorWithOnlyMessage() {
        FieldErrorOutputDto errorDto = new FieldErrorOutputDto("Error message");

        assertNotNull(errorDto);
        assertNull(errorDto.getField());
        assertEquals("Error message", errorDto.getMessage());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        FieldErrorOutputDto errorDto = new FieldErrorOutputDto();
        errorDto.setField("newField");
        errorDto.setMessage("newMessage");

        assertNotNull(errorDto);
        assertEquals("newField", errorDto.getField());
        assertEquals("newMessage", errorDto.getMessage());
    }
}
