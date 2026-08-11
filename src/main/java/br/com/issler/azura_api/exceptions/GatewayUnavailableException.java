package br.com.issler.azura_api.exceptions;

public class GatewayUnavailableException extends RuntimeException {
    public GatewayUnavailableException(String message) {
        super(message);
    }
}
