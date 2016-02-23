package hm.binkley.spring.axon.monitoring;

import hm.binkley.spring.axon.monitoring.MonitoringTestConfiguration
        .Processed;
import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.EventProcessingMonitor;

import java.util.ArrayList;
import java.util.List;

final class TestEventProcessingMonitor
        implements EventProcessingMonitor {
    final List<Processed> processed = new ArrayList<>();

    @Override
    public void onEventProcessingCompleted(
            final List<? extends EventMessage> eventMessages) {
        processed.add(new Processed((List) eventMessages, null));
    }

    @Override
    public void onEventProcessingFailed(
            final List<? extends EventMessage> eventMessages,
            final Throwable cause) {
        processed.add(new Processed((List) eventMessages, cause));
    }
}
