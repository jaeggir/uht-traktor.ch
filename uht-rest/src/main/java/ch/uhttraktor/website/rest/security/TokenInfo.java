package ch.uhttraktor.website.rest.security;

import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class TokenInfo {

    private long modificationDate;

    private final String token;

    private final UserDetails userDetails;

    public TokenInfo(String token, UserDetails userDetails) {
        this.modificationDate = System.currentTimeMillis();
        this.token = token;
        this.userDetails = userDetails;
    }

    public void updateModificationDate() {
        this.modificationDate = System.currentTimeMillis();
    }

}
