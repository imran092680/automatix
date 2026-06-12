package com.teamsits.automatix.entities.common;

import com.teamsits.automatix.entities.Organization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class TenantAuditBase extends CommonColumn {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    public TenantAuditBase(Long createdBy, Long updatedBy) {
        super(createdBy, updatedBy);
    }
}
