package ch.uhttraktor.website.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "T_USER", indexes = {
        @Index(columnList = "login")
})
public class User extends BaseEntity {

    @NotNull
    @Size(max = 255)
    @Column(unique = true, nullable = false, length = 255)
    private String login;

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String lastName;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean enabled;

    @NotNull
    @Email
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String email;

    @Column(unique = false, nullable = true)
    private Instant lastLoginDate;

    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Authority.class)
    @CollectionTable(name = "T_USER_AUTHORITY", joinColumns = @JoinColumn(name = "user_uuid"))
    private List<Authority> authorities = new LinkedList<>();

    @Override
    public String toString() {
        return "User{uuid='" + uuid + "', login='" + login + "', email='" + email + "'}";
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }
}