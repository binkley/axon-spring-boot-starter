package hm.binkley.spring.axon;

/**
 * {@code DuplicateAuditKeyException} is thrown when copying metadata if a
 * user-supplied metadata key duplicates already used.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Rethink this exception
 * @see SpringBootAuditLogger#EVENTS
 * @see SpringBootAuditLogger#FAILURE_CAUSE
 * @see SpringBootAuditLogger#MESSAGE_TYPE
 * @see SpringBootAuditLogger#RETURN_VALUE
 */
public final class DuplicateAuditKeyException
        extends RuntimeException {
    DuplicateAuditKeyException(final String key) {
        super("Duplicate audit key: " + key);
    }
}
