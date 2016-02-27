package hm.binkley.spring.axon;

import org.axonframework.domain.EventMessage;

import java.util.Date;
import java.util.Map;

/**
 * {@code AxonEventAuditEvent} is a Spring application audit event for Axon
 * events..
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 */
public class AxonEventAuditEvent
        extends AxonAuditEvent {
    public static final String AXON_EVENT_AUDIT_TYPE = EventMessage.class
            .getName();

    public AxonEventAuditEvent(final String principal,
            final Map<String, Object> data) {
        super(principal, AXON_EVENT_AUDIT_TYPE, data);
    }

    public AxonEventAuditEvent(final String principal, final String... data) {
        super(principal, EventMessage.class.getName(), data);
    }

    public AxonEventAuditEvent(final Date timestamp, final String principal,
            final Map<String, Object> data) {
        super(timestamp, principal, EventMessage.class.getName(), data);
    }
}
