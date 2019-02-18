package com.app.validators;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ValidationErrors {
    private Map<String, String> errors = new HashMap<>();

    public void addError(String field, String message) {
        errors.put(field, message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public String errors() {
        return errors
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(", "));
    }
}
