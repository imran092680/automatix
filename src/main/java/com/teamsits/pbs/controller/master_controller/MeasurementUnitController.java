package com.teamsits.pbs.controller.master_controller;

import com.teamsits.pbs.models.master_models.MeasurementUnitModel;
import com.teamsits.pbs.service.master_data_service.MeasurementUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/measurementUnit")
@CrossOrigin("*")
@RequiredArgsConstructor
public class MeasurementUnitController {

    public final MeasurementUnitService measurementUnitService;

    @GetMapping()
    public ResponseEntity<List<MeasurementUnitModel>> getMeasurementUnits() {
        try {
            return ResponseEntity.ok(measurementUnitService.getMeasurementUnits());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<MeasurementUnitModel>> getMeasurementUnitById(@PathVariable Long id) {
        return ResponseEntity.ok(measurementUnitService.getMeasurementUnitById(id));
    }

    @PostMapping()
    public ResponseEntity<Optional<MeasurementUnitModel>> addMeasurementUnit(@RequestBody MeasurementUnitModel MeasurementUnitModel) {
        return ResponseEntity.ok(measurementUnitService.addMeasurementUnit(MeasurementUnitModel));
    }

    @DeleteMapping("{id}")
    public void deleteMeasurementUnit(@PathVariable Long id) {
        measurementUnitService.deleteMeasurementUnit(id);
    }

    @PutMapping()
    public ResponseEntity<Optional<MeasurementUnitModel>> updateProduct(@RequestBody MeasurementUnitModel MeasurementUnitModel) {
        return ResponseEntity.ok(measurementUnitService.updateMeasurementUnit(MeasurementUnitModel));
    }
}
