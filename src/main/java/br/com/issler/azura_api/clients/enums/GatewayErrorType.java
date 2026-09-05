package br.com.issler.azura_api.clients.enums;

public enum GatewayErrorType {
    CONNECT_TIMEOUT,
    READ_TIMEOUT,
    CONNECTION_FAILURE,

    RATE_LIMITED,
    SERVICE_UNAVAILABLE,

    CLIENT_ERROR,

    UNKNOWN
}
