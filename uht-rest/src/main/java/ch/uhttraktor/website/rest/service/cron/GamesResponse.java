package ch.uhttraktor.website.rest.service.cron;

import ch.uhttraktor.website.domain.suhv.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement(name = "games")
@XmlAccessorType(XmlAccessType.FIELD)
public class GamesResponse {

    @XmlElement(name = "game")
    private List<Game> games = new LinkedList<>();

}