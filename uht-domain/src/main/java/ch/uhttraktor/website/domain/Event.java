package ch.uhttraktor.website.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_event")
public class Event extends BaseEntity {

    @NotNull
    @Size(max = 64)
    @Column(unique = false, nullable = false, length = 64)
    private String title;

    @NotNull
    @Size(max = 64)
    @Column(unique = false, nullable = false, length = 64)
    private String location;

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String description;

    @Size(max = 255)
    @Column(unique = false, nullable = true, length = 255)
    private String link;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean highlight;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean suhv;

    @ManyToMany
    private List<Team> teams = new LinkedList<>();

    // Events have a startDate and an endDate (yyy-mm-dd hh:mm). This implementation does not store timezone
    // related information, so startDate and endDate are stored as Instants (UTC). The UI is responsible to
    // display the dates correctly (timezone is "Europe/Zurich").
    //
    // Set allDay to TRUE to ignore time in startDate and endDate. Both values are stored, but the
    // UI should ignore them and only show the date.

    @NotNull
    @Column(unique = false, nullable = false)
    private Instant startDate;

    @NotNull
    @Column(unique = false, nullable = false)
    private Instant endDate;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean allDay;

}