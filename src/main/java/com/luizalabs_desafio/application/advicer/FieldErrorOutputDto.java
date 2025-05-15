package com.luizalabs_desafio.application.advicer;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldErrorOutputDto {

    private String field;
    private String message;

    public FieldErrorOutputDto(String field, String message) {
        this.field = field;
        this.message = message;
    }
    public FieldErrorOutputDto(String message) {
        this.message = message;
    }
}
