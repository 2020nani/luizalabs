package com.luizalabs_desafio.core.utils;

import com.luizalabs_desafio.core.exception.InvalidFormatDateException;
import com.luizalabs_desafio.core.exception.InvalidFormatNumberException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class ValidatorAndFormatter {
    public static String validateNonNullstring(String value) {
        if(Objects.isNull(value)) {
            return "";
        }
        return value;
    }

    public static Integer formatterStringToInteger(String value) {
        if(Objects.isNull(value)){
            throw new InvalidFormatNumberException(String.format("Error formatted number %s", value));
        }
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            throw new InvalidFormatNumberException(String.format("Error formatted number %s", value));
        }
    }

    public static LocalDate formattDate(String value){
        if(Objects.isNull(value)){
            log.error("Erro to formatted date: {}");
            throw new InvalidFormatDateException(String.format("Date %s formatted invalid", value));
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {
            log.error("Erro to formatted date: {}", e.getMessage());
            throw new InvalidFormatDateException(String.format("Date %s formatted invalid", value));
        }

    }

    public static String formatDateFromString(LocalDate date) {
        DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            String dataFormatada = date.format(formater);
            return dataFormatada;
        } catch (Exception e) {
            log.error("Erro to formatted date: {}", e.getMessage());
            log.error(e.getMessage());
            throw new InvalidFormatDateException(String.format("Erro to formatted date", e.getMessage()));
        }

    }

    public static BigDecimal formatterStringByBigDecimal(String value) {
        if(Objects.isNull(value)){
            throw new InvalidFormatNumberException(String.format("Error formatted number %s", value));
        }
        try {
            return new BigDecimal(value);
        }catch (Exception e){
            throw new InvalidFormatNumberException(String.format("Error formatted number %s", value));
        }

    }

    public static <T> void validatedInstance(T input, Validator validator) {

        Set<ConstraintViolation<T>> violations = validator.validate(input);

        if (!violations.isEmpty()) {
            String mensagens = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .reduce((m1, m2) -> m1 + ", " + m2)
                    .orElse("Unknow invalidation error");

            throw new IllegalArgumentException(mensagens);
        }
    }

}
