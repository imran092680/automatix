package com.teamsits.automatix.entities.master_entity;

import com.teamsits.automatix.entities.common.TenantAuditBase;
import com.teamsits.automatix.models.master_models.MeasurementUnitModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementUnit extends TenantAuditBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String unit;

    public MeasurementUnit(MeasurementUnitModel measurementUnitModel) {
        super(
                measurementUnitModel.getCreatedBy(),
                measurementUnitModel.getUpdatedBy()
        );
        this.setUnit(measurementUnitModel.getUnit());
    }
}
