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
import com.teamsits.automatix.repository.master_data_repository.PartyRepo;
import com.teamsits.automatix.repository.master_data_repository.ProductRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
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
    private final SecurityUtils securityUtils;

    public List<CashOutResponse> getCashOutsByDate(LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return cashOutRepo.findCashOutsByDateWhereIsDeletedEqualsZero(orgId, date)
                .stream()
                .map(CashOutResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<CashOutResponse> getCashOutById(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return cashOutRepo.findCashOutByIdWhereIsDeletedEqualsZero(orgId, id).map(CashOutResponse::new);
    }

    @Transactional
    public Optional<CashOutResponse> addCashOut(CashOutRequest cashOutRequest) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        Long userId = securityUtils.getCurrentUserId();

        Party party = null;
        if (cashOutRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(orgId, cashOutRequest.getPartyId())
                    .orElseThrow(() -> new RuntimeException("Party by ID : " + cashOutRequest.getPartyId() + " not found."));
        }

        Bank bank = null;
        if (cashOutRequest.getBankId() != null) {
            bank = bankRepo.findBankByIdWhereIsDeletedEqualsZero(orgId, cashOutRequest.getBankId())
                    .orElseThrow(() -> new RuntimeException("Bank by ID : " + cashOutRequest.getBankId() + " not found."));
        }

        Product product = null;
        Stock stock = null;
        if (cashOutRequest.getProductId() != null) {
            product = productRepo.findProductByIdWhereIsDeletedEqualsZero(orgId, cashOutRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product by ID : " + cashOutRequest.getProductId() + " not found."));

            if (bank != null || party != null) {
                throw new RuntimeException("Party and Bank cannot co-exist with Product");
            }

            stock = new Stock(product, cashOutRequest);
            stock.setOrganization(securityUtils.getCurrentOrganization());
            stock.setCreatedBy(userId);
            stock.setUpdatedBy(userId);
            stockRepo.save(stock);
        }

        CashOut cashOut = new CashOut(cashOutRequest, product, party, bank, stock);
        cashOut.setOrganization(securityUtils.getCurrentOrganization());
        cashOut.setCreatedBy(userId);
        cashOut.setUpdatedBy(userId);
        return Optional.of(cashOutRepo.save(cashOut)).map(CashOutResponse::new);
    }

    @Transactional
    public void deleteCashOut(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        if (cashOutRepo.existsById(id)) {
            CashOut cashOut = cashOutRepo
                    .findCashOutByIdWhereIsDeletedEqualsZero(orgId, id)
                    .orElseThrow(() -> new EntityNotFoundException("CashOut not found."));

            if (cashOut.getStock() != null) {
                Stock stock = cashOut.getStock();
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
        Long orgId = securityUtils.getCurrentOrganizationId();

        Party party = null;
        if (cashOutRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(orgId, cashOutRequest.getPartyId())
                    .orElseThrow(() -> new EntityNotFoundException("Party not Found"));
        }

        Product product = null;
        Stock stock = null;
        if (cashOutRequest.getProductId() != null) {
            product = productRepo.findProductByIdWhereIsDeletedEqualsZero(orgId, cashOutRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not Found"));

            stock = cashOutRepo.findCashOutByIdWhereIsDeletedEqualsZero(orgId, cashOutRequest.getId())
                    .map(CashOut::getStock)
                    .orElseThrow(() -> new EntityNotFoundException("Product found but Stock not Found"));
        }

        Bank bank = null;
        if (cashOutRequest.getBankId() != null) {
            bank = bankRepo.findById(cashOutRequest.getBankId())
                    .orElseThrow(() -> new EntityNotFoundException("Bank not Found"));
        }

        if (product != null && (party != null || bank != null)) {
            throw new RuntimeException("Party and Bank cannot co-exist with Product");
        }

        final Product finalProduct = product;
        final Stock finalStock = stock;
        final Party finalParty = party;
        final Bank finalBank = bank;
        return cashOutRepo.findCashOutByIdWhereIsDeletedEqualsZero(orgId, cashOutRequest.getId())
                .map((CashOut cashOut) -> {
                    cashOut.setParty(finalParty);
                    cashOut.setProduct(finalProduct);
                    cashOut.setBank(finalBank);
                    cashOut.setQuantity(cashOutRequest.getQuantity());
                    cashOut.setParticulars(cashOutRequest.getParticulars());
                    cashOut.setAmount(cashOutRequest.getAmount());
                    cashOut.setTransactionDate(cashOutRequest.getTransactionDate());
                    cashOut.setUpdatedBy(securityUtils.getCurrentUserId());

                    if (finalProduct != null && finalStock != null) {
                        finalStock.setQuantity(cashOutRequest.getQuantity());
                        finalStock.setTransactionDate(cashOutRequest.getTransactionDate());
                        stockRepo.save(finalStock);
                        cashOut.setStock(finalStock);
                    }

                    return cashOut;
                })
                .map(cashOutRepo::save)
                .map(CashOutResponse::new);
    }
}
