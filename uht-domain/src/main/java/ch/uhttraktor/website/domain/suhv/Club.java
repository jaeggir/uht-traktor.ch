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

    @OneToMany(mappedBy = "club", orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private List<SuhvTeam> teams;

}
