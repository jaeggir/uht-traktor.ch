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
@Table(name = "t_suhv_team", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id", "season"})
})
public class SuhvTeam extends BaseEntity {

    @NotNull
    @Column(unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer season;

    @Column(unique = false, nullable = true)
    private Integer leaguecode;

    @NotNull
    @Size(max = 128)
    @Column(unique = false, nullable = false, length = 128)
    private String teamname;

    @Size(max = 4)
    @Column(unique = false, nullable = true, length = 4)
    private String teamromancounter;

    @NotNull
    @Size(max = 32)
    @Column(name = "c_group", unique = false, nullable = false, length = 32)
    private String group;

    @NotNull
    @Size(max = 32)
    @Column(unique = false, nullable = false, length = 32)
    private String grouptext;

    // e.g. Herren Aktive KF 4. Liga
    @Size(max = 128)
    @Column(unique = false, nullable = true, length = 128)
    private String description;

    @NotNull
    @ManyToOne(optional = false)
    private Club club;

    @OneToMany(mappedBy = "team", orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private List<Game> games;

    @NotNull
    @OneToOne(optional = false, orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private Standings standings;

}
