package hm.binkley.spring.axon.monitoring;

import org.axonframework.eventhandling.annotation.EventHandler;

public class SuccessfulEventHandler {
    @EventHandler
    public void handle(final SuccessfulEvent Event) {}
}
