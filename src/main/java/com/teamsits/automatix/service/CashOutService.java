package com.teamsits.automatix.service;

import com.teamsits.automatix.entities.CashOut;
import com.teamsits.automatix.entities.Stock;
import com.teamsits.automatix.entities.master_entity.Bank;
import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.models.cash_out.CashOutRequest;
import com.teamsits.automatix.models.cash_out.CashOutResponse;
import com.teamsits.automatix.repository.CashOutRepo;
import com.teamsits.automatix.repository.StockRepo;
import com.teamsits.automatix.repository.master_data_repository.BankRepo;
import com.teamsits.automatix.repository.master_data_repository.MeasurementUnitRepo;
import com.teamsits.automatix.repository.master_data_repository.PartyRepo;
import com.teamsits.automatix.repository.master_data_repository.ProductRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CashOutService {
    private final CashOutRepo cashOutRepo;
    private final PartyRepo partyRepo;
    private final ProductRepo productRepo;
    private final BankRepo bankRepo;
    private final StockRepo stockRepo;

    public List<CashOutResponse> getCashOutsByDate(LocalDate date) {
        return cashOutRepo.findCashOutsByDateWhereIsDeletedEqualsZero(date)
                .stream()
                .map(CashOutResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<CashOutResponse> getCashOutById(Long id) {
        return cashOutRepo.findCashOutByIdWhereIsDeletedEqualsZero(id).map(CashOutResponse::new);
    }

    @Transactional
    public Optional<CashOutResponse> addCashOut(CashOutRequest cashOutRequest) {
        Party party = null;
        if (cashOutRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(cashOutRequest.getPartyId())
                    .orElseThrow(() -> new RuntimeException("Party by ID : " + cashOutRequest.getPartyId() + " not found."));


        }

        Bank bank = null;
        if (cashOutRequest.getBankId() != null) {
            bank = bankRepo.findBankByIdWhereIsDeletedEqualsZero(cashOutRequest.getBankId())
                    .orElseThrow(() -> new RuntimeException("Bank by ID : " + cashOutRequest.getBankId() + " not found."));
        }

        Product product = null;
        Stock stock = null;
        if (cashOutRequest.getProductId() != null) {
            product = productRepo.findProductByIdWhereIsDeletedEqualsZero(cashOutRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product by ID : " + cashOutRequest.getProductId() + " not found."));

            if (bank != null || party != null) {
                throw new RuntimeException("Party and Bank cannot co-exist with Product");
            }

            stock = new Stock(product, cashOutRequest);
            stockRepo.save(stock);
        }

        return Optional.of(cashOutRepo.save(new CashOut(cashOutRequest, product, party, bank, stock)))
                .map(CashOutResponse::new);
    }

    @Transactional
    public void deleteCashOut(Long id) {
        if (cashOutRepo.existsById(id)) {
            CashOut cashOut = cashOutRepo
                    .findCashOutByIdWhereIsDeletedEqualsZero(id)
                    .orElseThrow(() -> new EntityNotFoundException("ExpenseBank not found."));

            Stock stock;
            if (cashOut.getStock() != null) {
                stock = cashOut.getStock();

                stock.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
                cashOut.setStock(stock);
                stockRepo.save(stock);
            }

            cashOut.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            cashOutRepo.save(cashOut);
        }
    }

    @Transactional
    public Optional<CashOutResponse> updateCashOut(CashOutRequest cashOutRequest) {
        Party party;
        if (cashOutRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(cashOutRequest.getPartyId())
                    .orElseThrow(() -> new EntityNotFoundException("Party not Found"));

        } else {
            party = null;
        }

        Product product;
        Stock stock;
        if (cashOutRequest.getProductId() != null) {
            product = productRepo.findProductByIdWhereIsDeletedEqualsZero(cashOutRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not Found"));

            stock = cashOutRepo.findCashOutByIdWhereIsDeletedEqualsZero(cashOutRequest.getId())
                    .map(CashOut::getStock)
                    .orElseThrow(() -> new EntityNotFoundException("Product found but Stock not Found"));
        } else {
            product = null;
            stock = null;
        }

        Bank bank;
        if (cashOutRequest.getBankId() != null) {
            bank = bankRepo.findById(cashOutRequest.getBankId())
                    .orElseThrow(() -> new EntityNotFoundException("Bank not Found"));
        } else {
            bank = null;
        }

        if (product != null && (party != null || bank != null)) {
            throw new RuntimeException("Party and Bank cannot co-exist with Product");
        }

        return cashOutRepo.findCashOutByIdWhereIsDeletedEqualsZero(cashOutRequest.getId())
                .map((CashOut cashOut) -> {
                    cashOut.setParty(party);
                    cashOut.setProduct(product);
                    cashOut.setBank(bank);
                    cashOut.setQuantity(cashOutRequest.getQuantity());
                    cashOut.setParticulars(cashOutRequest.getParticulars());
                    cashOut.setAmount(cashOutRequest.getAmount());
                    cashOut.setTransactionDate(cashOutRequest.getTransactionDate());

                    if (product != null && stock != null) {
                        stock.setQuantity(cashOutRequest.getQuantity());
                        stock.setTransactionDate(cashOutRequest.getTransactionDate());

                        stockRepo.save(stock);
                        cashOut.setStock(stock);
                    }

                    return cashOut;
                })
                .map(cashOutRepo::save)
                .map(CashOutResponse::new);
    }
}
