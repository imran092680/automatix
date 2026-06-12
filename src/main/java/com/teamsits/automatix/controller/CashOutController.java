package com.teamsits.automatix.controller;

import com.teamsits.automatix.models.cash_out.CashOutRequest;
import com.teamsits.automatix.models.cash_out.CashOutResponse;
import com.teamsits.automatix.service.CashOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/cashOut")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CashOutController {
    public final CashOutService cashOutService;

    @GetMapping()
    public ResponseEntity<List<CashOutResponse>> getCashOutsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok(cashOutService.getCashOutsByDate(date));
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<CashOutResponse>> getCashOutById(@PathVariable Long id) {
        return ResponseEntity.ok(cashOutService.getCashOutById(id));
    }

    @PostMapping()
    public ResponseEntity<Optional<CashOutResponse>> addCashOut(@RequestBody CashOutRequest cashOutRequest) {
        return ResponseEntity.ok(cashOutService.addCashOut(cashOutRequest));
    }

    @DeleteMapping("{id}")
    public void deleteCashOut(@PathVariable Long id) {
        cashOutService.deleteCashOut(id);
    }

    @PutMapping()
    public ResponseEntity<Optional<CashOutResponse>> updateCashOut(@RequestBody CashOutRequest cashOutRequest) {
        return ResponseEntity.ok(cashOutService.updateCashOut(cashOutRequest));
    }
}
