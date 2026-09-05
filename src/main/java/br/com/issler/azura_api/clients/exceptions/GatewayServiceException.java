package br.com.issler.azura_api.clients.exceptions;

import br.com.issler.azura_api.clients.enums.GatewayErrorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GatewayServiceException extends RuntimeException {
    private GatewayErrorType type;

    public GatewayServiceException(String message, GatewayErrorType type) {
        super(message);
        this.type = type;
    }

    public GatewayServiceException(String message, Throwable cause, GatewayErrorType type) {
        super(message, cause);
        this.type = type;
    }
}
