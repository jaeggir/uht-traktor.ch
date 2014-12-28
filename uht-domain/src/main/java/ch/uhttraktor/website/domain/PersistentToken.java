package ch.uhttraktor.website.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;


/**
 * Persistent tokens are used by Spring Security to automatically log in users.
 */
@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "T_PERSISTENT_TOKEN")
public class PersistentToken extends BaseEntity {
    
    private static final int MAX_USER_AGENT_LEN = 255;

    @NotNull
    @Size(max = 255)
    @Column(name = "series", unique = true, nullable = false, length = 255)
    private String series;

    @JsonIgnore
    @NotNull
    @Column(name = "token_value", length = 255)
    private String tokenValue;

    @JsonIgnore
    @Column(name = "token_date")
    private Instant tokenDate;

    //an IPV6 address max length is 39 characters
    @Size(max = 39)
    @Column(name = "ip_address", length = 64)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @JsonIgnore
    @ManyToOne
    private User user;

    public void setUserAgent(String userAgent) {
        if (userAgent.length() >= MAX_USER_AGENT_LEN) {
            this.userAgent = userAgent.substring(0, MAX_USER_AGENT_LEN - 1);
        } else {
            this.userAgent = userAgent;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersistentToken that = (PersistentToken) o;
        return series.equals(that.series);
    }

    @Override
    public int hashCode() {
        return series.hashCode();
    }

    @Override
    public String toString() {
        return "PersistentToken{series='" + series + "', tokenValue='" + tokenValue + "', tokenDate='" + tokenDate +
                "', ipAddress='" + ipAddress + "', userAgent='" + userAgent + "'}";
    }
}
