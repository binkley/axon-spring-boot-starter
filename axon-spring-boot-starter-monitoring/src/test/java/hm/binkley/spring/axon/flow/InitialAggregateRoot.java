package hm.binkley.spring.axon.flow;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@NoArgsConstructor
public final class InitialAggregateRoot
        extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id;

    @CommandHandler
    public InitialAggregateRoot(final InitialCommand command) {
        id = command.getId();
        apply(new InitialEvent(command.getId()));
    }
}
