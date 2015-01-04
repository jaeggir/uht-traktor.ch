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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "t_suhv_team", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id", "season"})
})
public class SuhvTeam extends BaseEntity {

    @NotNull
    @XmlAttribute(name = "id")
    @Column(unique = false, nullable = false)
    private Integer id;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer season;

    @XmlAttribute(name = "leaguecode")
    @Column(unique = false, nullable = true)
    private Integer leagueCode;

    @NotNull
    @Size(max = 128)
    @XmlAttribute(name = "teamname")
    @Column(unique = false, nullable = false, length = 128)
    private String teamName;

    @Size(max = 4)
    @XmlAttribute(name = "teamromancounter")
    @Column(unique = false, nullable = true, length = 4)
    private String teamRomanCounter;

    @NotNull
    @Size(max = 32)
    @XmlAttribute(name = "group")
    @Column(name = "c_group", unique = false, nullable = false, length = 32)
    private String group;

    @NotNull
    @Size(max = 32)
    @XmlAttribute(name = "grouptext")
    @Column(unique = false, nullable = false, length = 32)
    private String groupText;

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "team", orphanRemoval = true)
    private List<Game> games;

    @NotNull
    @Cascade(CascadeType.ALL)
    @OneToOne(optional = false, orphanRemoval = true)
    private Standings standings;

    @ManyToOne(optional = false)
    private Club club;
}
