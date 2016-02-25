package hm.binkley.spring.axon;

/**
 * {@code AuditTrail} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
public interface AuditTrail {
    void record(final CommandAuditEvent event);
}
