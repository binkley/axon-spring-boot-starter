package hm.binkley.spring.axon;

import org.axonframework.auditing.AuditLogger;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.domain.EventMessage;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * {@code SpringAuditLogger} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
public class SpringAuditLogger
        implements AuditLogger, ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;

    @Override
    public void logSuccessful(final CommandMessage<?> command,
            final Object returnValue, final List<EventMessage> events) {
        publisher.publishEvent(new AuditApplicationEvent(null, "AXON-COMMAND",
                new LinkedHashMap<String, Object>() {{
                    put("command", command.getPayloadType());
                    put("metadata", command.getMetaData());
                    put("returnValue", returnValue);
                    put("events", events);
                }}));
    }

    @Override
    public void logFailed(final CommandMessage<?> command,
            final Throwable failureCause, final List<EventMessage> events) {
        publisher.publishEvent(new AuditApplicationEvent(null, "AXON-COMMAND",
                new LinkedHashMap<String, Object>() {{
                    put("command", command.getPayloadType());
                    put("metadata", command.getMetaData());
                    put("failureCause", failureCause);
                    put("events", events);
                }}));
    }

    @Override
    public void setApplicationEventPublisher(
            final ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}
