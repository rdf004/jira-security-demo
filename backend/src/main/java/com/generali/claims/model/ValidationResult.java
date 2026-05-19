package com.generali.claims.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private boolean valid;
    private List<String> errors =
        new ArrayList<>();
    private List<String> missingDocuments =
        new ArrayList<>();

    public ValidationResult() {
        this.valid = true;
    }

    public void addError(String error) {
        this.errors.add(error);
        this.valid = false;
    }

    public void addMissingDocument(String doc) {
        this.missingDocuments.add(doc);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getMissingDocuments() {
        return missingDocuments;
    }

    public void setMissingDocuments(
        List<String> missingDocuments
    ) {
        this.missingDocuments = missingDocuments;
    }
}
