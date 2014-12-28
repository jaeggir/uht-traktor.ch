package ch.uhttraktor.website.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * An authority (a security role) used by Spring Security.
 */
@Getter
public enum Authority {

    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private String name;

    private Authority(String name) {
        this.name = name;
    }

    @JsonCreator
    public static Authority newInstance(String name) {
        return Authority.valueOf(name);
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
