package ch.uhttraktor.website.domain.suhv;

import ch.uhttraktor.website.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.*;

@Getter
@Setter
@Entity
@DynamicUpdate
@XmlRootElement(name = "gym")
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "t_suhv_gym")
public class Gym extends BaseEntity {

    @NotNull
    @XmlAttribute(name = "id")
    @Column(unique = true, nullable = false)
    private Integer id;

    @Size(max = 255)
    @XmlElement(name = "name")
    @Column(unique = false, nullable = true, length = 255)
    private String name;

    @Size(max = 255)
    @XmlElement(name = "street")
    @Column(unique = false, nullable = true, length = 255)
    private String street;

    @Size(max = 2)
    @XmlElement(name = "country")
    @Column(unique = false, nullable = true, length = 2)
    private String country;

    @XmlElement(name = "zip")
    @Column(unique = false, nullable = true)
    private Integer zip;

    @Size(max = 255)
    @XmlElement(name = "city")
    @Column(unique = false, nullable = true, length = 255)
    private String city;

    @NotNull
    @Embedded
    @XmlElement(name = "position")
    @Column(unique = false, nullable = false)
    private Position position;

}
