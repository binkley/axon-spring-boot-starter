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

import hm.binkley.spring.axon.AxonClusterAutoConfiguration
        .AxonClusterProperties;
import lombok.Data;
import org.axonframework.eventhandling.Cluster;
import org.axonframework.eventhandling.ClusterSelector;
import org.axonframework.eventhandling.ClusteringEventBus;
import org.axonframework.eventhandling.DefaultClusterSelector;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventBusTerminal;
import org.axonframework.eventhandling.SimpleCluster;
import org.axonframework.eventsourcing.annotation
        .AbstractAnnotatedAggregateRoot;
import org.axonframework.eventstore.EventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition
        .ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties
        .EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ServiceLoader;

/**
 * {@code AxonAutoConfiguration} autoconfigures Axon Framework for Spring
 * Boot.  Use {@link EnableAutoConfiguration} on your configuration class, and
 * define a bean for {@link EventStore}.
 * <p>
 * A minimal configuration is: <pre>   &#64;Configuration
 * &#64;EnableAutoConfiguration
 * public class AConfiguration {
 *     &#64;Bean
 *     public EventStore eventStore() {
 *         return ...;
 *     }
 * }</pre> In other classes inject Axon types
 * normally with {@code @Autowired}.  When injecting
 * repositories include the bean name: <pre>   &#64;Autowired
 * &#64;Qualifier("someAggregateRepository")
 * private Repository&lt;SomeAggregate&gt; repository;</pre>
 * For autoconfiguration to create the repository, mark your aggregate root
 * class with {@code @MetaInfServices} and extend {@link
 * AbstractAnnotatedAggregateRoot}:
 * <pre>   &#64;MetaInfServices
 * public class SomeAggregate
 *         extends AbstractAnnotatedAggregateRoot&lt;SomeIDType&gt;
 *     &#64;AggregateIdentifier
 *     private SomeIDType id;
 * }</pre>  Autoconfigurating repositories uses standard {@link
 * ServiceLoader} to discover aggregate root classes.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 */
@AutoConfigureBefore(AxonQueryAutoConfiguration.class)
@ConditionalOnClass(Cluster.class)
@Configuration
@EnableConfigurationProperties(AxonClusterProperties.class)
public class AxonClusterAutoConfiguration {
    @Autowired
    private AxonClusterProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public Cluster simpleCluster() {
        return new SimpleCluster(properties.getName());
    }

    @Bean
    @ConditionalOnMissingBean
    public ClusterSelector defaultClusterSelector(final Cluster cluster) {
        return new DefaultClusterSelector(cluster);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBusTerminal simpleEventBusTerminal() {
        return new SimpleEventBusTerminal();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBus clusteringEventBus(final ClusterSelector selecter,
            final EventBusTerminal terminal) {
        return new ClusteringEventBus(selecter, terminal);
    }

    @ConfigurationProperties("axon.cluster")
    @Data
    public static final class AxonClusterProperties {
        private String name;
    }
}
