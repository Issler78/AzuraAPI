package br.com.issler.azura_api.clients.exceptions;

public class GatewayUnexpectedResponseException extends RuntimeException {
    public GatewayUnexpectedResponseException(String message) {
        super(message);
    }
}
