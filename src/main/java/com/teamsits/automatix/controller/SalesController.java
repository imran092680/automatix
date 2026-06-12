package com.teamsits.automatix.controller;

import com.teamsits.automatix.models.sales.SalesRequest;
import com.teamsits.automatix.models.sales.SalesResponse;
import com.teamsits.automatix.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/sales")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SalesController {
    public final SalesService salesService;

    @GetMapping()
    public ResponseEntity<List<SalesResponse>> getSalesByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(salesService.getSalesByDate(date));
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<SalesResponse>> getSalesById(@PathVariable Long id) {
        return ResponseEntity.ok(salesService.getSalesById(id));
    }

    @PostMapping()
    public ResponseEntity<Optional<SalesResponse>> addSales(@RequestBody SalesRequest salesRequest) {
        return ResponseEntity.ok(salesService.addSales(salesRequest));
    }

    @DeleteMapping("{id}")
    public void deleteSales(@PathVariable Long id) {
        salesService.deleteSales(id);
    }

    @PutMapping()
    public ResponseEntity<Optional<SalesResponse>> updateProduct(@RequestBody SalesRequest salesRequest) {
        return ResponseEntity.ok(salesService.updateSales(salesRequest));
    }
}
