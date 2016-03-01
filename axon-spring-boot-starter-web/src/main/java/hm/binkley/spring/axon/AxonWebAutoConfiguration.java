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

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.embedded
        .AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.support
        .AnnotationConfigWebApplicationContext;

/**
 * {@code AxonSpringMessagingAutoConfiguration} autoconfigures Axon Framework
 * for Spring Boot for monitoring.  Use {@link EnableAutoConfiguration} on
 * your configuration class, and define beans for {@link EventStore} and
 * {@link AuditEventRepository}.  A minimal configuration is:
 * <pre>&#64;Configuration
 * &#64;EnableAutoConfiguration
 * public class AConfiguration {
 *     &#64;Bean
 *     public {@link EventStore} eventStore() {
 *         return ...;
 *     }
 *
 *     &#64;Bean
 *     public {@link AuditEventRepository} auditEventRepository() {
 *         return ...;
 *     }
 * }</pre> In other classes inject Axon types
 * normally with {@code @Autowired}.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 */
@ConditionalOnClass({CommandBus.class})
@Configuration
public class AxonWebAutoConfiguration {
    @Autowired
    private ConfigurableApplicationContext context;

    @Bean
    @Primary
    public AnnotationConfigRegistry annotationConfigRegistry() {
        if (context instanceof AnnotationConfigRegistry)
            return (AnnotationConfigRegistry) context;
        else if (context instanceof
                AnnotationConfigEmbeddedWebApplicationContext)
            return new WebAnnotationConfigRegistryShim(context);
        else
            throw new IllegalStateException();
    }

    @RequiredArgsConstructor
    private static final class WebAnnotationConfigRegistryShim
            implements AnnotationConfigRegistry {
        private final ConfigurableApplicationContext context;

        @Override
        public void register(final Class<?>... annotatedClasses) {
            ((AnnotationConfigWebApplicationContext) context)
                    .register(annotatedClasses);
        }

        @Override
        public void scan(final String... basePackages) {
            throw new UnsupportedOperationException();
        }
    }
}
