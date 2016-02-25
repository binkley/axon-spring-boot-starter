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

import hm.binkley.spring.axon.CommandAuditEvent;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MonitoringTestConfiguration.class)
public final class MonitoringIT {
    @Autowired
    private CommandGateway commands;
    @Autowired
    private MonitoringTestConfiguration configuration;
    private List<CommandAuditEvent> commandTrail;

    @Before
    public void rememberTrail() {
        commandTrail = configuration.commandTrail;
    }

    @After
    public void clearTrail() {
        commandTrail.clear();
    }

    @Test
    public void shouldAuditSuccessfulCommands() {
        final SuccessfulCommand payload = new SuccessfulCommand();
        commands.send(payload);

        assertThat(commandTrail).hasSize(1);
        final CommandAuditEvent event = commandTrail.get(0);
        assertThat(event.getCommand().getPayload()).
                isSameAs(payload);
        assertThat(event.getReturnValue()).
                isEqualTo(3);
    }

    @Test
    public void shouldAuditFailedCommands() {
        final FailedCommand payload = new FailedCommand();
        commands.send(payload);

        assertThat(commandTrail).hasSize(1);
        final CommandAuditEvent event = commandTrail.get(0);
        assertThat(event.getCommand().getPayload()).
                isSameAs(payload);
        assertThat(event.getFailureCause()).
                isInstanceOf(FailedException.class);
    }
}
