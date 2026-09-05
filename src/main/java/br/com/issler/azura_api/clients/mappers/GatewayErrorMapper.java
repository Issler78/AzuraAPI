package br.com.issler.azura_api.clients.mappers;

import br.com.issler.azura_api.clients.enums.GatewayErrorType;
import br.com.issler.azura_api.clients.exceptions.GatewayServiceException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.http.HttpConnectTimeoutException;
import java.net.http.HttpTimeoutException;

@Component
public class GatewayErrorMapper {

    // for http errors
    public GatewayServiceException map(ClientHttpResponse response) throws IOException {
        HttpStatusCode status = response.getStatusCode();

        if(status.value() == 429) return new GatewayServiceException("Gateway rate limit exceeded", GatewayErrorType.RATE_LIMITED);

        if(status.is4xxClientError()) return new GatewayServiceException("Gateway rejected the request", GatewayErrorType.CLIENT_ERROR);

        if(status.is5xxServerError()) return new GatewayServiceException("Gateway server error", GatewayErrorType.SERVICE_UNAVAILABLE);

        return new GatewayServiceException("Unexpected gateway error", GatewayErrorType.UNKNOWN);
    }

    // for communication errors
    public GatewayServiceException map(ResourceAccessException e){
        Throwable cause = e;

        while(cause != null){
            if(cause instanceof HttpConnectTimeoutException) return new GatewayServiceException("Gateway connect timeout", e, GatewayErrorType.CONNECT_TIMEOUT);

            if(cause instanceof HttpTimeoutException) return new GatewayServiceException("Gateway read timeout", e, GatewayErrorType.READ_TIMEOUT);

            if(cause instanceof ConnectException) return new GatewayServiceException("Failed to connect to gateway", e, GatewayErrorType.CONNECTION_FAILURE);

            cause = cause.getCause();
        }

        return new GatewayServiceException("Unexpected gateway error", GatewayErrorType.UNKNOWN);
    }

}
