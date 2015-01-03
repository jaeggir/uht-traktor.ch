package ch.uhttraktor.website.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "t_document")
public class Document extends BaseEntity {

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String fileName;

    @NotNull
    @Size(max = 255)
    @Column(unique = false, nullable = false, length = 255)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(unique = false, nullable = false)
    private Category category;

    @NotNull
    @Basic(fetch = FetchType.LAZY)
    @Column(unique = false, nullable = false)
    private byte[] content;

    @NotNull
    @Column(unique = false, nullable = false)
    private long size;

    enum Category {

        CLUB_GENERAL,
        CLUB_WORK_PLAN,
        CLUB_VARIOUS,
        CLUB_FORMS,

        PRESS,

        TRAKTOR_NEWS,
        TRAKTOR_XMAS_NEWS

    }
}
