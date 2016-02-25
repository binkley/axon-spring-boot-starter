package hm.binkley.spring.axon;

import lombok.Value;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.domain.EventMessage;

import java.util.List;

@Value
public final class CommandAuditEvent {
    CommandMessage<?> command;
    Object returnValue;
    Throwable failureCause;
    List<EventMessage> events;

    public static CommandAuditEvent successful(
            final CommandMessage<?> command, final Object returnValue,
            final List<EventMessage> events) {
        return new CommandAuditEvent(command, returnValue, null, events);
    }

    public static CommandAuditEvent failed(final CommandMessage<?> command,
            final Throwable failureCause, final List<EventMessage> events) {
        return new CommandAuditEvent(command, null, failureCause, events);
    }
}
