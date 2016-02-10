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

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation
        .AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.annotation
        .AbstractAnnotatedAggregateRoot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition
        .ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Set;

import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

/**
 * {@code AxonQueryConfiguration} configures Axon Framework for Spring Boot
 * but only for query (read side).  Use {@link EnableAxonQuery} on your
 * configuration class.
 * <p>
 * A minimal configuration is: <pre>   &#64;Configuration
 * &#64;EnableAxonQuery
 * public class AConfiguration {}</pre> In other classes inject Axon types
 * normally with {@code @Autowired}.
 * <p>
 * If your classpath includes aggregate roots referred to in {@code
 * META-INF/services}, Spring wiring will fail suggesting you switch to full
 * Axon support (read and write sides).
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 */
@Configuration
@ConditionalOnClass(EventBus.class)
public class AxonQueryConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @PostConstruct
    public void registerRepositories() {
        final Set<String> roots = stream(
                load(AbstractAnnotatedAggregateRoot.class).spliterator(),
                false).
                map(Object::getClass).
                map(Class::getName).
                collect(toSet());
        if (!roots.isEmpty())
            throw new IllegalStateException(
                    "Aggregate roots found in META-INF/services.  Did you "
                            + "mean to use axon-spring-boot-starter instead"
                            + " of axon-spring-boot-starter-query? " + roots);
    }

    @Bean
    public AnnotationEventListenerBeanPostProcessor
    annotationEventListenerBeanPostProcessor() {
        return new AnnotationEventListenerBeanPostProcessor();
    }
}
