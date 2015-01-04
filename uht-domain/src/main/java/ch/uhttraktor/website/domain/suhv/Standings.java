package ch.uhttraktor.website.domain.suhv;


import ch.uhttraktor.website.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@DynamicUpdate
@XmlRootElement(name = "table")
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "t_suhv_standings")
public class Standings extends BaseEntity {

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer season;

    @Size(max = 32)
    @XmlAttribute(name = "leaguetype")
    @Column(unique = false, nullable = true, length = 32)
    private String leagueType;

    @NotNull
    @XmlAttribute(name = "leaguecode")
    @Column(unique = false, nullable = false)
    private Integer leagueCode;

    @Size(max = 128)
    @XmlAttribute(name = "leaguetext")
    @Column(unique = false, nullable = true, length = 128)
    private String leagueText;

    @Size(max = 32)
    @XmlAttribute(name = "competitiontype")
    @Column(unique = false, nullable = true, length = 32)
    private String competitionType;

    @NotNull
    @Size(max = 32)
    @XmlAttribute(name = "group")
    @Column(name = "c_group", unique = false, nullable = false, length = 32)
    private String group;

    @Size(max = 32)
    @XmlAttribute(name = "grouptext")
    @Column(unique = false, nullable = true, length = 32)
    private String groupText;

    @NotNull
    @XmlAttribute(name = "bar1")
    @Column(unique = false, nullable = false)
    private Integer bar1;

    @NotNull
    @XmlAttribute(name = "bar2")
    @Column(unique = false, nullable = false)
    private Integer bar2;

    @NotNull
    @XmlAttribute(name = "bar3")
    @Column(unique = false, nullable = false)
    private Integer bar3;

    @XmlElement(name = "entry")
    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "standings", orphanRemoval = true)
    private Set<StandingsEntry> entries = new HashSet<>();

    @NotNull
    @OneToOne(mappedBy = "standings", optional = false)
    private SuhvTeam team;

}
