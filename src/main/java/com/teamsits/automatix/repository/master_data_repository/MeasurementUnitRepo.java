package com.teamsits.automatix.repository.master_data_repository;

import com.teamsits.automatix.entities.master_entity.MeasurementUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MeasurementUnitRepo extends JpaRepository<MeasurementUnit, Long> {

    @Query("SELECT mu FROM MeasurementUnit mu WHERE mu.organization.id = :orgId AND mu.isDeleted = 0")
    List<MeasurementUnit> findMeasurementUnitsWhereIsDeletedEqualsZero(@Param("orgId") Long orgId);

    @Query("SELECT mu FROM MeasurementUnit mu WHERE mu.organization.id = :orgId AND mu.id = :measurementUnitId AND mu.isDeleted = 0")
    Optional<MeasurementUnit> findMeasurementUnitByIdWhereIsDeletedEqualsZero(@Param("orgId") Long orgId, @Param("measurementUnitId") Long id);
}
