package com.nbm.carrental.event;

import com.nbm.carrental.entity.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

// Add this annotation to the class to enable asynchronous processing
@Component
public class RegistrationEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public RegistrationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    // Add the @Async annotation to make this method execute asynchronously
    @Async
    public void publishEventAsync(User user, String applicationUrl) {
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl));
    }
}
