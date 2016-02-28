package hm.binkley.spring.axon;

import org.axonframework.commandhandling.CommandDispatchInterceptor;
import org.axonframework.commandhandling.CommandMessage;
import org.springframework.core.annotation.Order;

import static org.axonframework.correlation.CorrelationDataHolder.getCorrelationData;
import static org.axonframework.correlation.CorrelationDataHolder.setCorrelationData;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * {@code CorrelationDataCommandDispatchInterceptor} <strong>needs
 * documentation</strong>.
 *
 * @author <a href="mailto:binkley@alumni.rice.edu">B. K. Oxley (binkley)</a>
 * @todo Needs documentation
 */
@Order(HIGHEST_PRECEDENCE)
public class CorrelationDataCommandDispatchInterceptor
        implements CommandDispatchInterceptor {
    @Override
    public CommandMessage<?> handle(final CommandMessage<?> commandMessage) {
        if (getCorrelationData().isEmpty()) {
            setCorrelationData(commandMessage.getMetaData());
            return commandMessage;
        } else
            return commandMessage.andMetaData(getCorrelationData());
    }
}
