package com.teamsits.pbs.service;

import com.teamsits.pbs.entities.CashIn;
import com.teamsits.pbs.entities.CashOut;
import com.teamsits.pbs.entities.Receivable;
import com.teamsits.pbs.entities.Sales;
import com.teamsits.pbs.entities.master_entity.Bank;
import com.teamsits.pbs.entities.master_entity.Party;
import com.teamsits.pbs.models.cash_in.CashInRequest;
import com.teamsits.pbs.models.cash_in.CashInResponse;
import com.teamsits.pbs.repository.CashInRepo;
import com.teamsits.pbs.repository.CashOutRepo;
import com.teamsits.pbs.repository.ReceivableRepo;
import com.teamsits.pbs.repository.SalesRepo;
import com.teamsits.pbs.repository.master_data_repository.BankRepo;
import com.teamsits.pbs.repository.master_data_repository.PartyRepo;
import com.teamsits.pbs.utils.ApplicationConstant;
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

    public List<CashInResponse> getCashInsByDate(LocalDate date) {
        return cashInRepo.findCashInsByDateWhereIsDeletedEqualsZero(date)
                .stream()
                .map(CashInResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<CashInResponse> getCashInById(Long id) {
        return cashInRepo.findCashInByIdWhereIsDeletedEqualsZero(id).map(CashInResponse::new);
    }

    public Optional<CashInResponse> addCashIn(CashInRequest cashInRequest) {
        Party party = null;
        Bank bank = null;

        if (cashInRequest.getBankId() != null) {
            bank = bankRepo.findBankByIdWhereIsDeletedEqualsZero(cashInRequest.getBankId())
                    .orElseThrow(() -> new RuntimeException("Bank by ID : " + cashInRequest.getBankId() + " not found."));
        } else if (cashInRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(cashInRequest.getPartyId())
                    .orElseThrow(() -> new RuntimeException("Party by ID : " + cashInRequest.getPartyId() + " not found."));
        }

        CashIn cashIn = new CashIn(cashInRequest, party, bank);
        cashInRepo.save(cashIn);

        return Optional.of(new CashInResponse(cashIn));
    }

    public void deleteCashIn(Long id) {
        if (cashInRepo.existsById(id)) {
            CashIn cashIn = cashInRepo
                    .findCashInByIdWhereIsDeletedEqualsZero(id)
                    .orElseThrow(() -> new RuntimeException("ReceiveBank not found."));

            cashIn.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            cashInRepo.save(cashIn);
        }
    }

    public Optional<CashInResponse> updateCashIn(CashInRequest cashInRequest) {
        Party party;
        Bank bank;

        if (cashInRequest.getBankId() == null && cashInRequest.getPartyId() == null) {
            party = null;
            bank = null;
        } else if (cashInRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(cashInRequest.getPartyId())
                    .orElseThrow(() -> new RuntimeException("Bank by ID : " + cashInRequest.getBankId() + " not found."));
            bank = null;
        } else {
            party = null;
            bank = bankRepo.findBankByIdWhereIsDeletedEqualsZero(cashInRequest.getBankId())
                    .orElseThrow(() -> new RuntimeException("Party by ID : " + cashInRequest.getPartyId() + " not found."));
        }

        return cashInRepo.findById(cashInRequest.getId())
                .map((CashIn cashIn) -> {
                    cashIn.setParty(party);
                    cashIn.setBank(bank);
                    cashIn.setPurpose(cashInRequest.getPurpose());
                    cashIn.setAmount(cashInRequest.getAmount());
                    cashIn.setMemoDate(cashInRequest.getMemoDate());
                    cashIn.setTransactionDate(cashInRequest.getTransactionDate());

                    return cashIn;
                })
                .map(cashInRepo::save)
                .map(CashInResponse::new);
    }

    public Double getOpeningBalance(LocalDate date) {
        return this.cashInRepo.getOpeningBalance(date);
    }

    public double getTotalCashInByDate(LocalDate date) {
        return cashInRepo.findCashInsByDateWhereIsDeletedEqualsZero(date).stream().mapToDouble(CashIn::getAmount).sum();
    }

    public double getTotalSalesByDate(LocalDate date) {
        return salesRepo.findSalesInfosByDateWhereIsDeletedEqualsZero(date).stream().mapToDouble(Sales::getAmount).sum();
    }

    public double getTotalCashOutByDate(LocalDate date) {
        return cashOutRepo.findCashOutsByDateWhereIsDeletedEqualsZero(date).stream().mapToDouble(CashOut::getAmount).sum();
    }

    public double getTotalReceivableByDate(LocalDate date) {
        return receivableRepo.findReceivablesByDateWhereIsDeletedEqualsZero(date).stream().mapToDouble(Receivable::getAmount).sum();
    }
}
