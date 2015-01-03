package ch.uhttraktor.website.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_team")
public class Team extends BaseEntity {

    @NotNull
    @Size(max = 255)
    @Column(unique = true, nullable = false, length = 255)
    private String name;

    @NotNull
    @Size(max = 8)
    @Column(unique = true, nullable = false, length = 8)
    private String shortName;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean enabled;

    @OneToMany(mappedBy = "team", orphanRemoval = true, cascade = {CascadeType.REMOVE})
    private List<TeamPlayer> players = new LinkedList<>();

}