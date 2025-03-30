package com.app.bank.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEvent {

    @EventListener
    public void onAuthorizationFailure(AuthorizationDeniedEvent<Authentication> deniedEvent) {
        log.error("User {} authentication failed. Decision: {}",
                deniedEvent.getAuthentication().get().getName(),
                deniedEvent.getAuthorizationResult().toString());
    }

}
