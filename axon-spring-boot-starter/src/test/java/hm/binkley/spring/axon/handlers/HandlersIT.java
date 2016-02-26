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

package hm.binkley.spring.axon.handlers;

import hm.binkley.spring.axon.handlers.HandlersTestConfiguration
        .EventCollector;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.domain.DomainEventStream;
import org.axonframework.eventstore.EventStore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HandlersTestConfiguration.class)
public final class HandlersIT {
    @Autowired
    private CommandGateway commands;
    @Autowired
    private EventCollector eventCollector;
    @Autowired
    private EventStore eventStore;

    @Test
    public void shouldWireEventStore() {
        commands.send(new HandlersTestCommand("abc"));
        assertThat(asAggregateIds(eventStore
                .readEvents(HandlersTestAggregateRoot.class.getSimpleName(),
                        "abc"))).
                isEqualTo(singletonList(new HandlersTestEvent("abc")));
    }

    @Test
    public void shouldFireHandlers() {
        commands.send(new HandlersTestCommand("def"));
        assertThat(eventCollector.getEvents()).
                isEqualTo(singletonList(new HandlersTestEvent("def")));
    }

    private static List<HandlersTestEvent> asAggregateIds(
            final DomainEventStream stream) {
        final List<HandlersTestEvent> events = new ArrayList<>();
        while (stream.hasNext())
            events.add((HandlersTestEvent) stream.next().getPayload());
        return events;
    }
}
