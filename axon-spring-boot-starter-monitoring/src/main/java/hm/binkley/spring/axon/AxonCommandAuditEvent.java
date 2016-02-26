package hm.binkley.spring.axon;

import org.axonframework.commandhandling.CommandMessage;

import java.util.Date;
import java.util.Map;

/**
 * {@code AxonCommandAuditEvent} is a Spring application audit event for Axon
 * commands..
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 */
public class AxonCommandAuditEvent
        extends AxonAuditEvent {
    public static final String AXON_COMMAND_AUDIT_TYPE = CommandMessage.class
            .getName();

    public AxonCommandAuditEvent(final String principal,
            final Map<String, Object> data) {
        super(principal, AXON_COMMAND_AUDIT_TYPE, data);
    }

    public AxonCommandAuditEvent(final String principal,
            final String... data) {
        super(principal, AXON_COMMAND_AUDIT_TYPE, data);
    }

    public AxonCommandAuditEvent(final Date timestamp, final String principal,
            final Map<String, Object> data) {
        super(timestamp, principal, AXON_COMMAND_AUDIT_TYPE, data);
    }
}
