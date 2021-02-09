package uz.bdm.HrTesting.audit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Base abstract class for entities which will hold definitions for created, last modified by and created,
 * last modified by date.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AuditEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "G1", strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created")
    @Temporal(value = TemporalType.TIMESTAMP)
    protected Date created;

    @Column(name = "changed")
    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonIgnore
    protected Date changed;

    @PrePersist
    @PreUpdate
    void pre() {
        if (this.created == null) {
            this.created = new Date();
        }
        this.changed = new Date();
    }
}
