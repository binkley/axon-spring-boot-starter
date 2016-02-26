package hm.binkley.spring.axon.metadata;

import org.axonframework.domain.MetaData;
import org.axonframework.eventhandling.annotation.EventHandler;

public class FailedEventHandler {
    @EventHandler
    public void handle(final FailedEvent event, final MetaData metaData) {
        throw event.getCause();
    }
}
