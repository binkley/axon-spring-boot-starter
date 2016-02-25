package hm.binkley.spring.axon;

import lombok.RequiredArgsConstructor;
import org.axonframework.auditing.AuditLogger;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.domain.EventMessage;

import java.util.List;

import static hm.binkley.spring.axon.CommandAuditEvent.failed;
import static hm.binkley.spring.axon.CommandAuditEvent.successful;

@RequiredArgsConstructor
final class RecordingAuditLogger
        implements AuditLogger {
    private final AuditTrail trail;

    @Override
    public void logSuccessful(final CommandMessage<?> command,
            final Object returnValue, final List<EventMessage> events) {
        trail.record(successful(command, returnValue, events));
    }

    @Override
    public void logFailed(final CommandMessage<?> command,
            final Throwable failureCause, final List<EventMessage> events) {
        trail.record(failed(command, failureCause, events));
    }
}
