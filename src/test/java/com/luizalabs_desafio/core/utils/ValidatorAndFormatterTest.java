package com.luizalabs_desafio.core.utils;

import com.luizalabs_desafio.core.exception.InvalidFormatDateException;
import com.luizalabs_desafio.core.exception.InvalidFormatNumberException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ValidatorAndFormatterTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidateNonNullString() {
        assertEquals("", ValidatorAndFormatter.validateNonNullstring(null));
        assertEquals("Teste", ValidatorAndFormatter.validateNonNullstring("Teste"));
    }

    @Test
    void testFormatterStringToIntegerValid() {
        assertEquals(123, ValidatorAndFormatter.formatterStringToInteger("123"));
    }

    @Test
    void testFormatterStringToIntegerInvalid() {
        assertThrows(InvalidFormatNumberException.class, () -> ValidatorAndFormatter.formatterStringToInteger(null));
        assertThrows(InvalidFormatNumberException.class, () -> ValidatorAndFormatter.formatterStringToInteger("abc"));
    }

    @Test
    void testFormattDateValid() {
        LocalDate expectedDate = LocalDate.of(2023, 5, 11);
        assertEquals(expectedDate, ValidatorAndFormatter.formattDate("20230511"));
    }

    @Test
    void testFormattDateInvalid() {
        assertThrows(InvalidFormatDateException.class, () -> ValidatorAndFormatter.formattDate(null));
        assertThrows(InvalidFormatDateException.class, () -> ValidatorAndFormatter.formattDate("abc"));
    }

    @Test
    void testFormatDateFromString() {
        LocalDate date = LocalDate.of(2023, 5, 11);
        assertEquals("2023-05-11", ValidatorAndFormatter.formatDateFromString(date));
    }

    @Test
    void testFormatterStringByBigDecimalValid() {
        assertEquals(new BigDecimal("123.45"), ValidatorAndFormatter.formatterStringByBigDecimal("123.45"));
        assertEquals(new BigDecimal("100.5"), ValidatorAndFormatter.formatterStringByBigDecimal("100.5"));
    }

    @Test
    void testFormatterStringByBigDecimalInvalid() {
        assertThrows(InvalidFormatNumberException.class, () -> ValidatorAndFormatter.formatterStringByBigDecimal(null));
        assertThrows(InvalidFormatNumberException.class, () -> ValidatorAndFormatter.formatterStringByBigDecimal("abc"));
    }

    @Test
    void testValidatedInstance() {
        TestEntity entity = new TestEntity("valid");
        assertDoesNotThrow(() -> ValidatorAndFormatter.validatedInstance(entity, validator));

        TestEntity invalidEntity = new TestEntity("");
        assertThrows(IllegalArgumentException.class, () -> ValidatorAndFormatter.validatedInstance(invalidEntity, validator));
    }


    static class TestEntity {
        @jakarta.validation.constraints.NotBlank
        private final String name;

        public TestEntity(String name) {
            this.name = name;
        }
    }
}
