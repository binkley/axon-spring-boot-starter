package hm.binkley.spring.axon.metadata;

import org.axonframework.domain.MetaData;
import org.axonframework.eventhandling.annotation.EventHandler;

public class SuccessfulEventHandler {
    @EventHandler
    public void handle(final SuccessfulEvent Event,
            final MetaData metaData) {}
}
