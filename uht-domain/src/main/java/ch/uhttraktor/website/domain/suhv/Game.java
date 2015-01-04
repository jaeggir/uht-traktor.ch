package ch.uhttraktor.website.domain.suhv;

import ch.uhttraktor.website.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_suhv_game")
public class Game extends BaseEntity {

    @NotNull
    @Column(unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Size(max = 8)
    @Column(unique = false, nullable = false, length = 8)
    private String leaguetype;

    @NotNull
    @Size(max = 8)
    @Column(unique = false, nullable = false, length = 8)
    private String leaguecode;

    @Size(max = 64)
    @Column(unique = false, nullable = true, length = 64)
    private String leaguetext;

    @Size(max = 64)
    @Column(unique = false, nullable = true, length = 64)
    private String fieldsize;

    @NotNull
    @Size(max = 32)
    @Column(unique = false, nullable = false, length = 32)
    private String competitiontype;

    @NotNull
    @Column(name = "c_round", unique = false, nullable = false)
    private Integer round;

    @Column(name = "c_date", unique = false, nullable = true)
    private Instant date;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer hometeamid;

    @NotNull
    @Size(max = 128)
    @Column(unique = false, nullable = false, length = 128)
    private String hometeamname;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer awayteamid;

    @NotNull
    @Size(max = 128)
    @Column(unique = false, nullable = false, length = 128)
    private String awayteamname;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean played;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer goalshome;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer goalsaway;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean overtime;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean penaltyshooting;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean forfait;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean canceled;

    // e.g. Einzelspiel, Turnierform
    @NotNull
    @Size(max = 32)
    @Column(unique = false, nullable = false, length = 32)
    private String eventtype;

    @NotNull
    @ManyToOne(optional = false)
    private Gym gym;

    @NotNull
    @ManyToOne(optional = false)
    private SuhvTeam team;

}
