package ch.uhttraktor.website.domain;


import com.fasterxml.jackson.annotation.JsonValue;

/**
 * An authority (a security role) used by Spring Security.
 */
public enum Authority {

    ANONYMOUS(SecurityRole.ANONYMOUS),
    ADMIN(SecurityRole.ADMIN),
    USER(SecurityRole.USER),
    NEWS(SecurityRole.NEWS);

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
