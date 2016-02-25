package hm.binkley.spring.axon.monitoring;

import org.axonframework.commandhandling.annotation.CommandHandler;

public class SuccessfulCommandHandler {
    @CommandHandler
    public Object handle(final SuccessfulCommand command) {
        return 3;
    }
}
