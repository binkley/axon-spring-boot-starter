package hm.binkley.spring.axon.audit;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class FailedCommand {
    final FailedException cause;
}
