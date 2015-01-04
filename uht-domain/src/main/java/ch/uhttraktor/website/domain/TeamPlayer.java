package ch.uhttraktor.website.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_team_player", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"team_uuid", "player_uuid"})
})
public class TeamPlayer extends BaseEntity {

    @NotNull
    @ManyToOne(optional = false)
    private Team team;

    @NotNull
    @ManyToOne(optional = false)
    private Player player;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false)
    private Role role;

    @Size(max = 4)
    @Column(unique = false, nullable = true, length = 4)
    private String shirtNumber;

    enum Role {
        GOALKEEPER,
        DEFENSE,
        OFFENSE,
        COACH,
        NONE
    }

}