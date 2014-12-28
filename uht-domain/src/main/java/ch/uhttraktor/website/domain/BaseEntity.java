package ch.uhttraktor.website.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

	@Id
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "uuid", unique = true, nullable = false)
	protected UUID uuid;

    @Generated(GenerationTime.INSERT)
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp")
    private Instant dateCreated;

    @Column(insertable = false)
    @Generated(GenerationTime.INSERT)
    private Instant lastModified;

    @Override
    public int hashCode() {
        if (uuid == null) {
            return super.hashCode();
        } else {
            return uuid.hashCode();
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof BaseEntity)) {
            return false;
        } else if (uuid == null) {
            return super.equals(obj);
        } else {
            return uuid.equals(((BaseEntity) obj).uuid);
        }
    }

}
