package com.luizalabs_desafio.infrastructure.adapter;

import com.luizalabs_desafio.application.mapper.input.CustomerOrderRequest;
import com.luizalabs_desafio.core.exception.InvalidFileFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Validator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.luizalabs_desafio.core.utils.ValidatorAndFormatter.*;

@Slf4j
public class FileAdapter {
    public static List<CustomerOrderRequest> parseFileToUserRequest(MultipartFile file, Validator validator) {
        log.info("Received and reading file");
        List<CustomerOrderRequest> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                CustomerOrderRequest input = parseLine(line);
                validatedInstance(input, validator);
                users.add(input);
            }
        } catch (IOException e) {
            log.error("Error processing file {}", e.getMessage());
            throw new InvalidFileFormatException(String.format("Error processing file: %s", e.getMessage()));
        }
        return users;
    }

    public static CustomerOrderRequest parseLine(String input) {
        log.info("Started parse file to request");
        String regex = "^(.{10})\\s+([\\D]+?)(\\d{10})(\\d{10})\\s+(\\d+\\.\\d{1,2})(\\d{8})$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input.trim());

        if (matcher.matches()) {
            String userId = matcher.group(1);
            String userName = matcher.group(2).trim();
            String orderId = matcher.group(3);
            String productId = matcher.group(4);
            String value = matcher.group(5);
            String dateOrder = matcher.group(6);

            log.info("Parse concluded with success");
            return new CustomerOrderRequest(
                    Integer.parseInt(userId),
                    userName,
                    formatterStringToInteger(orderId),
                    formatterStringToInteger(productId),
                    formatterStringByBigDecimal(value),
                    formattDate(dateOrder));
        } else {
            log.error("Not possible to converted file");
            throw new InvalidFileFormatException("Input did not match the pattern");
        }
    }

}
