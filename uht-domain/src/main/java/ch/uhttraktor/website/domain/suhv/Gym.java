package ch.uhttraktor.website.domain.suhv;

import ch.uhttraktor.website.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_suhv_gym")
public class Gym extends BaseEntity {

    @NotNull
    @Column(unique = true, nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(unique = false, nullable = true, length = 255)
    private String name;

    @Size(max = 255)
    @Column(unique = false, nullable = true, length = 255)
    private String street;

    @Size(min = 2, max = 2)
    @Column(unique = false, nullable = true, length = 2)
    private String country;

    @Column(unique = false, nullable = true)
    private Integer zip;

    @Size(max = 255)
    @Column(unique = false, nullable = true, length = 255)
    private String city;

    @NotNull
    @Column(unique = false, nullable = false)
    private Double lat;

    @NotNull
    @Column(unique = false, nullable = false)
    private Double lng;

}
