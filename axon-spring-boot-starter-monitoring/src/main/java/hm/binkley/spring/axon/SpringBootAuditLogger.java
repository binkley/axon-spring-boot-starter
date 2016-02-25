package hm.binkley.spring.axon;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.axonframework.auditing.AuditDataProvider;
import org.axonframework.auditing.AuditLogger;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.domain.EventMessage;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiFunction;

import static org.axonframework.auditing.CorrelationAuditDataProvider
        .DEFAULT_CORRELATION_KEY;

@RequiredArgsConstructor
class SpringBootAuditLogger
        implements AuditLogger, ApplicationEventPublisherAware {
    private final AuditDataProvider auditDataProvider;
    @Setter
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void logSuccessful(final CommandMessage<?> command,
            final Object returnValue, final List<EventMessage> events) {
        applicationEventPublisher.publishEvent(
                new AuditApplicationEvent(null, "AXON-COMMAND",
                        new SuccessfulDataMap(auditDataProvider, command,
                                returnValue, events)));
    }

    @Override
    public void logFailed(final CommandMessage<?> command,
            final Throwable failureCause, final List<EventMessage> events) {
        applicationEventPublisher.publishEvent(
                new AuditApplicationEvent(null, "AXON-COMMAND",
                        new FailedDataMap(auditDataProvider, command,
                                failureCause, events)));
    }

    private static class SuccessfulDataMap
            extends LinkedHashMap<String, Object> {
        SuccessfulDataMap(final AuditDataProvider auditDataProvider,
                final CommandMessage<?> command, final Object returnValue,
                final List<EventMessage> events) {
            putAll(auditDataProvider.provideAuditDataFor(command));
            compute("command-name", throwIfPresent(command.getCommandName()));
            compute(DEFAULT_CORRELATION_KEY,
                    throwIfPresent(command.getIdentifier()));
            compute("command-success", throwIfPresent(true));
            compute("command-return-value", throwIfPresent(returnValue));
            compute("command-events", throwIfPresent(events));
        }
    }

    private static class FailedDataMap
            extends LinkedHashMap<String, Object> {
        FailedDataMap(final AuditDataProvider auditDataProvider,
                final CommandMessage<?> command, final Throwable failureCause,
                final List<EventMessage> events) {
            putAll(auditDataProvider.provideAuditDataFor(command));
            compute("command-name", throwIfPresent(command.getCommandName()));
            compute(DEFAULT_CORRELATION_KEY,
                    throwIfPresent(command.getIdentifier()));
            compute("command-success", throwIfPresent(false));
            compute("command-failure-cause", throwIfPresent(failureCause));
            compute("command-events", throwIfPresent(events));
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
