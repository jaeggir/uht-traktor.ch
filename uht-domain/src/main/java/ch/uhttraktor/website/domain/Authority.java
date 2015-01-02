package ch.uhttraktor.website.domain;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * An authority (a security role) used by Spring Security.
 */
public enum Authority {

    // Enum constant and name should be the same, otherwise Jackson cannot map between
    // the String representation and the enum value. Example:
    // Authority.ROLE_ANONYMOUS.name == "ROLE_ANONYMOUS"

    ROLE_ANONYMOUS(SecurityRole.ANONYMOUS),
    ROLE_ADMIN(SecurityRole.ADMIN),
    ROLE_USER(SecurityRole.USER),
    ROLE_NEWS(SecurityRole.NEWS);

    private String name;

    private Authority(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Authority{name='" + name + "'}";
    }
}
