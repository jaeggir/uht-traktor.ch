package ch.uhttraktor.website.rest.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class InMemoryTokenManager {

    private static final long TOKEN_LIFETIME_IN_MS = 30 * 60 * 1000l; // 30 minutes

    private static final long CLEANUP_INTERVAL_IN_MS = 30 * 60 * 1000l; // 30 minutes

    private final Map<String, Tuple> validTokens;

    private final SecureRandom secureRandom;

    private long lastCleanup = System.currentTimeMillis();

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
        validTokens.put(token, new Tuple(tokenInfo, userDetails));

        return tokenInfo;
    }

    private String generateToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return new String(Base64.encode(tokenBytes), StandardCharsets.UTF_8);
    }

    public String removeToken(String token) {
        Tuple tuple = validTokens.remove(token);
        if (tuple != null) {
            return tuple.getUserDetails().getUsername();
        }
        return null;
    }

    private void considerHousekeeping() {
        if (System.currentTimeMillis() - lastCleanup > CLEANUP_INTERVAL_IN_MS) {
            new Thread(() -> {
                Iterator<Map.Entry<String, Tuple>> iterator = validTokens.entrySet().iterator();
                while (iterator.hasNext()) {
                    long created = iterator.next().getValue().getTokenInfo().getModificationDate();
                    if ((System.currentTimeMillis() - created) > TOKEN_LIFETIME_IN_MS) {
                        iterator.remove();
                    }
                }
            }).start();
            lastCleanup = System.currentTimeMillis();
        }
    }

    public UserDetails getUserDetails(String token) {

        // do some housekeeping from time to time (in a separate thread)
        considerHousekeeping();

        Tuple tuple = validTokens.get(token);
        if (tuple != null) {
            // token still valid?
            if ((System.currentTimeMillis() - tuple.getTokenInfo().getModificationDate()) > TOKEN_LIFETIME_IN_MS) {
                validTokens.remove(token);
                return null;
            } else {
                tuple.getTokenInfo().updateModificationDate();
                return tuple.getUserDetails();
            }
        } else {
            return null;
        }
    }

    @Getter
    @AllArgsConstructor
    private class Tuple {
        private TokenInfo tokenInfo;
        private UserDetails userDetails;
    }
}
