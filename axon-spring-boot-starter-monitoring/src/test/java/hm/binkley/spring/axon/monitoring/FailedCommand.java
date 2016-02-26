package hm.binkley.spring.axon.monitoring;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FailedCommand {
    final FailedException cause;
}
