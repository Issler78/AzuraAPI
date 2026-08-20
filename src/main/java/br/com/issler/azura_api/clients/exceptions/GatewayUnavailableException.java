package br.com.issler.azura_api.clients.exceptions;

public class GatewayUnavailableException extends RuntimeException {
    public GatewayUnavailableException(String message) {
        super(message);
    }
}
