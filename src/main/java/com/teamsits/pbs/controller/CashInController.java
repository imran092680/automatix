package com.teamsits.pbs.controller;

import com.teamsits.pbs.models.cash_in.CashInRequest;
import com.teamsits.pbs.models.cash_in.CashInResponse;
import com.teamsits.pbs.service.CashInService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/cashIn")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CashInController {
    public final CashInService cashInService;

    @GetMapping()
    public ResponseEntity<List<CashInResponse>> getCashInsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok(cashInService.getCashInsByDate(date));
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("total-cash-in-by-date")
    public ResponseEntity<Double> getTotalCashInAmountByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(cashInService.getTotalCashInByDate(date));
    }

    @GetMapping("total-cash-out-by-date")
    public ResponseEntity<Double> getTotalCashOutAmountByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(cashInService.getTotalCashOutByDate(date));
    }

    @GetMapping("total-receivable-by-date")
    public ResponseEntity<Double> getTotalReceivableAmountByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(cashInService.getTotalReceivableByDate(date));
    }

    @GetMapping("total-sales-by-date")
    public ResponseEntity<Double> getTotalSalesAmountByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(cashInService.getTotalSalesByDate(date));
    }

    @GetMapping("opening-balance")
    public ResponseEntity<Double> getOpeningBalance(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok(cashInService.getOpeningBalance(date));
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<CashInResponse>> getCashInById(@PathVariable Long id) {
        return ResponseEntity.ok(cashInService.getCashInById(id));
    }

    @PostMapping()
    public ResponseEntity<Optional<CashInResponse>> addCashIn(@RequestBody CashInRequest cashInRequest) {
        return ResponseEntity.ok(cashInService.addCashIn(cashInRequest));
    }

    @DeleteMapping("{id}")
    public void deleteCashIn(@PathVariable Long id) {
        cashInService.deleteCashIn(id);
    }

    @PutMapping()
    public ResponseEntity<Optional<CashInResponse>> updateCashIn(@RequestBody CashInRequest cashInRequest) {
        return ResponseEntity.ok(cashInService.updateCashIn(cashInRequest));
    }
}
