package hm.binkley.spring.axon.interceptors;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandHandlerInterceptor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.InterceptorChain;
import org.axonframework.unitofwork.UnitOfWork;

import java.util.List;

@RequiredArgsConstructor
class TestCommandHandlerInterceptor
        implements CommandHandlerInterceptor {
    private final int order;
    private final List<Integer> handlings;

    @Override
    public Object handle(final CommandMessage<?> commandMessage,
            final UnitOfWork unitOfWork,
            final InterceptorChain interceptorChain)
            throws Throwable {
        handlings.add(order);
        return interceptorChain.proceed();
    }
}
