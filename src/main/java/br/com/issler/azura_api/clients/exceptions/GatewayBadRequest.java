package br.com.issler.azura_api.clients.exceptions;

public class GatewayBadRequest extends RuntimeException {
    public GatewayBadRequest(String message) {
        super(message);
    }
}
