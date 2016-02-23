package hm.binkley.spring.axon;

import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.EventProcessingMonitor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

public class EventProcessingMonitorAutomation
        implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final ConfigurableApplicationContext context
                = (ConfigurableApplicationContext) event.getSource();
        for (final Cluster cluster : context.getBeansOfType(Cluster.class)
                .values())
            context.getBeansOfType(EventProcessingMonitor.class).values().
                    forEach(cluster::subscribeEventProcessingMonitor);
    }
}
