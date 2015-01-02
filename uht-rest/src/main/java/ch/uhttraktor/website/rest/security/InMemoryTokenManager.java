package ch.uhttraktor.website.rest.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTokenManager {

    private Map<String, UserDetails> validUsers = new HashMap<>();

    /**
     * This maps system users to tokens because equals/hashCode is delegated to User entity.
     * This can store either one token or list of them for each user, depending on what you want to do.
     * Here we store single token, which means, that any older tokens are invalidated.
     */
    private Map<UserDetails, TokenInfo> tokens = new HashMap<>();

    public TokenInfo createNewToken(UserDetails userDetails) {
        String token;
        do {
            token = generateToken();
        } while (validUsers.containsKey(token));

        TokenInfo tokenInfo = new TokenInfo(token, userDetails);
        removeUserDetails(userDetails);
        validUsers.put(token, userDetails);
        tokens.put(userDetails, tokenInfo);

        return tokenInfo;
    }

    private String generateToken() {
        byte[] tokenBytes = new byte[32];
        new SecureRandom().nextBytes(tokenBytes);
        return new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);
    }

    private void removeUserDetails(UserDetails userDetails) {
        TokenInfo token = tokens.remove(userDetails);
        if (token != null) {
            validUsers.remove(token.getToken());
        }
    }

    public String removeToken(String token) {
        UserDetails userDetails = validUsers.remove(token);
        if (userDetails != null) {
            tokens.remove(userDetails);
            return userDetails.getUsername();
        }
        return null;
    }

    public UserDetails getUserDetails(String token) {
        return validUsers.get(token);
    }
}
