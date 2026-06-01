package com.teamsits.pbs.controller;

import com.teamsits.pbs.models.stock.StockPerProductByDateResponse;
import com.teamsits.pbs.models.stock.StockRequest;
import com.teamsits.pbs.models.stock.StockResponse;
import com.teamsits.pbs.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/stock")
@CrossOrigin("*")
@RequiredArgsConstructor
public class StockController {
    public final StockService stockService;

    @GetMapping("per-product")
    public ResponseEntity<List<StockPerProductByDateResponse>> getStocksPerProductByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok(stockService.getStocksPerProductByDate(date));
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("per-product/{productId}/{date}")
    public ResponseEntity<Double> getAvailableStockByProductId(@PathVariable Long productId, @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok(stockService.getAvailableStockByProductId(productId, date));
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<Optional<StockResponse>> addStock(@RequestBody StockRequest stockRequest) {
        return ResponseEntity.ok(stockService.addStock(stockRequest));
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportStocksToExcel(@RequestParam String date) throws IOException {
        LocalDate currentDate = LocalDate.parse(date);
        byte[] fileContent = stockService.exportStocksToExcel(currentDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=stocks_" + currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileContent);
    }

}
