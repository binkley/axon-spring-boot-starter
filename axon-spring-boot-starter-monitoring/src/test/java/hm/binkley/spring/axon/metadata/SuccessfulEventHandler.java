package hm.binkley.spring.axon.metadata;

import org.axonframework.eventhandling.annotation.EventHandler;

public class SuccessfulEventHandler {
    @EventHandler
    public void handle(final SuccessfulEvent event) {}
}
