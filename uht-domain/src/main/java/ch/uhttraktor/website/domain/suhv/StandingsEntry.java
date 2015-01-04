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

@Getter
@Setter
@Entity
@DynamicUpdate
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "t_suhv_standings_entry")
public class StandingsEntry extends BaseEntity {

    @NotNull
    @XmlAttribute(name = "place")
    @Column(unique = false, nullable = false)
    private Integer place;

    @NotNull
    @XmlAttribute(name = "clubid")
    @Column(unique = false, nullable = false)
    private Integer clubId;

    @NotNull
    @XmlAttribute(name = "teamid")
    @Column(unique = false, nullable = false)
    private Integer teamId;

    @NotNull
    @Size(max = 128)
    @XmlAttribute(name = "teamname")
    @Column(unique = false, nullable = false, length = 128)
    private String teamName;

    @NotNull
    @XmlAttribute(name = "games")
    @Column(unique = false, nullable = false)
    private Integer games;

    @NotNull
    @XmlAttribute(name = "wins")
    @Column(unique = false, nullable = false)
    private Integer wins;

    @NotNull
    @XmlAttribute(name = "wins-overtime")
    @Column(unique = false, nullable = false)
    private Integer winsOvertime;

    @NotNull
    @XmlAttribute(name = "ties")
    @Column(unique = false, nullable = false)
    private Integer ties;

    @NotNull
    @XmlAttribute(name = "defeats")
    @Column(unique = false, nullable = false)
    private Integer defeats;

    @NotNull
    @XmlAttribute(name = "defeats-overtime")
    @Column(unique = false, nullable = false)
    private Integer defeatsOvertime;

    @NotNull
    @XmlAttribute(name = "goals-scored")
    @Column(unique = false, nullable = false)
    private Integer goalsScored;

    @NotNull
    @XmlAttribute(name = "goals-received")
    @Column(unique = false, nullable = false)
    private Integer goalsReceived;

    @NotNull
    @XmlAttribute(name = "points")
    @Column(unique = false, nullable = false)
    private Integer points;

    @NotNull
    @ManyToOne(optional = false)
    private Standings standings;

}
