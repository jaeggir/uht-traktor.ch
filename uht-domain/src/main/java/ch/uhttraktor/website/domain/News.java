package ch.uhttraktor.website.domain;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "T_NEWS")
public class News extends BaseEntity {

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String title;

    @Lob
    @NotNull
    @Column(unique = false, nullable = false)
    private String content;

    @NotNull
    @Column(unique = false, nullable = false)
    private Boolean visible;

    @Column(unique = false, nullable = true)
    private Instant publishDate;

    @NotNull
    @ManyToOne(optional = false)
    private User user;

}