package com.teamsits.automatix.service.master_data_service;

import com.teamsits.automatix.entities.master_entity.MeasurementUnit;
import com.teamsits.automatix.models.master_models.MeasurementUnitModel;
import com.teamsits.automatix.repository.master_data_repository.MeasurementUnitRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeasurementUnitService {
    private final MeasurementUnitRepo measurementUnitRepo;
    private final SecurityUtils securityUtils;

    public List<MeasurementUnitModel> getMeasurementUnits() {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return measurementUnitRepo.findMeasurementUnitsWhereIsDeletedEqualsZero(orgId)
                .stream()
                .map(MeasurementUnitModel::new)
                .collect(Collectors.toList());
    }

    public Optional<MeasurementUnitModel> getMeasurementUnitById(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return measurementUnitRepo.findMeasurementUnitByIdWhereIsDeletedEqualsZero(orgId, id).map(MeasurementUnitModel::new);
    }

    public Optional<MeasurementUnitModel> addMeasurementUnit(MeasurementUnitModel measurementUnitModel) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        String unit = measurementUnitModel.getUnit().trim().toLowerCase();

        boolean exists = measurementUnitRepo.findMeasurementUnitsWhereIsDeletedEqualsZero(orgId).stream()
                .anyMatch(mu -> mu.getUnit().trim().equalsIgnoreCase(unit));

        if (exists) {
            throw new RuntimeException("This Measurement Unit already exists.");
        }

        MeasurementUnit mu = new MeasurementUnit(measurementUnitModel);
        mu.setOrganization(securityUtils.getCurrentOrganization());
        mu.setCreatedBy(securityUtils.getCurrentUserId());
        mu.setUpdatedBy(securityUtils.getCurrentUserId());
        return Optional.of(new MeasurementUnitModel(measurementUnitRepo.save(mu)));
    }

    public void deleteMeasurementUnit(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        if (measurementUnitRepo.existsById(id)) {
            MeasurementUnit measurementUnit = measurementUnitRepo
                    .findMeasurementUnitByIdWhereIsDeletedEqualsZero(orgId, id)
                    .orElseThrow(() -> new RuntimeException("MeasurementUnit not found."));

            measurementUnit.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            measurementUnitRepo.save(measurementUnit);
        }
    }

    public Optional<MeasurementUnitModel> updateMeasurementUnit(MeasurementUnitModel measurementUnitModel) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        MeasurementUnit measurementUnit = measurementUnitRepo
                .findMeasurementUnitByIdWhereIsDeletedEqualsZero(orgId, measurementUnitModel.getId())
                .orElseThrow(() -> new RuntimeException("This MeasurementUnit does not exist"));

        measurementUnit.setUnit(measurementUnitModel.getUnit());
        measurementUnit.setUpdatedBy(securityUtils.getCurrentUserId());

        return Optional.of(new MeasurementUnitModel(measurementUnitRepo.save(measurementUnit)));
    }
}
