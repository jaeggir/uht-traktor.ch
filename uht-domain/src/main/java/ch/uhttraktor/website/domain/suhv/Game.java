package ch.uhttraktor.website.domain.suhv;

import ch.uhttraktor.website.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.time.Instant;

@Getter
@Setter
@Entity
@DynamicUpdate
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "t_suhv_game")
public class Game extends BaseEntity {

    @NotNull
    @XmlAttribute(name = "id")
    @Column(unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Size(max = 32)
    @XmlAttribute(name = "leaguetype")
    @Column(unique = false, nullable = false, length = 32)
    private String leagueType;

    @NotNull
    @Size(max = 32)
    @XmlAttribute(name = "leaguecode")
    @Column(unique = false, nullable = false, length = 32)
    private String leagueCode;

    @Size(max = 64)
    @XmlAttribute(name = "leaguetext")
    @Column(unique = false, nullable = true, length = 64)
    private String leagueText;

    @Size(max = 64)
    @XmlAttribute(name = "fieldsize")
    @Column(unique = false, nullable = true, length = 64)
    private String fieldSize;

    @NotNull
    @Size(max = 32)
    @XmlAttribute(name = "competitiontype")
    @Column(unique = false, nullable = false, length = 32)
    private String competitionType;

    @XmlAttribute(name = "tableau")
    @Column(unique = false, nullable = true)
    private Integer tableau;

    @Size(max = 64)
    @XmlAttribute(name = "tableautext")
    @Column(unique = false, nullable = true, length = 64)
    private Integer tableauText;

    @Size(max = 64)
    @XmlAttribute(name = "gametext")
    @Column(unique = false, nullable = true, length = 64)
    private Integer gameText;

    @NotNull
    @XmlAttribute(name = "round")
    @Column(name = "c_round", unique = false, nullable = false)
    private Integer round;

    @Size(max = 64)
    @XmlAttribute(name = "roundtext")
    @Column(unique = false, nullable = true, length = 64)
    private String roundText;

    // TODO not yet implemented
    @Column(name = "c_date", unique = false, nullable = true)
    private Instant date;

    @NotNull
    @XmlAttribute(name = "hometeamid")
    @Column(unique = false, nullable = false)
    private Integer homeTeamId;

    @NotNull
    @Size(max = 128)
    @XmlAttribute(name = "hometeamname")
    @Column(unique = false, nullable = false, length = 128)
    private String homeTeamName;

    @NotNull
    @XmlAttribute(name = "awayteamid")
    @Column(unique = false, nullable = false)
    private Integer awayTeamId;

    @NotNull
    @Size(max = 128)
    @XmlAttribute(name = "awayteamname")
    @Column(unique = false, nullable = false, length = 128)
    private String awayTeamName;

    @NotNull
    @XmlAttribute(name = "played")
    @Column(unique = false, nullable = false)
    private Boolean played;

    @NotNull
    @XmlAttribute(name = "goalshome")
    @Column(unique = false, nullable = false)
    private Integer goalsHome;

    @NotNull
    @XmlAttribute(name = "goalsaway")
    @Column(unique = false, nullable = false)
    private Integer goalsAway;

    @NotNull
    @XmlAttribute(name = "overtime")
    @Column(unique = false, nullable = false)
    private Boolean overtime;

    @NotNull
    @XmlAttribute(name = "penaltyshooting")
    @Column(unique = false, nullable = false)
    private Boolean penaltyShooting;

    @NotNull
    @XmlAttribute(name = "forfait")
    @Column(unique = false, nullable = false)
    private Boolean forfait;

    @NotNull
    @XmlAttribute(name = "canceled")
    @Column(unique = false, nullable = false)
    private Boolean canceled;

    @Size(max = 128)
    @XmlAttribute(name = "organizer")
    @Column(unique = false, nullable = true, length = 128)
    private String organizer;

    @XmlAttribute(name = "organizerid")
    @Column(unique = false, nullable = true)
    private Integer organizerId;

    // e.g. Einzelspiel, Turnierform
    @NotNull
    @Size(max = 32)
    @XmlAttribute(name = "eventtype")
    @Column(unique = false, nullable = false, length = 32)
    private String eventType;

    // for new games (not yet assigned to an organiser) or old games, we might not have a gym
    @XmlElement(name = "gym")
    @ManyToOne(optional = true)
    private Gym gym;

    @NotNull
    @ManyToOne(optional = false)
    private SuhvTeam team;

}
