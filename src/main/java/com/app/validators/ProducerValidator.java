package com.app.validators;

import com.app.dto.ProducerDto;
import org.springframework.stereotype.Component;

@Component
public class ProducerValidator{

    private final static String COUNTRY_REGEX = "[A-Z]+";
    private final static String NAME_REGEX = "[A-Z]+";

    private static boolean isNameValid(String name) {
        return name != null && name.matches(NAME_REGEX);
    }

    private static boolean isCountryValid(String country) {
        return country != null && country.matches(COUNTRY_REGEX);
    }

    public ValidationErrors validate(ProducerDto producer) {

        ValidationErrors errors = new ValidationErrors();

        if (!isNameValid(producer.getName())) {
            errors.addError("name", "NAME VALIDATION ERROR");
        }

        if (!isCountryValid(producer.getCountry())) {
            errors.addError("country", "COUNTRY VALIDATION ERROR");
        }

        return errors;
    }
}
