package ch.uhttraktor.website.domain.suhv;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@XmlAccessorType(XmlAccessType.FIELD)
public class Position {

    @XmlAttribute(name  = "lat")
    @Column(unique = false, nullable = false)
    private Double lat;

    @XmlAttribute(name  = "lng")
    @Column(unique = false, nullable = false)
    private Double lng;

}