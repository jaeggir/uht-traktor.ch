package ch.uhttraktor.website.rest.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class TokenInfo {

    private final long created;

    private final String token;

    private final UserDetails userDetails;

    public TokenInfo(String token, UserDetails userDetails) {
        this.created = System.currentTimeMillis();
        this.token = token;
        this.userDetails = userDetails;
    }

}
