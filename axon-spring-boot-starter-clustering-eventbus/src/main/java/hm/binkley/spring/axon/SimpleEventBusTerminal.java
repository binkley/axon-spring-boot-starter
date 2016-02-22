package hm.binkley.spring.axon;

import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.EventBusTerminal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {@code SimpleEventBusTerminal} <strong>needs documentation</strong>.
 *
 * @author <a href="mailto:boxley@thoughtworks.com">Brian Oxley</a>
 * @todo Needs documentation
 * @todo Why private in Axon Framework?  This may be automated!
 */
public class SimpleEventBusTerminal
        implements EventBusTerminal {
    private final List<Cluster> clusters = new CopyOnWriteArrayList<>();

    @Override
    public void publish(final EventMessage... events) {
        for (final Cluster cluster : clusters)
            cluster.publish(events);
    }

    @Override
    public void onClusterCreated(final Cluster cluster) {
        clusters.add(cluster);
    }
}
