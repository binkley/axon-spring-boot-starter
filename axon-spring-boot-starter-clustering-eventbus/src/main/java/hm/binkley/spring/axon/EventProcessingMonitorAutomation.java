package hm.binkley.spring.axon;

import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.EventProcessingMonitor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Collection;

import static org.springframework.beans.factory.BeanFactoryUtils
        .beansOfTypeIncludingAncestors;

public class EventProcessingMonitorAutomation
        implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final ConfigurableApplicationContext context
                = (ConfigurableApplicationContext) event.getSource();
        final Collection<EventProcessingMonitor> monitors
                = beansOfTypeIncludingAncestors(context.getBeanFactory(),
                EventProcessingMonitor.class).values();

        for (final Cluster cluster : beansOfTypeIncludingAncestors(
                context.getBeanFactory(), Cluster.class).values())
            monitors.forEach(cluster::subscribeEventProcessingMonitor);
    }
}
