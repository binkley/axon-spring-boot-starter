package hm.binkley.spring.axon.audit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FailedEvent {
    final FailedException cause;
}
