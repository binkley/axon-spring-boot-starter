package hm.binkley.spring.axon.monitoring;

import org.axonframework.commandhandling.annotation.CommandHandler;

public class FailedCommandHandler {
    @CommandHandler
    public Object handle(final FailedCommand command) {
        throw command.cause;
    }
}
