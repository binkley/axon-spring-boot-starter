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

package hm.binkley.spring.axon.flow;

import hm.binkley.spring.axon.shared.TestAuditEventRepository;
import org.axonframework.commandhandling.GenericCommandMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static hm.binkley.spring.axon.AxonCommandAuditEvent
        .AXON_COMMAND_AUDIT_TYPE;
import static hm.binkley.spring.axon.AxonEventAuditEvent
        .AXON_EVENT_AUDIT_TYPE;
import static hm.binkley.spring.axon.SpringBootAuditLogger.MESSAGE_TYPE;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.axonframework.correlation.CorrelationDataHolder
        .getCorrelationData;

@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = FlowTestConfiguration.class)
public final class FlowIT {
    @Autowired
    private CommandGateway commands;
    @Autowired
    private TestAuditEventRepository trail;

    @After
    public void resetTrail() {
        trail.reset();
    }

    @Test
    public void shouldAuditCommandToEventToCommand() {
        final String aggregateId = "abc";
        final InitialCommand payload = new InitialCommand(aggregateId);
        commands.send(payload);

        assertThat(trail.received()).
                isEqualTo(3);

        final AuditEvent initialCommandAuditEvent = trail.eventAt(2);
        assertThat(initialCommandAuditEvent.getType()).
                isEqualTo(AXON_COMMAND_AUDIT_TYPE);
        final Map<String, Object> initialCommandData
                = initialCommandAuditEvent.getData();
        assertThat(initialCommandData.get(MESSAGE_TYPE)).
                isEqualTo(InitialCommand.class.getName());

        final AuditEvent initialEventAuditEvent = trail.eventAt(1);
        assertThat(initialEventAuditEvent.getType()).
                isEqualTo(AXON_EVENT_AUDIT_TYPE);
        final Map<String, Object> initialEventData = initialEventAuditEvent
                .getData();
        assertThat(initialEventData.get(MESSAGE_TYPE)).
                isEqualTo(InitialEvent.class.getName());

        final AuditEvent terminalCommandAuditEvent = trail.eventAt(0);
        assertThat(terminalCommandAuditEvent.getType()).
                isEqualTo(AXON_COMMAND_AUDIT_TYPE);
        final Map<String, Object> terminalCommandData
                = terminalCommandAuditEvent.getData();
        assertThat(terminalCommandData.get(MESSAGE_TYPE)).
                isEqualTo(TerminalCommand.class.getName());
    }

    @Test
    public void shouldPassMetaDataCommandToEventToCommand() {
        commands.send(new GenericCommandMessage<>(new InitialCommand("abc"),
                singletonMap("mumble", 3)));

        final Map<String, Object> initialCommandData = trail.eventAt(2)
                .getData();
        assertThat(initialCommandData.get("mumble")).
                isEqualTo(3);

        final Map<String, Object> initialEventData = trail.eventAt(1)
                .getData();
        assertThat(initialEventData.get("mumble")).
                isEqualTo(3);

        final Map<String, Object> terminalCommandData = trail.eventAt(0)
                .getData();
        assertThat(terminalCommandData.get("mumble")).
                isEqualTo(3);
    }

    @Test
    public void shouldReleaseThreadLocalWhenDone() {
        commands.send(new InitialCommand("abc"));

        assertThat(getCorrelationData()).
                isEmpty();
    }
}
