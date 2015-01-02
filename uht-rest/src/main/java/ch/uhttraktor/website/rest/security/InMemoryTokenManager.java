package ch.uhttraktor.website.rest.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTokenManager {

    private final Map<String, UserDetails> validTokens;

    private final SecureRandom secureRandom;

    public InMemoryTokenManager() {
        this.validTokens = new HashMap<>();
        try {
            this.secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            // config error - see https://github.com/jaeggir/uht-traktor.ch/wiki#jdk
            throw new RuntimeException(e);
        }
    }

    public TokenInfo createNewToken(UserDetails userDetails) {
        String token;
        do {
            token = generateToken();
        } while (validTokens.containsKey(token));

        TokenInfo tokenInfo = new TokenInfo(token, userDetails);
        validTokens.put(token, userDetails);

        return tokenInfo;
    }

    private String generateToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);
    }

    public String removeToken(String token) {
        UserDetails userDetails = validTokens.remove(token);
        if (userDetails != null) {
            return userDetails.getUsername();
        }
        return null;
    }

    public UserDetails getUserDetails(String token) {
        return validTokens.get(token);
    }
}
