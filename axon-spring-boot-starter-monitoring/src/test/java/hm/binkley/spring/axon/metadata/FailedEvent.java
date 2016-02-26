package hm.binkley.spring.axon.metadata;

import lombok.Value;

@Value
public class FailedEvent {
    private final String id;
    private final FailedException cause;
}
