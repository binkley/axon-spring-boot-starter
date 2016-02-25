package hm.binkley.spring.axon;

/**
 * {@code DuplicateAuditKeyException} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 */
public final class DuplicateAuditKeyException
        extends RuntimeException {
    DuplicateAuditKeyException(final String key) {
        super("Duplicate audit key: " + key);
    }
}
