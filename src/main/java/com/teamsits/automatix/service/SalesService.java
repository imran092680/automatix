package com.teamsits.automatix.service;

import com.teamsits.automatix.entities.Sales;
import com.teamsits.automatix.entities.Stock;
import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.models.sales.SalesRequest;
import com.teamsits.automatix.models.sales.SalesResponse;
import com.teamsits.automatix.repository.SalesRepo;
import com.teamsits.automatix.repository.StockRepo;
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
public class SalesService {
    private final SalesRepo salesRepo;
    private final ProductRepo productRepo;
    private final StockRepo stockRepo;
    private final PartyRepo partyRepo;
    private final SecurityUtils securityUtils;

    public List<SalesResponse> getSalesByDate(LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return salesRepo.findSalesInfosByDateWhereIsDeletedEqualsZero(orgId, date)
                .stream()
                .map(SalesResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<SalesResponse> getSalesById(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return salesRepo.findSalesInfoByIdWhereIsDeletedEqualsZero(orgId, id).map(SalesResponse::new);
    }

    public Optional<SalesResponse> addSales(SalesRequest salesRequest) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        Long userId = securityUtils.getCurrentUserId();

        Product product = productRepo.findProductByIdWhereIsDeletedEqualsZero(orgId, salesRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not Found"));

        Party party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(orgId, salesRequest.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException("Party not Found"));

        Stock stock = new Stock(product, salesRequest);
        stock.setOrganization(securityUtils.getCurrentOrganization());
        stock.setCreatedBy(userId);
        stock.setUpdatedBy(userId);

        Sales sales = new Sales(salesRequest, product, party, stock);
        sales.setOrganization(securityUtils.getCurrentOrganization());
        sales.setCreatedBy(userId);
        sales.setUpdatedBy(userId);

        stockRepo.save(stock);
        salesRepo.save(sales);

        return Optional.of(new SalesResponse(sales));
    }

    @Transactional
    public Optional<SalesResponse> updateSales(SalesRequest salesRequest) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        Sales sales = salesRepo.findById(salesRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Sales Data not Found"));

        Product product = productRepo.findProductByIdWhereIsDeletedEqualsZero(orgId, salesRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not Found"));

        Party party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(orgId, salesRequest.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException("Party not Found"));

        Stock stock = sales.getStock();
        stock.setQuantity(salesRequest.getCount());
        stock.setProduct(product);
        stock.setTransactionDate(salesRequest.getTransactionDate());
        stock.setUpdatedBy(securityUtils.getCurrentUserId());
        stockRepo.save(stock);

        return salesRepo.findById(salesRequest.getId())
                .map((Sales s) -> {
                    s.setProduct(product);
                    s.setParty(party);
                    s.setStock(stock);
                    s.setCount(salesRequest.getCount());
                    s.setPricePerUnit(salesRequest.getPricePerUnit());
                    s.setAmount(salesRequest.getAmount());
                    s.setTransactionDate(salesRequest.getTransactionDate());
                    s.setUpdatedBy(securityUtils.getCurrentUserId());
                    return s;
                })
                .map(salesRepo::save)
                .map(SalesResponse::new);
    }

    public void deleteSales(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        if (salesRepo.existsById(id)) {
            Sales sales = salesRepo
                    .findSalesInfoByIdWhereIsDeletedEqualsZero(orgId, id)
                    .orElseThrow(() -> new RuntimeException("SalesInfo not found."));

            sales.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);

            if (sales.getStock() != null) {
                sales.getStock().setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
                sales.setStock(sales.getStock());
                stockRepo.save(sales.getStock());
            }

            salesRepo.save(sales);
        }
    }
}
