package hm.binkley.spring.axon.metadata;

import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventsourcing.annotation
        .AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;
import org.kohsuke.MetaInfServices;

@MetaInfServices
@NoArgsConstructor
public final class FailedAggregateRoot
        extends AbstractAnnotatedAggregateRoot<String> {
    @AggregateIdentifier
    private String id;

    @CommandHandler
    public FailedAggregateRoot(final FailedCommand command) {
        id = command.getId();
        apply(new FailedEvent(command.getId(), command.getCause()));
    }
}
