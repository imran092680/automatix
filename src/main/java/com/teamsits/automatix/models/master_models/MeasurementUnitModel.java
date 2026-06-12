package com.teamsits.automatix.models.master_models;

import com.teamsits.automatix.entities.master_entity.MeasurementUnit;
import com.teamsits.automatix.models.common.CommonModel;
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
