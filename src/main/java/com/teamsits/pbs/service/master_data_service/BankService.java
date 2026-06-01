package com.teamsits.pbs.service.master_data_service;

import com.teamsits.pbs.entities.master_entity.Bank;
import com.teamsits.pbs.models.master_models.BankModel;
import com.teamsits.pbs.repository.master_data_repository.BankRepo;
import com.teamsits.pbs.utils.ApplicationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankService {
    private final BankRepo bankRepo;

    public List<BankModel> getBanks() {
        return bankRepo.findBanksWhereIsDeletedEqualsZero()
                .stream()
                .map(BankModel::new)
                .collect(Collectors.toList());
    }

    public Optional<BankModel> getBankById(Long id) {
        return bankRepo.findBankByIdWhereIsDeletedEqualsZero(id).map(BankModel::new);
    }

    public Optional<BankModel> addBank(BankModel bankModel) {
        boolean exists = bankRepo.findBanksWhereIsDeletedEqualsZero()
                .stream()
                .anyMatch(bank -> bank.getName().equalsIgnoreCase(bankModel.getName()));

        if (exists) {
            throw new IllegalArgumentException(bankModel.getName() + " already exists.");
        }

        return Optional.of(new BankModel(bankRepo.save(new Bank(bankModel))));
    }

    public void deleteBank(Long id) {
        if (bankRepo.existsById(id)) {
            Bank bank = bankRepo
                    .findBankByIdWhereIsDeletedEqualsZero(id)
                    .orElseThrow(() -> new RuntimeException("Bank not found."));

            bank.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            bankRepo.save(bank);
        }
    }

    public Optional<BankModel> updateBank(BankModel bankModel) {
        boolean exists = bankRepo.findBanksWhereIsDeletedEqualsZero()
                .stream()
                .anyMatch(bank -> bank.getName().equalsIgnoreCase(bankModel.getName()));

        if (exists) {
            throw new IllegalArgumentException(bankModel.getName() + " already exists.");
        }

        Bank bank = bankRepo.findBankByIdWhereIsDeletedEqualsZero(bankModel.getId())
                .orElseThrow(() -> new RuntimeException("This Bank does not exist"));

        if (!Objects.equals(bank.getName(), bankModel.getName())) {
            bank.setName(bankModel.getName());
        }

        if (!Objects.equals(bank.getPrefix(), bankModel.getPrefix())) {
            bank.setPrefix(bankModel.getPrefix());
        }

        return Optional.of(new BankModel(bankRepo.save(bank)));
    }
}
