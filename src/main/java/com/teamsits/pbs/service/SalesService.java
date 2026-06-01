package com.teamsits.pbs.service;

import com.teamsits.pbs.entities.Sales;
import com.teamsits.pbs.entities.Stock;
import com.teamsits.pbs.entities.master_entity.Party;
import com.teamsits.pbs.entities.master_entity.Product;
import com.teamsits.pbs.models.sales.SalesRequest;
import com.teamsits.pbs.models.sales.SalesResponse;
import com.teamsits.pbs.repository.SalesRepo;
import com.teamsits.pbs.repository.StockRepo;
import com.teamsits.pbs.repository.master_data_repository.PartyRepo;
import com.teamsits.pbs.repository.master_data_repository.ProductRepo;
import com.teamsits.pbs.utils.ApplicationConstant;
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

    public List<SalesResponse> getSalesByDate(LocalDate date) {
        return salesRepo.findSalesInfosByDateWhereIsDeletedEqualsZero(date)
                .stream()
                .map(SalesResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<SalesResponse> getSalesById(Long id) {
        return salesRepo.findSalesInfoByIdWhereIsDeletedEqualsZero(id).map(SalesResponse::new);
    }

    public Optional<SalesResponse> addSales(SalesRequest salesRequest) {
        Product product = productRepo.findProductByIdWhereIsDeletedEqualsZero(salesRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not Found"));

        Party party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(salesRequest.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException("Party not Found"));

        Stock stock = new Stock(product, salesRequest);
        Sales sales = new Sales(salesRequest, product, party, stock);

        stockRepo.save(stock);
        salesRepo.save(sales);

        return Optional.of(new SalesResponse(sales));
    }

    @Transactional
    public Optional<SalesResponse> updateSales(SalesRequest salesRequest) {
        Sales sales = salesRepo.findById(salesRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Sales Data not Found"));

        Product product = productRepo.findProductByIdWhereIsDeletedEqualsZero(salesRequest.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not Found"));

        Party party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(salesRequest.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException("Party not Found"));

        Stock stock = sales.getStock();
        stock.setQuantity(salesRequest.getCount());
        stock.setProduct(product);
        stock.setTransactionDate(salesRequest.getTransactionDate());
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

                    return s;
                })
                .map(salesRepo::save)
                .map(SalesResponse::new);
    }

    public void deleteSales(Long id) {
        if (salesRepo.existsById(id)) {
            Sales sales = salesRepo
                    .findSalesInfoByIdWhereIsDeletedEqualsZero(id)
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
