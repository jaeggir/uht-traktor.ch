package ch.uhttraktor.website.domain.suhv;


import ch.uhttraktor.website.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_suhv_standings")
public class Standings extends BaseEntity {

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer season;

    @Size(max = 32)
    @Column(unique = false, nullable = true, length = 32)
    private String leaguetype;

    @NotNull
    @Size(max = 128)
    @Column(unique = false, nullable = false, length = 128)
    private Integer leaguecode;

    @Size(max = 128)
    @Column(unique = false, nullable = true, length = 128)
    private String leaguetext;

    @Size(max = 32)
    @Column(unique = false, nullable = true, length = 32)
    private String competitiontype;

    @NotNull
    @Size(max = 32)
    @Column(name = "c_group", unique = false, nullable = false, length = 32)
    private String group;

    @Size(max = 32)
    @Column(unique = false, nullable = true, length = 32)
    private String grouptext;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer bar1;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer bar2;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer bar3;

    @OneToMany(mappedBy = "standings", orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private List<StandingsEntry> entries;

    @NotNull
    @OneToOne(optional = false)
    private SuhvTeam team;

}
