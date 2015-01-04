package ch.uhttraktor.website.domain.suhv;


import ch.uhttraktor.website.domain.BaseEntity;
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
@Table(name = "t_suhv_standings_entry")
public class StandingsEntry extends BaseEntity {

    @NotNull
    @ManyToOne(optional = false)
    private Standings standings;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer place;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer clubid;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer teamid;

    @NotNull
    @Size(max = 128)
    @Column(unique = false, nullable = false, length = 128)
    private String teamname;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer games;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer wins;

    // wins-overtime
    @NotNull
    @Column(unique = false, nullable = false)
    private Integer winsOvertime;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer ties;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer defeats;

    // defeats-overtime
    @NotNull
    @Column(unique = false, nullable = false)
    private Integer defeatsOvertime;

    // goals-scored
    @NotNull
    @Column(unique = false, nullable = false)
    private Integer goalsScored;

    // goals-received
    @NotNull
    @Column(unique = false, nullable = false)
    private Integer goalsReceived;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer points;

}
