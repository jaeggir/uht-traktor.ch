package ch.uhttraktor.website.rest.service.cron;

import ch.uhttraktor.website.domain.suhv.SuhvTeam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "teams")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamResponse {

    @XmlElement(name = "team")
    private Set<SuhvTeam> teams = new HashSet<>();

}