package br.com.issler.azura_api.exceptions;

public class CategoryInUseException extends Exception {
    public CategoryInUseException(String message) {
        super(message);
    }
}
