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

package hm.binkley.spring.axon.audit;

import hm.binkley.spring.axon.shared.TestAuditEventRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.GenericDomainEventMessage;
import org.axonframework.eventhandling.Cluster;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static hm.binkley.spring.axon.AxonCommandAuditEvent.AXON_COMMAND_AUDIT_TYPE;
import static hm.binkley.spring.axon.AxonEventAuditEvent.AXON_EVENT_AUDIT_TYPE;
import static hm.binkley.spring.axon.SpringBootAuditLogger.FAILURE_CAUSE;
import static hm.binkley.spring.axon.SpringBootAuditLogger.MESSAGE_TYPE;
import static hm.binkley.spring.axon.SpringBootAuditLogger.RETURN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AuditTestConfiguration.class)
public final class AuditIT {
    @Autowired
    private CommandGateway commands;
    @Autowired
    private Cluster events;
    @Autowired
    private TestAuditEventRepository trail;

    @After
    public void resetTrail() {
        trail.reset();
    }

    @Test
    public void shouldAuditSuccessfulCommands() {
        final SuccessfulCommand payload = new SuccessfulCommand();
        commands.send(payload);

        assertThat(trail.received()).
                isEqualTo(1);
        final AuditEvent auditEvent = trail.eventAt(0);
        assertThat(auditEvent.getType()).
                isEqualTo(AXON_COMMAND_AUDIT_TYPE);
        final Map<String, Object> data = auditEvent.getData();
        assertThat(data.get(MESSAGE_TYPE)).
                isEqualTo(payload.getClass().getName());
        assertThat(data.get(RETURN_VALUE)).
                isEqualTo(3);
        assertThat(data.get(FAILURE_CAUSE)).
                isNull();
    }

    @Test
    public void shouldAuditFailedCommands() {
        final FailedException cause = new FailedException();
        final FailedCommand payload = new FailedCommand(cause);
        commands.send(payload);

        assertThat(trail.received()).
                isEqualTo(1);
        final AuditEvent auditEvent = trail.eventAt(0);
        assertThat(auditEvent.getType()).
                isEqualTo(AXON_COMMAND_AUDIT_TYPE);
        final Map<String, Object> data = auditEvent.getData();
        assertThat(data.get(MESSAGE_TYPE)).
                isEqualTo(payload.getClass().getName());
        assertThat(data.get(RETURN_VALUE)).
                isNull();
        assertThat(data.get(FAILURE_CAUSE)).
                isSameAs(cause);
    }

    @Test
    public void shouldAuditSuccessfulEvents() {
        final SuccessfulEvent payload = new SuccessfulEvent();
        events.publish(new GenericDomainEventMessage<>("abc", 1L, payload));

        assertThat(trail.received()).
                isEqualTo(1);
        final AuditEvent auditEvent = trail.eventAt(0);
        assertThat(auditEvent.getType()).
                isEqualTo(AXON_EVENT_AUDIT_TYPE);
        final Map<String, Object> data = auditEvent.getData();
        assertThat(data.get(MESSAGE_TYPE)).
                isEqualTo(payload.getClass().getName());
        assertThat(data.get(FAILURE_CAUSE)).
                isNull();
    }

    @Test
    public void shouldAuditFailedEvents() {
        final FailedException cause = new FailedException();
        final FailedEvent payload = new FailedEvent(cause);
        try {
            events.publish(
                    new GenericDomainEventMessage<>("abc", 1L, payload));
            fail();
        } catch (final FailedException ignored) {
            assertThat(trail.received()).
                    isEqualTo(1);
            final AuditEvent auditEvent = trail.eventAt(0);
            assertThat(auditEvent.getType()).
                    isEqualTo(AXON_EVENT_AUDIT_TYPE);
            final Map<String, Object> data = auditEvent.getData();
            assertThat(data.get(MESSAGE_TYPE)).
                    isEqualTo(payload.getClass().getName());
            assertThat(data.get(FAILURE_CAUSE)).
                    isSameAs(cause);
        }
    }
}
