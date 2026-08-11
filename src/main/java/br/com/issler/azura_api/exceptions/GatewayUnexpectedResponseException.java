package br.com.issler.azura_api.exceptions;

public class GatewayUnexpectedResponseException extends RuntimeException {
    public GatewayUnexpectedResponseException(String message) {
        super(message);
    }
}
