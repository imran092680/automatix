package com.teamsits.pbs.controller;

import com.teamsits.pbs.models.receivable.ReceivableRequest;
import com.teamsits.pbs.models.receivable.ReceivableResponse;
import com.teamsits.pbs.service.ReceivableService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/receivable")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReceivableController {
    public final ReceivableService receivableService;

    @GetMapping()
    public ResponseEntity<List<ReceivableResponse>> getReceivablesByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok(receivableService.getReceivablesByDate(date));
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<ReceivableResponse>> getReceivableById(@PathVariable Long id) {
        return ResponseEntity.ok(receivableService.getReceivableById(id));
    }

    @PostMapping()
    public ResponseEntity<Optional<ReceivableResponse>> addReceivable(@RequestBody ReceivableRequest receivableRequest) {
        return ResponseEntity.ok(receivableService.addReceivable(receivableRequest));
    }

    @DeleteMapping("{id}")
    public void deleteReceivable(@PathVariable Long id) {
        receivableService.deleteReceivable(id);
    }

    @PutMapping()
    public ResponseEntity<Optional<ReceivableResponse>> updateReceivable(@RequestBody ReceivableRequest receivableRequest) {
        return ResponseEntity.ok(receivableService.updateReceivable(receivableRequest));
    }
}
