package hm.binkley.spring.axon.monitoring;

import org.axonframework.eventhandling.annotation.EventHandler;

public class FailedEventHandler {
    @EventHandler
    public void handle(final FailedEvent event) {
        throw new FailedException();
    }
}
