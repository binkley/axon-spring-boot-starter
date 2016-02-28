package hm.binkley.spring.axon.flow;

import org.axonframework.commandhandling.annotation.CommandHandler;

public class TerminalCommandHandler {
    @CommandHandler
    public void handle(final TerminalCommand command) {}
}
