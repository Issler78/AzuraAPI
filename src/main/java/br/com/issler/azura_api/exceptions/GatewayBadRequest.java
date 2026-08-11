package br.com.issler.azura_api.exceptions;

public class GatewayBadRequest extends RuntimeException {
    public GatewayBadRequest(String message) {
        super(message);
    }
}
