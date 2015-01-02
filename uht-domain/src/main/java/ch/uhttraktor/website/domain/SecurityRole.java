package ch.uhttraktor.website.domain;

/**
 * Required as we cannot directly reference enum values in @Secured annotations.
 */
public interface SecurityRole {

    public static final String ANONYMOUS_USER_NAME = "anonymous";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ANONYMOUS_ROLE_SHORT = "ANONYMOUS";

    public static final String USER = "ROLE_USER";
    public static final String ADMIN = "ROLE_ADMIN";

    public static final String NEWS = "ROLE_NEWS";

}
