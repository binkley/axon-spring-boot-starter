package hm.binkley.spring.axon;

import org.axonframework.commandhandling.CommandHandlerInterceptor;
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

public class CommandHandlerInterceptorAutomation
        implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        final ConfigurableApplicationContext context
                = (ConfigurableApplicationContext) event.getSource();
        final List<CommandHandlerInterceptor> interceptors = new ArrayList<>(
                beansOfTypeIncludingAncestors(context.getBeanFactory(),
                        CommandHandlerInterceptor.class).values());
        sort(interceptors);

        for (final SimpleCommandBus commandBus :
                beansOfTypeIncludingAncestors(
                context.getBeanFactory(), SimpleCommandBus.class).values())
            commandBus.setHandlerInterceptors(interceptors);
    }
}
