package ch.uhttraktor.website.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

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
@Table(name = "t_player")
public class Player extends BaseEntity {

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String firstName;

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String lastName;

    @Column(unique = false, nullable = true)
    private Instant birthDate;

    @OneToMany(mappedBy = "player", orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private List<TeamPlayer> teams = new LinkedList<>();

}