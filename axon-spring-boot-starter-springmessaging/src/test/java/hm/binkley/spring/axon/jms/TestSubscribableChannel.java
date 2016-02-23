package hm.binkley.spring.axon.jms;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.AbstractSubscribableChannel;

import java.util.ArrayList;
import java.util.List;

public final class TestSubscribableChannel
        extends AbstractSubscribableChannel {
    public final List<Message<?>> sentMessages = new ArrayList<>();

    @Override
    protected boolean sendInternal(final Message<?> message,
            final long timeout) {
        return sentMessages.add(message);
    }
}
