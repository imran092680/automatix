package com.teamsits.automatix.service;

import com.teamsits.automatix.entities.CashIn;
import com.teamsits.automatix.entities.CashOut;
import com.teamsits.automatix.entities.Receivable;
import com.teamsits.automatix.entities.Sales;
import com.teamsits.automatix.entities.master_entity.Bank;
import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.models.cash_in.CashInRequest;
import com.teamsits.automatix.models.cash_in.CashInResponse;
import com.teamsits.automatix.repository.CashInRepo;
import com.teamsits.automatix.repository.CashOutRepo;
import com.teamsits.automatix.repository.ReceivableRepo;
import com.teamsits.automatix.repository.SalesRepo;
import com.teamsits.automatix.repository.master_data_repository.BankRepo;
import com.teamsits.automatix.repository.master_data_repository.PartyRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CashInService {
    private final CashInRepo cashInRepo;
    private final PartyRepo partyRepo;
    private final BankRepo bankRepo;
    private final SalesRepo salesRepo;
    private final CashOutRepo cashOutRepo;
    private final ReceivableRepo receivableRepo;
    private final SecurityUtils securityUtils;

    public List<CashInResponse> getCashInsByDate(LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return cashInRepo.findCashInsByDateWhereIsDeletedEqualsZero(orgId, date)
                .stream()
                .map(CashInResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<CashInResponse> getCashInById(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return cashInRepo.findCashInByIdWhereIsDeletedEqualsZero(orgId, id).map(CashInResponse::new);
    }

    public Optional<CashInResponse> addCashIn(CashInRequest cashInRequest) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        Party party = null;
        Bank bank = null;

        if (cashInRequest.getBankId() != null) {
            bank = bankRepo.findBankByIdWhereIsDeletedEqualsZero(orgId, cashInRequest.getBankId())
                    .orElseThrow(() -> new RuntimeException("Bank by ID : " + cashInRequest.getBankId() + " not found."));
        } else if (cashInRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(orgId, cashInRequest.getPartyId())
                    .orElseThrow(() -> new RuntimeException("Party by ID : " + cashInRequest.getPartyId() + " not found."));
        }

        CashIn cashIn = new CashIn(cashInRequest, party, bank);
        cashIn.setOrganization(securityUtils.getCurrentOrganization());
        cashIn.setCreatedBy(securityUtils.getCurrentUserId());
        cashIn.setUpdatedBy(securityUtils.getCurrentUserId());
        cashInRepo.save(cashIn);

        return Optional.of(new CashInResponse(cashIn));
    }

    public void deleteCashIn(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        if (cashInRepo.existsById(id)) {
            CashIn cashIn = cashInRepo
                    .findCashInByIdWhereIsDeletedEqualsZero(orgId, id)
                    .orElseThrow(() -> new RuntimeException("CashIn not found."));

            cashIn.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            cashInRepo.save(cashIn);
        }
    }

    public Optional<CashInResponse> updateCashIn(CashInRequest cashInRequest) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        Party party;
        Bank bank;

        if (cashInRequest.getBankId() == null && cashInRequest.getPartyId() == null) {
            party = null;
            bank = null;
        } else if (cashInRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(orgId, cashInRequest.getPartyId())
                    .orElseThrow(() -> new RuntimeException("Party by ID : " + cashInRequest.getPartyId() + " not found."));
            bank = null;
        } else {
            party = null;
            bank = bankRepo.findBankByIdWhereIsDeletedEqualsZero(orgId, cashInRequest.getBankId())
                    .orElseThrow(() -> new RuntimeException("Bank by ID : " + cashInRequest.getBankId() + " not found."));
        }

        return cashInRepo.findById(cashInRequest.getId())
                .map((CashIn cashIn) -> {
                    cashIn.setParty(party);
                    cashIn.setBank(bank);
                    cashIn.setPurpose(cashInRequest.getPurpose());
                    cashIn.setAmount(cashInRequest.getAmount());
                    cashIn.setMemoDate(cashInRequest.getMemoDate());
                    cashIn.setTransactionDate(cashInRequest.getTransactionDate());
                    cashIn.setUpdatedBy(securityUtils.getCurrentUserId());
                    return cashIn;
                })
                .map(cashInRepo::save)
                .map(CashInResponse::new);
    }

    public Double getOpeningBalance(LocalDate date) {
        return this.cashInRepo.getOpeningBalance(securityUtils.getCurrentOrganizationId(), date);
    }

    public double getTotalCashInByDate(LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return cashInRepo.findCashInsByDateWhereIsDeletedEqualsZero(orgId, date).stream().mapToDouble(CashIn::getAmount).sum();
    }

    public double getTotalSalesByDate(LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return salesRepo.findSalesInfosByDateWhereIsDeletedEqualsZero(orgId, date).stream().mapToDouble(Sales::getAmount).sum();
    }

    public double getTotalCashOutByDate(LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return cashOutRepo.findCashOutsByDateWhereIsDeletedEqualsZero(orgId, date).stream().mapToDouble(CashOut::getAmount).sum();
    }

    public double getTotalReceivableByDate(LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return receivableRepo.findReceivablesByDateWhereIsDeletedEqualsZero(orgId, date).stream().mapToDouble(Receivable::getAmount).sum();
    }
}
