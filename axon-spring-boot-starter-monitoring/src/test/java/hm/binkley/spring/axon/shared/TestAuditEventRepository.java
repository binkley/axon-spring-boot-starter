package hm.binkley.spring.axon.shared;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestAuditEventRepository
        implements AuditEventRepository {
    private final List<AuditEvent> trail = new ArrayList<>();

    @Override
    public List<AuditEvent> find(final String principal, final Date after) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(final AuditEvent event) {
        trail.add(event);
    }

    public void reset() {
        trail.clear();
    }

    public int received() {
        return trail.size();
    }

    public AuditEvent eventAt(final int sequence) {
        return trail.get(sequence);
    }
}
