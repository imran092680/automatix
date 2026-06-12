package com.teamsits.automatix.service.master_data_service;

import com.teamsits.automatix.entities.master_entity.Bank;
import com.teamsits.automatix.models.master_models.BankModel;
import com.teamsits.automatix.repository.master_data_repository.BankRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
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
    private final SecurityUtils securityUtils;

    public List<BankModel> getBanks() {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return bankRepo.findBanksWhereIsDeletedEqualsZero(orgId)
                .stream()
                .map(BankModel::new)
                .collect(Collectors.toList());
    }

    public Optional<BankModel> getBankById(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return bankRepo.findBankByIdWhereIsDeletedEqualsZero(orgId, id).map(BankModel::new);
    }

    public Optional<BankModel> addBank(BankModel bankModel) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        if (bankRepo.existsByOrganizationIdAndName(orgId, bankModel.getName())) {
            throw new IllegalArgumentException(bankModel.getName() + " already exists.");
        }

        Bank bank = new Bank(bankModel);
        bank.setOrganization(securityUtils.getCurrentOrganization());
        bank.setCreatedBy(securityUtils.getCurrentUserId());
        bank.setUpdatedBy(securityUtils.getCurrentUserId());
        return Optional.of(new BankModel(bankRepo.save(bank)));
    }

    public void deleteBank(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        if (bankRepo.existsById(id)) {
            Bank bank = bankRepo
                    .findBankByIdWhereIsDeletedEqualsZero(orgId, id)
                    .orElseThrow(() -> new RuntimeException("Bank not found."));

            bank.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            bankRepo.save(bank);
        }
    }

    public Optional<BankModel> updateBank(BankModel bankModel) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        Bank bank = bankRepo.findBankByIdWhereIsDeletedEqualsZero(orgId, bankModel.getId())
                .orElseThrow(() -> new RuntimeException("This Bank does not exist"));

        if (!Objects.equals(bank.getName(), bankModel.getName()) &&
                bankRepo.existsByOrganizationIdAndName(orgId, bankModel.getName())) {
            throw new IllegalArgumentException(bankModel.getName() + " already exists.");
        }

        bank.setName(bankModel.getName());
        bank.setPrefix(bankModel.getPrefix());
        bank.setUpdatedBy(securityUtils.getCurrentUserId());

        return Optional.of(new BankModel(bankRepo.save(bank)));
    }
}
