package com.teamsits.pbs.repository.master_data_repository;

import com.teamsits.pbs.entities.master_entity.MeasurementUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MeasurementUnitRepo extends JpaRepository<MeasurementUnit, Long> {
    @Query("SELECT mu FROM MeasurementUnit mu WHERE mu.isDeleted = com.teamsits.pbs.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    List<MeasurementUnit> findMeasurementUnitsWhereIsDeletedEqualsZero();

    @Query("SELECT mu FROM MeasurementUnit mu WHERE mu.id = :measurementUnitId AND mu.isDeleted = com.teamsits.pbs.utils.ApplicationConstant.DOMAIN_STATUS_ZERO")
    Optional<MeasurementUnit> findMeasurementUnitByIdWhereIsDeletedEqualsZero(@Param("measurementUnitId") Long id);
}
