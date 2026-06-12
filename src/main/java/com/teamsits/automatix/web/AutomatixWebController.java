package com.teamsits.automatix.web;

import com.teamsits.automatix.enums.PartyType;
import com.teamsits.automatix.models.cash_in.CashInRequest;
import com.teamsits.automatix.models.cash_out.CashOutRequest;
import com.teamsits.automatix.models.master_models.BankModel;
import com.teamsits.automatix.models.master_models.MeasurementUnitModel;
import com.teamsits.automatix.models.master_models.PartyModel;
import com.teamsits.automatix.models.master_models.ProductModel;
import com.teamsits.automatix.models.receivable.ReceivableRequest;
import com.teamsits.automatix.models.sales.SalesRequest;
import com.teamsits.automatix.models.stock.StockRequest;
import com.teamsits.automatix.service.CashInService;
import com.teamsits.automatix.service.CashOutService;
import com.teamsits.automatix.service.ReceivableService;
import com.teamsits.automatix.service.SalesService;
import com.teamsits.automatix.service.StockService;
import com.teamsits.automatix.service.master_data_service.BankService;
import com.teamsits.automatix.service.master_data_service.MeasurementUnitService;
import com.teamsits.automatix.service.master_data_service.PartyService;
import com.teamsits.automatix.service.master_data_service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class AutomatixWebController {
    private static final long SYSTEM_USER = 0L;

    private final BankService bankService;
    private final ProductService productService;
    private final PartyService partyService;
    private final MeasurementUnitService measurementUnitService;
    private final CashInService cashInService;
    private final CashOutService cashOutService;
    private final SalesService salesService;
    private final ReceivableService receivableService;
    private final StockService stockService;

    @ModelAttribute
    void shared(Model model) {
        model.addAttribute("appName", "Automatix");
    }

    @GetMapping("/")
    public String dashboard(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date,
            Model model
    ) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        double openingBalance = cashInService.getOpeningBalance(selectedDate);
        double totalCashIn = cashInService.getTotalCashInByDate(selectedDate);
        double totalCashOut = cashInService.getTotalCashOutByDate(selectedDate);
        double totalSales = cashInService.getTotalSalesByDate(selectedDate);
        double totalReceivable = cashInService.getTotalReceivableByDate(selectedDate);

        model.addAttribute("date", selectedDate);
        model.addAttribute("cashIns", cashInService.getCashInsByDate(selectedDate));
        model.addAttribute("cashOuts", cashOutService.getCashOutsByDate(selectedDate));
        model.addAttribute("sales", salesService.getSalesByDate(selectedDate));
        model.addAttribute("receivables", receivableService.getReceivablesByDate(selectedDate));
        model.addAttribute("openingBalance", openingBalance);
        model.addAttribute("totalCashIn", totalCashIn);
        model.addAttribute("totalCashOut", totalCashOut);
        model.addAttribute("totalSales", totalSales);
        model.addAttribute("totalReceivable", totalReceivable);
        model.addAttribute("grandTotal", (openingBalance + totalCashIn + totalSales) - (totalCashOut + totalReceivable));
        return "dashboard";
    }

    @GetMapping("/banks")
    public String banks(Model model) {
        model.addAttribute("bank", withAudit(new BankModel()));
        model.addAttribute("banks", bankService.getBanks());
        return "banks";
    }

    @PostMapping("/banks")
    public String addBank(@ModelAttribute BankModel bank, RedirectAttributes redirectAttributes) {
        return save(() -> bankService.addBank(withAudit(bank)), "/banks", redirectAttributes);
    }

    @PostMapping("/banks/{id}/delete")
    public String deleteBank(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return save(() -> bankService.deleteBank(id), "/banks", redirectAttributes);
    }

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("product", withAudit(new ProductModel()));
        model.addAttribute("products", productService.getProducts());
        return "products";
    }

    @PostMapping("/products")
    public String addProduct(@ModelAttribute ProductModel product, RedirectAttributes redirectAttributes) {
        return save(() -> productService.addProduct(withAudit(product)), "/products", redirectAttributes);
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return save(() -> productService.deleteProduct(id), "/products", redirectAttributes);
    }

    @GetMapping("/measurement-units")
    public String measurementUnits(Model model) {
        model.addAttribute("measurementUnit", withAudit(new MeasurementUnitModel()));
        model.addAttribute("measurementUnits", measurementUnitService.getMeasurementUnits());
        return "measurement-units";
    }

    @PostMapping("/measurement-units")
    public String addMeasurementUnit(@ModelAttribute MeasurementUnitModel measurementUnit, RedirectAttributes redirectAttributes) {
        return save(() -> measurementUnitService.addMeasurementUnit(withAudit(measurementUnit)), "/measurement-units", redirectAttributes);
    }

    @PostMapping("/measurement-units/{id}/delete")
    public String deleteMeasurementUnit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return save(() -> measurementUnitService.deleteMeasurementUnit(id), "/measurement-units", redirectAttributes);
    }

    @GetMapping("/parties")
    public String parties(Model model) {
        model.addAttribute("party", withAudit(new PartyModel()));
        model.addAttribute("parties", partyService.getParties());
        model.addAttribute("partyTypes", PartyType.values());
        return "parties";
    }

    @PostMapping("/parties")
    public String addParty(@ModelAttribute PartyModel party, RedirectAttributes redirectAttributes) {
        return save(() -> partyService.addParty(withAudit(party)), "/parties", redirectAttributes);
    }

    @PostMapping("/parties/{id}/delete")
    public String deleteParty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return save(() -> partyService.deleteParty(id), "/parties", redirectAttributes);
    }

    @GetMapping("/cash-in")
    public String cashIn(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date, Model model) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        CashInRequest request = withAudit(new CashInRequest());
        request.setTransactionDate(selectedDate);
        request.setMemoDate(selectedDate);

        model.addAttribute("date", selectedDate);
        model.addAttribute("cashIn", request);
        model.addAttribute("cashIns", cashInService.getCashInsByDate(selectedDate));
        addLookups(model);
        return "cash-in";
    }

    @PostMapping("/cash-in")
    public String addCashIn(@ModelAttribute CashInRequest cashIn, RedirectAttributes redirectAttributes) {
        return save(() -> cashInService.addCashIn(withAudit(cashIn)), "/cash-in?date=" + cashIn.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), redirectAttributes);
    }

    @PostMapping("/cash-in/{id}/delete")
    public String deleteCashIn(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return save(() -> cashInService.deleteCashIn(id), "/cash-in", redirectAttributes);
    }

    @GetMapping("/cash-out")
    public String cashOut(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date, Model model) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        CashOutRequest request = withAudit(new CashOutRequest());
        request.setTransactionDate(selectedDate);

        model.addAttribute("date", selectedDate);
        model.addAttribute("cashOut", request);
        model.addAttribute("cashOuts", cashOutService.getCashOutsByDate(selectedDate));
        addLookups(model);
        return "cash-out";
    }

    @PostMapping("/cash-out")
    public String addCashOut(@ModelAttribute CashOutRequest cashOut, RedirectAttributes redirectAttributes) {
        return save(() -> cashOutService.addCashOut(withAudit(cashOut)), "/cash-out?date=" + cashOut.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), redirectAttributes);
    }

    @PostMapping("/cash-out/{id}/delete")
    public String deleteCashOut(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return save(() -> cashOutService.deleteCashOut(id), "/cash-out", redirectAttributes);
    }

    @GetMapping("/sales")
    public String sales(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date, Model model) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        SalesRequest request = withAudit(new SalesRequest());
        request.setTransactionDate(selectedDate);
        request.setDiscount(0D);

        model.addAttribute("date", selectedDate);
        model.addAttribute("sale", request);
        model.addAttribute("sales", salesService.getSalesByDate(selectedDate));
        addLookups(model);
        return "sales";
    }

    @PostMapping("/sales")
    public String addSale(@ModelAttribute SalesRequest sale, RedirectAttributes redirectAttributes) {
        if (sale.getAmount() == null) {
            double count = sale.getCount() != null ? sale.getCount() : 0D;
            double price = sale.getPricePerUnit() != null ? sale.getPricePerUnit() : 0D;
            double discount = sale.getDiscount() != null ? sale.getDiscount() : 0D;
            sale.setAmount((count * price) - discount);
        }
        return save(() -> salesService.addSales(withAudit(sale)), "/sales?date=" + sale.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), redirectAttributes);
    }

    @PostMapping("/sales/{id}/delete")
    public String deleteSale(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return save(() -> salesService.deleteSales(id), "/sales", redirectAttributes);
    }

    @GetMapping("/receivables")
    public String receivables(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date, Model model) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        ReceivableRequest request = withAudit(new ReceivableRequest());
        request.setTransactionDate(selectedDate);

        model.addAttribute("date", selectedDate);
        model.addAttribute("receivable", request);
        model.addAttribute("receivables", receivableService.getReceivablesByDate(selectedDate));
        addLookups(model);
        return "receivables";
    }

    @PostMapping("/receivables")
    public String addReceivable(@ModelAttribute ReceivableRequest receivable, RedirectAttributes redirectAttributes) {
        return save(() -> receivableService.addReceivable(withAudit(receivable)), "/receivables?date=" + receivable.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), redirectAttributes);
    }

    @PostMapping("/receivables/{id}/delete")
    public String deleteReceivable(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        return save(() -> receivableService.deleteReceivable(id), "/receivables", redirectAttributes);
    }

    @GetMapping("/stock")
    public String stock(@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate date, Model model) {
        LocalDate selectedDate = date != null ? date : LocalDate.now();
        StockRequest request = withAudit(new StockRequest());
        request.setTransactionDate(selectedDate);

        model.addAttribute("date", selectedDate);
        model.addAttribute("stock", request);
        model.addAttribute("stocks", stockService.getStocksPerProductByDate(selectedDate));
        addLookups(model);
        return "stock";
    }

    @PostMapping("/stock")
    public String addStock(@ModelAttribute StockRequest stock, RedirectAttributes redirectAttributes) {
        return save(() -> stockService.addStock(withAudit(stock)), "/stock?date=" + stock.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), redirectAttributes);
    }

    private void addLookups(Model model) {
        model.addAttribute("banks", bankService.getBanks());
        model.addAttribute("products", productService.getProducts());
        model.addAttribute("parties", partyService.getParties());
    }

    private <T extends com.teamsits.automatix.models.common.CommonModel> T withAudit(T model) {
        if (model.getCreatedBy() == null) {
            model.setCreatedBy(SYSTEM_USER);
        }
        if (model.getUpdatedBy() == null) {
            model.setUpdatedBy(SYSTEM_USER);
        }
        return model;
    }

    private String save(Runnable action, String redirectTo, RedirectAttributes redirectAttributes) {
        try {
            action.run();
            redirectAttributes.addFlashAttribute("success", "Saved successfully.");
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("error", exception.getMessage());
        }
        return "redirect:" + redirectTo;
    }
}
