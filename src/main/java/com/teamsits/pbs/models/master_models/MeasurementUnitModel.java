package com.teamsits.pbs.models.master_models;

import com.teamsits.pbs.entities.master_entity.MeasurementUnit;
import com.teamsits.pbs.models.common.CommonModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementUnitModel extends CommonModel {
    private String unit;

    public MeasurementUnitModel(MeasurementUnit measurementUnit) {
        super(
                measurementUnit.getId(),
                measurementUnit.getVersion(),
                measurementUnit.getCreatedBy(),
                measurementUnit.getCreatedAt(),
                measurementUnit.getUpdatedBy(),
                measurementUnit.getUpdatedAt()
        );
        this.setUnit(measurementUnit.getUnit());
    }
}
