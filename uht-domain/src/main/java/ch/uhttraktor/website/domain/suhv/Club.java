package ch.uhttraktor.website.domain.suhv;

import ch.uhttraktor.website.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;


@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_suhv_club")
public class Club extends BaseEntity {

    @NotNull
    @Column(unique = true, nullable = false)
    private Integer id;

    @NotNull
    @Size(max = 128)
    @Column(unique = true, nullable = false, length = 128)
    private String name;

    @NotNull
    @Column(unique = false, nullable = false)
    private Integer firstSuhvSeason;

    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "club", fetch = LAZY, orphanRemoval = true)
    private Set<SuhvTeam> teams = new HashSet<>();

}
