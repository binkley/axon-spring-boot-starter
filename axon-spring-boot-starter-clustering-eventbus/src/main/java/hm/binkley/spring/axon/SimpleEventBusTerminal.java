package hm.binkley.spring.axon;

import org.axonframework.domain.EventMessage;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.ClusteringEventBus;
import org.axonframework.eventhandling.EventBusTerminal;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * {@code SimpleEventBusTerminal} is an explicit copy from {@link
 * ClusteringEventBus} which is marked {@code private}, so that
 * autoconfiguration can expose an {@link EventBusTerminal} bean to be
 * conditionally replaced.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
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
