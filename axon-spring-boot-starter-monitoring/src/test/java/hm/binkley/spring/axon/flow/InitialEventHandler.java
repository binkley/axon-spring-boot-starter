package hm.binkley.spring.axon.flow;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.annotation.EventHandler;

@RequiredArgsConstructor
public class InitialEventHandler {
    private final CommandGateway commands;

    @EventHandler
    public void handle(final InitialEvent event) {
        commands.send(new TerminalCommand(event.getId()));
    }
}
