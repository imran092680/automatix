package com.teamsits.automatix.service.master_data_service;

import com.teamsits.automatix.entities.master_entity.MeasurementUnit;
import com.teamsits.automatix.models.master_models.MeasurementUnitModel;
import com.teamsits.automatix.repository.master_data_repository.MeasurementUnitRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeasurementUnitService {
    private final MeasurementUnitRepo measurementUnitRepo;

    public List<MeasurementUnitModel> getMeasurementUnits() {
        return measurementUnitRepo.findMeasurementUnitsWhereIsDeletedEqualsZero()
                .stream()
                .map(MeasurementUnitModel::new)
                .collect(Collectors.toList());
    }

    public Optional<MeasurementUnitModel> getMeasurementUnitById(Long id) {
        return measurementUnitRepo.findMeasurementUnitByIdWhereIsDeletedEqualsZero(id).map(MeasurementUnitModel::new);
    }

    public Optional<MeasurementUnitModel> addMeasurementUnit(MeasurementUnitModel measurementUnitModel) {
        String unit = measurementUnitModel.getUnit().trim().toLowerCase();

        // Check if a measurement unit with the same name (ignoring case) already exists
        boolean exists = measurementUnitRepo.findMeasurementUnitsWhereIsDeletedEqualsZero().stream()
                .anyMatch(mu -> mu.getUnit().trim().equalsIgnoreCase(unit));

        if (exists) {
            throw new RuntimeException("This Measurement Unit already exists.");
        }

        return Optional.of(new MeasurementUnitModel(measurementUnitRepo.save(new MeasurementUnit(measurementUnitModel))));
    }

    // update later, make more robust
    public void deleteMeasurementUnit(Long id) {
        if (measurementUnitRepo.existsById(id)) {
            MeasurementUnit measurementUnit = measurementUnitRepo
                    .findMeasurementUnitByIdWhereIsDeletedEqualsZero(id)
                    .orElseThrow(() -> new RuntimeException("MeasurementUnit not found."));

            measurementUnit.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            measurementUnitRepo.save(measurementUnit);
        }
    }

    public Optional<MeasurementUnitModel> updateMeasurementUnit(MeasurementUnitModel measurementUnitModel) {
        MeasurementUnit measurementUnit = measurementUnitRepo.findMeasurementUnitByIdWhereIsDeletedEqualsZero(measurementUnitModel.getId())
                .orElseThrow(() -> new RuntimeException("This MeasurementUnit does not exist"));

        measurementUnit.setUnit(measurementUnitModel.getUnit());

        return Optional.of(new MeasurementUnitModel(measurementUnitRepo.save(measurementUnit)));
    }
}