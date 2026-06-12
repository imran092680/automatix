package com.teamsits.automatix.service;

import com.teamsits.automatix.entities.Stock;
import com.teamsits.automatix.entities.master_entity.Product;
import com.teamsits.automatix.enums.StockTransactionType;
import com.teamsits.automatix.models.stock.StockPerProductByDateResponse;
import com.teamsits.automatix.models.stock.StockRequest;
import com.teamsits.automatix.models.stock.StockResponse;
import com.teamsits.automatix.repository.StockRepo;
import com.teamsits.automatix.repository.master_data_repository.ProductRepo;
import com.teamsits.automatix.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepo stockRepo;
    private final ProductRepo productRepo;
    private final SecurityUtils securityUtils;

    public List<StockPerProductByDateResponse> getStocksPerProductByDate(LocalDate currentDate) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return stockRepo.getStockDataByDate(orgId, currentDate);
    }

    public Optional<StockResponse> addStock(StockRequest stockRequest) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        Long userId = securityUtils.getCurrentUserId();

        final Product product = productRepo.findProductByIdWhereIsDeletedEqualsZero(orgId, stockRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product by ID : " + stockRequest.getProductId() + " not found."));

        final Stock stock = new Stock(product, stockRequest);
        stock.setOrganization(securityUtils.getCurrentOrganization());
        stock.setCreatedBy(userId);
        stock.setUpdatedBy(userId);
        stockRepo.save(stock);

        return Optional.of(new StockResponse(stock));
    }

    public Double getAvailableStockByProductId(Long productId, LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        Product product = productRepo.findProductByIdWhereIsDeletedEqualsZero(orgId, productId)
                .orElseThrow(() -> new RuntimeException("Product by ID : " + productId + " not found."));

        final List<Stock> stockList = stockRepo.findStocksByProductByDate(orgId, product.getId(), date);

        Double totalPurchased = stockList.stream()
                .filter(stock -> stock.getStockTransactionType() == StockTransactionType.PURCHASE)
                .mapToDouble(Stock::getQuantity)
                .sum();

        Double totalSold = stockList.stream()
                .filter(stock -> stock.getStockTransactionType() == StockTransactionType.SALES)
                .mapToDouble(Stock::getQuantity)
                .sum();

        return totalPurchased - totalSold;
    }

    public byte[] exportStocksToExcel(LocalDate currentDate) throws IOException {
        List<StockPerProductByDateResponse> stockList = getStocksPerProductByDate(currentDate);

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Stock Data");

            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            CellStyle subtitleStyle = workbook.createCellStyle();
            Font subtitleFont = workbook.createFont();
            subtitleFont.setBold(true);
            subtitleFont.setFontHeightInPoints((short) 12);
            subtitleStyle.setFont(subtitleFont);
            subtitleStyle.setAlignment(HorizontalAlignment.LEFT);

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Date Wise Stock Report");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

            Row dateRow = sheet.createRow(1);
            Cell dateCell = dateRow.createCell(0);
            dateCell.setCellValue("Report Date: " + currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            dateCell.setCellStyle(subtitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

            Row headerRow = sheet.createRow(3);
            String[] headers = {"Product Name", "Opening Stock", "Purchased", "Sold", "Remaining Stock"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(subtitleStyle);
            }

            int rowIdx = 4;
            for (StockPerProductByDateResponse stock : stockList) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(stock.getProductName().toString());
                row.createCell(1).setCellValue(stock.getOpeningStock());
                row.createCell(2).setCellValue(stock.getPurchased());
                row.createCell(3).setCellValue(stock.getSold());
                row.createCell(4).setCellValue(stock.getRemainingStock());
            }

            for (int i = 0; i <= 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }
}
