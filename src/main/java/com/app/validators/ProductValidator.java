package com.app.validators;

import com.app.dto.ProducerDto;
import com.app.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductValidator {
    @Value("${validation.product.name}")
    private String nameRegex;
    @Value("${validation.product.price.min}")
    private BigDecimal priceMin;
    @Value("${validation.product.price.max}")
    private BigDecimal priceMax;

    private boolean isNameValid(String name) {
        return name != null && name.matches(nameRegex);
    }

    private boolean isPriceValid(BigDecimal price) {
        return price != null && price.compareTo(priceMin) >= 0 && price.compareTo(priceMax) <= 0;
    }

    private boolean isProducerValid(ProducerDto producerDto) {
        return producerDto != null && (producerDto.getId() != null || producerDto.getName() != null);
    }

    public ValidationErrors validate(ProductDto productDto) {

        ValidationErrors errors = new ValidationErrors();

        if (!isNameValid(productDto.getName())) {
            errors.addError("name", "NAME VALIDATION ERROR");
        }

        if (!isPriceValid(productDto.getPrice())) {
            errors.addError("price", "PRICE VALIDATION ERROR");
        }

        return errors;
    }
}
