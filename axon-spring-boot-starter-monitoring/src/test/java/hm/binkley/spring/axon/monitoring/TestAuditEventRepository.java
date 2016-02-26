package hm.binkley.spring.axon.monitoring;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;

import java.util.Date;
import java.util.List;

class TestAuditEventRepository
        implements AuditEventRepository {
    private final MonitoringTestConfiguration configuration;

    TestAuditEventRepository(
            final MonitoringTestConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public List<AuditEvent> find(final String principal, final Date after) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(final AuditEvent event) {
        configuration.trail.add(event);
    }
}
