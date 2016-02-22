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

import hm.binkley.spring.axon.AxonDistributedCommandBusAutoConfiguration
        .AxonDistributedCommandBusProperties;
import lombok.Getter;
import lombok.Setter;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.distributed.DistributedCommandBus;
import org.axonframework.commandhandling.distributed.jgroups.JGroupsConnector;
import org.axonframework.eventstore.EventStore;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.jgroups.JChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;

/**
 * {@code AxonDistributedCommandBusAutoConfiguration} autoconfigures Axon
 * Framework for Spring Boot with a distributed command bus using JGroups. Use
 * {@link EnableAutoConfiguration} on your configuration class, and define a
 * bean for {@link EventStore} (see {@link AxonAutoConfiguration} for
 * details).  Then define application properties for JGroups: <pre>   axon:
 *   jgroups:
 *     cluster-name: <i>test</i>
 *     configuration: <i>classpath:/my-config.xml</i>  # Optional</pre>
 * Autoconfiguration defaults to XML serialization for JGroups.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @see AxonAutoConfiguration
 */
@AutoConfigureBefore(AxonAutoConfiguration.class)
@ConditionalOnClass(CommandBus.class)
@Configuration
@EnableConfigurationProperties(AxonDistributedCommandBusProperties.class)
public class AxonDistributedCommandBusAutoConfiguration {
    @Autowired
    private AxonDistributedCommandBusProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public JChannel jChannel()
            throws Exception {
        final Resource configuration = properties.getConfiguration();
        return null == configuration ? new JChannel()
                : new JChannel(configuration.getFile());
    }

    @Bean
    @ConditionalOnMissingBean
    public CommandBus localSegmentCommandBus() {
        return new SimpleCommandBus();
    }

    @Bean
    @ConditionalOnMissingBean
    public Serializer serializer() {
        return new XStreamSerializer();
    }

    @Bean
    @ConditionalOnMissingBean
    public JGroupsConnector jGroupsConnector(final JChannel channel,
            @Qualifier("localSegmentCommandBus") final CommandBus commandBus,
            final Serializer serializer) {
        return new JGroupsConnector(channel, properties.getClusterName(),
                commandBus, serializer);
    }

    @Bean
    @ConditionalOnMissingBean
    @Primary
    public DistributedCommandBus distributedCommandBus(
            final JGroupsConnector connector) {
        return new DistributedCommandBus(connector);
    }

    @ConfigurationProperties("axon.jgroups")
    @Getter
    @Setter
    public static class AxonDistributedCommandBusProperties {
        /** The JGroups cluster name. */
        private String clusterName;
        /**
         * The Spring resource path to JGroups XML configuration, optioanl.
         */
        private Resource configuration;
    }
}
