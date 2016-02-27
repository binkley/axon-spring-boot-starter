package hm.binkley.spring.axon.audit;

import org.axonframework.commandhandling.annotation.CommandHandler;

public class SuccessfulCommandHandler {
    @CommandHandler
    public Object handle(final SuccessfulCommand command) {
        return 3;
    }
}
