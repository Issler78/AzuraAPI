package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.dtos.webhook.requests.WebhookRequest;
import br.com.issler.azura_api.exceptions.NotFoundException;
import br.com.issler.azura_api.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/webhooks")
@RequiredArgsConstructor
@Validated
public class WebhookController {
    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmPayment(@Valid @RequestBody WebhookRequest webhookRequest) throws NotFoundException {
        paymentService.setAsPayed(webhookRequest);
    }
}
