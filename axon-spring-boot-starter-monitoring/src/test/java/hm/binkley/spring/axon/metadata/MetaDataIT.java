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

package hm.binkley.spring.axon.metadata;

import org.axonframework.commandhandling.gateway.CommandGateway;
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

import static hm.binkley.spring.axon.AxonCommandAuditEvent
        .AXON_COMMAND_AUDIT_TYPE;
import static hm.binkley.spring.axon.AxonEventAuditEvent
        .AXON_EVENT_AUDIT_TYPE;
import static hm.binkley.spring.axon.SpringBootAuditLogger.FAILURE_CAUSE;
import static hm.binkley.spring.axon.SpringBootAuditLogger.NAME;
import static hm.binkley.spring.axon.SpringBootAuditLogger.RETURN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MetaDataTestConfiguration.class)
public final class MetaDataIT {
    @Autowired
    private CommandGateway commands;
    @Autowired
    private MetaDataTestConfiguration configuration;
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
        final String aggregateId = "abc";
        final SuccessfulCommand payload = new SuccessfulCommand(aggregateId);
        commands.send(payload);

        assertThat(trail).hasSize(2);

        final AuditEvent commandAuditEvent = trail.get(1);
        assertThat(commandAuditEvent.getType()).
                isEqualTo(AXON_COMMAND_AUDIT_TYPE);
        final Map<String, Object> commandData = commandAuditEvent.getData();
        assertThat(commandData.get(NAME)).
                isEqualTo(SuccessfulCommand.class.getName());
        assertThat(commandData.get(RETURN_VALUE)).
                isEqualTo(aggregateId);
        assertThat(commandData.get(FAILURE_CAUSE)).
                isNull();

        final AuditEvent eventAuditEvent = trail.get(0);
        assertThat(eventAuditEvent.getType()).
                isEqualTo(AXON_EVENT_AUDIT_TYPE);
        final Map<String, Object> eventData = eventAuditEvent.getData();
        assertThat(eventData.get(NAME)).
                isEqualTo(SuccessfulEvent.class.getName());
        assertThat(eventData.get(FAILURE_CAUSE)).
                isNull();
    }

    @Test
    public void shouldAuditFailedCommands() {
        final FailedException cause = new FailedException();
        final FailedCommand payload = new FailedCommand("def", cause);
        commands.send(payload);

        assertThat(trail).hasSize(2);

        final AuditEvent commandAuditEvent = trail.get(1);
        assertThat(commandAuditEvent.getType()).
                isEqualTo(AXON_COMMAND_AUDIT_TYPE);
        final Map<String, Object> commandData = commandAuditEvent.getData();
        assertThat(commandData.get(NAME)).
                isEqualTo(FailedCommand.class.getName());
        assertThat(commandData.get(RETURN_VALUE)).
                isNull();
        assertThat(commandData.get(FAILURE_CAUSE)).
                isSameAs(cause);

        final AuditEvent eventAuditEvent = trail.get(0);
        assertThat(eventAuditEvent.getType()).
                isEqualTo(AXON_EVENT_AUDIT_TYPE);
        final Map<String, Object> eventData = eventAuditEvent.getData();
        assertThat(eventData.get(NAME)).
                isEqualTo(FailedEvent.class.getName());
        assertThat(eventData.get(FAILURE_CAUSE)).
                isSameAs(cause);
    }
}
