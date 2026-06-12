package com.teamsits.automatix.controller.master_controller;

import com.teamsits.automatix.models.master_models.BankModel;
import com.teamsits.automatix.service.master_data_service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/bank")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BankController {

    public final BankService bankService;

    @GetMapping()
    public ResponseEntity<List<BankModel>> getBanks() {
        try {
            return ResponseEntity.ok(bankService.getBanks());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<BankModel>> getBankById(@PathVariable Long id) {
        return ResponseEntity.ok(bankService.getBankById(id));
    }

    @PostMapping()
    public ResponseEntity<Optional<BankModel>> addBank(@RequestBody BankModel bankModel) {
        return ResponseEntity.ok(bankService.addBank(bankModel));
    }

    @DeleteMapping("{id}")
    public void deleteBank(@PathVariable Long id) {
        bankService.deleteBank(id);
    }

    @PutMapping()
    public ResponseEntity<Optional<BankModel>> updateProduct(@RequestBody BankModel bankModel) {
        return ResponseEntity.ok(bankService.updateBank(bankModel));
    }
}
