package start.security.model.response;

import lombok.Builder;

@Builder
public record MainResponse (
    String username,
    String role
){ }
