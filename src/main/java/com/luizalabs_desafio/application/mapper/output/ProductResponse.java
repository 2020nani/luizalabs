package com.luizalabs_desafio.application.mapper.output;

import java.math.BigDecimal;

public record ProductResponse (
        Integer productId,
        BigDecimal value
){
}
