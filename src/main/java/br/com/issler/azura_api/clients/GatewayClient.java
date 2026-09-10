package br.com.issler.azura_api.clients;

import br.com.issler.azura_api.clients.mappers.GatewayErrorMapper;
import br.com.issler.azura_api.database.models.PaymentEntity;
import br.com.issler.azura_api.clients.dtos.responses.GatewayPaymentResponse;
import br.com.issler.azura_api.clients.dtos.requests.GatewayRequestBody;
import br.com.issler.azura_api.dtos.payment.responses.PaymentResponse;
import br.com.issler.azura_api.clients.exceptions.GatewayUnexpectedResponseException;
import br.com.issler.azura_api.mappers.IPaymentMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class GatewayClient {
    private final RestClient restClient;
    private final String gatewayKey;
    private final IPaymentMapper paymentMapper;
    private final GatewayErrorMapper errorMapper;

    public GatewayClient(
            @Value("${spring.external.gateway-url}")
            String gatewayUrl,
            @Value("${spring.external.gateway-key}")
            String gatewayKey,
            @Value("${spring.external.connect-timeout}")
            long connectTimeout,
            @Value("${spring.external.read-timeout}")
            long readTimeout,
            IPaymentMapper paymentMapper,
            GatewayErrorMapper errorMapper
    ){
        this.gatewayKey = gatewayKey;
        this.paymentMapper = paymentMapper;
        this.errorMapper = errorMapper;

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(client);
        requestFactory.setReadTimeout(Duration.ofMillis(readTimeout));

        this.restClient = RestClient.builder()
                .baseUrl(gatewayUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public PaymentResponse send(PaymentEntity payment, String payerCpf){
        // gatewayPaymentResponse and paymentResponse are the same, but we need to map them because the gateway response has a different name for the fields in the future
        GatewayRequestBody gatewayRequestBody = GatewayRequestBody.builder()
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .payerCpf(payerCpf)
                .build();

        try {
            GatewayPaymentResponse result = restClient.post()
                    .uri("/v1/payments")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-API-KEY", gatewayKey)
                    .body(gatewayRequestBody)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (request, response) -> {
                        throw errorMapper.map(response);
                    })
                    .body(GatewayPaymentResponse.class);


            if(result == null || result.gatewayPaymentId() == null) {
                throw new GatewayUnexpectedResponseException("Gateway returned a null response");
            }

            return paymentMapper.toPaymentResponse(result);
        } catch (ResourceAccessException e){
            throw errorMapper.map(e);
        }
    }
}
