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

package hm.binkley.spring.axon.monitoring;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.GenericDomainEventMessage;
import org.axonframework.eventhandling.Cluster;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MonitoringTestConfiguration.class)
public final class MonitoringIT {
    @Autowired
    private CommandGateway commands;
    @Autowired
    private Cluster events;
    @Autowired
    private MonitoringTestConfiguration configuration;
    private List<AuditEvent> trail;

    @Before
    public void rememberTrail() {
        trail = configuration.trail;
    }

    @After
    public void clearTrail() {
        trail.clear();
    }

    @Test
    public void shouldAuditSuccessfulCommands() {
        final SuccessfulCommand payload = new SuccessfulCommand();
        commands.send(payload);

        assertThat(trail).hasSize(1);
        final Map<String, Object> data = trail.get(0).getData();
        assertThat(data.get("command-name")).
                isEqualTo(payload.getClass().getName());
        assertThat(data.get("command-success")).
                isEqualTo(true);
        assertThat(data.get("command-return-value")).
                isEqualTo(3);
    }

    @Test
    public void shouldAuditFailedCommands() {
        final FailedCommand payload = new FailedCommand();
        commands.send(payload);

        assertThat(trail).hasSize(1);
        final Map<String, Object> data = trail.get(0).getData();
        assertThat(data.get("command-name")).
                isEqualTo(payload.getClass().getName());
        assertThat(data.get("command-success")).
                isEqualTo(false);
        assertThat(data.get("command-failure-cause")).
                isInstanceOf(FailedException.class);
    }

    @Test
    public void shouldAuditSuccessfulEvents() {
        final SuccessfulEvent payload = new SuccessfulEvent();
        events.publish(new GenericDomainEventMessage<>("abc", 1L, payload));

        assertThat(trail).hasSize(1);
        final Map<String, Object> data = trail.get(0).getData();
        assertThat(data.get("event-name")).
                isEqualTo(payload.getClass().getName());
        assertThat(data.get("event-success")).
                isEqualTo(true);
    }

    @Test
    public void shouldAuditFailedEvents() {
        final FailedEvent payload = new FailedEvent();
        try {
            events.publish(
                    new GenericDomainEventMessage<>("abc", 1L, payload));
            fail();
        } catch (final FailedException ignored) {
            assertThat(trail).hasSize(1);
            final Map<String, Object> data = trail.get(0).getData();
            assertThat(data.get("event-name")).
                    isEqualTo(payload.getClass().getName());
            assertThat(data.get("event-success")).
                    isEqualTo(false);
            assertThat(data.get("event-failure-cause")).
                    isInstanceOf(FailedException.class);
        }
    }
}
