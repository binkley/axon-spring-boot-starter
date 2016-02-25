package hm.binkley.spring.axon;

import org.axonframework.commandhandling.CommandDispatchInterceptor;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.BeanFactoryUtils
        .beansOfTypeIncludingAncestors;
import static org.springframework.core.annotation
        .AnnotationAwareOrderComparator.sort;

public class CommandDispatchInterceptorAutomation
        implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final ConfigurableApplicationContext context
                = (ConfigurableApplicationContext) event.getSource();
        final List<CommandDispatchInterceptor> interceptors = new ArrayList<>(
                beansOfTypeIncludingAncestors(context.getBeanFactory(),
                        CommandDispatchInterceptor.class).values());
        sort(interceptors);

        for (final SimpleCommandBus commandBus :
                beansOfTypeIncludingAncestors(
                context.getBeanFactory(), SimpleCommandBus.class).values())
            commandBus.setDispatchInterceptors(interceptors);
    }
}
