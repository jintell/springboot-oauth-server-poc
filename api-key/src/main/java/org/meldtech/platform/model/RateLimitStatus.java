package org.meldtech.platform.model;

public record RateLimitStatus(long remainingTokens,
                              long capacity,
                              long nanosToRefill) {
}
