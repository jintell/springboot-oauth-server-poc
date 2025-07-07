package org.meldtech.platform.model;

import lombok.Builder;

@Builder
public record ApiClient(String clientName,
                        String clientApiKey,
                        String clientRole,
                        RateLimitStatus rateLimitStatus) {
}
