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
import java.util.Map;

import static org.axonframework.auditing.CorrelationAuditDataProvider
        .DEFAULT_CORRELATION_KEY;

@RequiredArgsConstructor
public class SpringBootAuditLogger
        implements AuditLogger, EventProcessingMonitor,
        ApplicationEventPublisherAware {
    public static final String PARENT_CORRELATION_ID = inNamespace(
            "parent-correlation-id");
    public static final String MESSAGE_TYPE = inNamespace("message-type");
    public static final String RETURN_VALUE = inNamespace("return-value");
    public static final String FAILURE_CAUSE = inNamespace("failure-cause");
    public static final String EVENTS = inNamespace("events");

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

    private static String inNamespace(final String key) {
        return SpringBootAuditLogger.class.getName() + ':' + key;
    }

    private static Map<String, Object> successfulCommand(
            final MessageAuditDataProvider provider,
            final CommandMessage<?> command, final Object returnValue,
            final List<EventMessage> events) {
        return auditData(provider, command, command.getCommandName(),
                returnValue, null, events);
    }

    private static Map<String, Object> successfulEvent(
            final MessageAuditDataProvider provider,
            final EventMessage event) {
        return auditData(provider, event, event.getPayloadType().getName(),
                null, null, null);
    }

    private static Map<String, Object> failedCommand(
            final MessageAuditDataProvider provider,
            final CommandMessage<?> command, final Throwable failureCause,
            final List<EventMessage> events) {
        return auditData(provider, command, command.getCommandName(), null,
                failureCause, events);
    }

    private static Map<String, Object> failedEvent(
            final MessageAuditDataProvider provider, final EventMessage event,
            final Throwable failureCause) {
        return auditData(provider, event, event.getPayloadType().getName(),
                null, failureCause, null);
    }

    private static Map<String, Object> auditData(
            final MessageAuditDataProvider provider, final Message<?> message,
            final String name, final Object returnValue,
            final Throwable failureCause, final List<EventMessage> events) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.putAll(provider.provideAuditDataFor(message));
        map.put(DEFAULT_CORRELATION_KEY, message.getIdentifier());
        map.put(MESSAGE_TYPE, name);
        map.put(RETURN_VALUE, returnValue);
        map.put(FAILURE_CAUSE, failureCause);
        map.put(EVENTS, events);
        return map;
    }
}
