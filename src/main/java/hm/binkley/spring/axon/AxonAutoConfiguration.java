/*
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 *
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information, please refer to <http://unlicense.org/>.
 */

package hm.binkley.spring.axon;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.domain.IdentifierFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.supporting.VolatileEventStore;
import org.springframework.beans.factory.serviceloader.ServiceListFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code AxonAutoConfiguration} autoconfigures Axon Framework for Spring
 * Boot.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation.
 * @todo Should many of these beans be left out, requiring user to provide?
 * @todo Switch to META-INF/spring.factories and drop @EnableAxon
 */
@Configuration
@ConditionalOnClass(CommandBus.class)
public class AxonAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public CommandBus commandBus() {
        return new SimpleCommandBus();
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandGateway commandGateway(final CommandBus commandBus) {
        return new DefaultCommandGateway(commandBus);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventStore eventStore()
            throws Exception {
        return new VolatileEventStore();
    }

    @SuppressWarnings("MethodReturnOfConcreteClass")
    @Bean
    public EventSourcingRepositoryFactory eventSourcingRepositoryFactory(
            final CommandBus commandBus, final EventBus eventBus,
            final EventStore eventStore) {
        return new EventSourcingRepositoryFactory(commandBus, eventBus,
                eventStore);
    }

    @Bean
    public ServiceListFactoryBean serviceListFactoryBean() {
        final ServiceListFactoryBean factoryBean
                = new ServiceListFactoryBean();
        factoryBean.setServiceType(AbstractAnnotatedAggregateRoot.class);
        return factoryBean;
    }

    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor(
            final CommandBus commandBus) {
        final AnnotationCommandHandlerBeanPostProcessor bean
                = new AnnotationCommandHandlerBeanPostProcessor();
        // TODO: Javadoc says this is auto-detected from Spring context
        bean.setCommandBus(commandBus);
        return bean;
    }

    @Bean
    public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor(
            final EventBus eventBus) {
        final AnnotationEventListenerBeanPostProcessor bean
                = new AnnotationEventListenerBeanPostProcessor();
        // TODO: Javadoc says this is auto-detected from Spring context
        bean.setEventBus(eventBus);
        return bean;
    }

    /** @todo Javadoc says this is auto-detected from ServiceLoader */
    @Bean
    @ConditionalOnMissingBean
    public IdentifierFactory identifierFactory() {
        return IdentifierFactory.getInstance();
    }
}
