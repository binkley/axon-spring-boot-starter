package hm.binkley.spring.axon.handlers;

import org.junit.Test;

import static org.axonframework.test.Fixtures.newGivenWhenThenFixture;

public class HandlersTest {
    @Test
    public void shouldPublishEvents() {
        newGivenWhenThenFixture(HandlersTestAggregateRoot.class).
                given().
                when(new TestCommand("abc")).
                expectEvents(new TestEvent("abc"));
    }
}
