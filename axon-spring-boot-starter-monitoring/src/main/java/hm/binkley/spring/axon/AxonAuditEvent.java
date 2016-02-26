package hm.binkley.spring.axon;

import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;

import java.util.Date;
import java.util.Map;

/**
 * {@code AxonAuditEvent} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation
 */
public abstract class AxonAuditEvent
        extends AuditApplicationEvent {
    protected AxonAuditEvent(final String principal, final String type,
            final Map<String, Object> data) {
        super(principal, type, data);
    }

    protected AxonAuditEvent(final String principal, final String type,
            final String... data) {
        super(principal, type, data);
    }

    protected AxonAuditEvent(final Date timestamp, final String principal,
            final String type, final Map<String, Object> data) {
        super(timestamp, principal, type, data);
    }
}
