package br.com.issler.azura_api.security;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class IdempotencyKeyProvider {
    @Transactional(propagation = Propagation.MANDATORY)
    public UUID generate() {
        return UUID.randomUUID();
    }
}
