package hm.binkley.spring.axon;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.axonframework.auditing.AuditLogger;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.domain.EventMessage;
import org.axonframework.domain.Message;
import org.axonframework.eventhandling.EventProcessingMonitor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;

import static hm.binkley.spring.axon.SpringBootAuditLogger.AuditDataMap
        .failedCommand;
import static hm.binkley.spring.axon.SpringBootAuditLogger.AuditDataMap
        .failedEvent;
import static hm.binkley.spring.axon.SpringBootAuditLogger.AuditDataMap
        .successfulCommand;
import static hm.binkley.spring.axon.SpringBootAuditLogger.AuditDataMap
        .successfulEvent;
import static org.axonframework.auditing.CorrelationAuditDataProvider
        .DEFAULT_CORRELATION_KEY;

@RequiredArgsConstructor
public class SpringBootAuditLogger
        implements AuditLogger, EventProcessingMonitor,
        ApplicationEventPublisherAware {
    public static final String NAME = "name";
    public static final String RETURN_VALUE = "return-value";
    public static final String FAILURE_CAUSE = "failure-cause";
    public static final String EVENTS = "events";

    private final MessageAuditDataProvider provider;
    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void logSuccessful(final CommandMessage<?> command,
            final Object returnValue, final List<EventMessage> events) {
        applicationEventPublisher.publishEvent(new AxonCommandAuditEvent(null,
                successfulCommand(provider, command, returnValue, events)));
    }

    @Override
    public void logFailed(final CommandMessage<?> command,
            final Throwable failureCause, final List<EventMessage> events) {
        applicationEventPublisher.publishEvent(new AxonCommandAuditEvent(null,
                failedCommand(provider, command, failureCause, events)));
    }

    @Override
    public void onEventProcessingCompleted(
            final List<? extends EventMessage> eventMessages) {
        for (final EventMessage event : eventMessages)
            applicationEventPublisher.publishEvent(
                    new AxonEventAuditEvent(null,
                            successfulEvent(provider, event)));
    }

    @Override
    public void onEventProcessingFailed(
            final List<? extends EventMessage> eventMessages,
            final Throwable cause) {
        for (final EventMessage event : eventMessages)
            applicationEventPublisher.publishEvent(
                    new AxonEventAuditEvent(null,
                            failedEvent(provider, event, cause)));
    }

    static final class AuditDataMap
            extends LinkedHashMap<String, Object> {
        static AuditDataMap successfulCommand(
                final MessageAuditDataProvider provider,
                final CommandMessage<?> command, final Object returnValue,
                final List<EventMessage> events) {
            return new AuditDataMap(provider, command,
                    command.getCommandName(), returnValue, null, events);
        }

        static AuditDataMap successfulEvent(
                final MessageAuditDataProvider provider,
                final EventMessage event) {
            return new AuditDataMap(provider, event,
                    event.getPayloadType().getName(), null, null, null);
        }

        static AuditDataMap failedCommand(
                final MessageAuditDataProvider provider,
                final CommandMessage<?> command, final Throwable failureCause,
                final List<EventMessage> events) {
            return new AuditDataMap(provider, command,
                    command.getCommandName(), null, failureCause, events);
        }

        static AuditDataMap failedEvent(
                final MessageAuditDataProvider provider,
                final EventMessage event, final Throwable failureCause) {
            return new AuditDataMap(provider, event,
                    event.getPayloadType().getName(), null, failureCause,
                    null);
        }

        private AuditDataMap(final MessageAuditDataProvider provider,
                final Message<?> message, final String name,
                final Object returnValue, final Throwable failureCause,
                final List<EventMessage> events) {
            putAll(provider.provideAuditDataFor(message));
            compute(DEFAULT_CORRELATION_KEY,
                    throwIfPresent(message.getIdentifier()));
            compute(NAME, throwIfPresent(name));
            compute(RETURN_VALUE, throwIfPresent(returnValue));
            compute(FAILURE_CAUSE, throwIfPresent(failureCause));
            compute(EVENTS, throwIfPresent(events));
        }
    }

    private static AuditDataPutter throwIfPresent(final Object value) {
        return new AuditDataPutter(value);
    }

    @RequiredArgsConstructor
    private static final class AuditDataPutter
            implements BiFunction<String, Object, Object> {
        private final Object value;

        @Override
        public Object apply(final String key, final Object oldValue) {
            if (null == oldValue)
                return value;
            throw new DuplicateAuditKeyException(key);
        }
    }
}
