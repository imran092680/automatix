package com.teamsits.pbs.entities.common;

import com.teamsits.pbs.utils.ApplicationConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
//@EntityListeners(value = {AuditEntityListener.class})
public abstract class CommonColumn {
    @Version
    @Column(nullable = false)
    private Integer version;

    @Column(columnDefinition = "INTEGER DEFAULT 0 NOT NULL")
    private Integer isDeleted = ApplicationConstant.DOMAIN_STATUS_ZERO;

    @Column(nullable = false)
    private Long createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createdAt;

    @Column(nullable = false)
    private Long updatedBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar updatedAt;

    public CommonColumn(Long createdBy, Long updatedBy) {
        this.setCreatedBy(createdBy);
        this.setUpdatedBy(updatedBy);
    }
}
