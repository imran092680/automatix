package com.teamsits.pbs.controller.master_controller;

import com.teamsits.pbs.models.master_models.PartyModel;
import com.teamsits.pbs.service.master_data_service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/party")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PartyController {
    public final PartyService partyService;

    @GetMapping()
    public ResponseEntity<List<PartyModel>> getParties() {
        return ResponseEntity.ok(partyService.getParties());
    }

    @GetMapping("page")
    public Page<PartyModel> getPartiesByPage(
            @PathParam("offset") Integer offset,
            @PathParam("pageSize") Integer pageSize,
            @PathParam("field") String field,
            @PathParam("ascending") Boolean ascending
    ) {
        return partyService.getPartiesByPage(offset, pageSize, field, ascending);
    }

    @GetMapping("purchase_parties")
    public ResponseEntity<List<PartyModel>> getPurchaseParties() {
        try {
            return ResponseEntity.ok(partyService.getPurchaseParties());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("sales_parties")
    public ResponseEntity<List<PartyModel>> getSalesParties() {
        try {
            return ResponseEntity.ok(partyService.getSalesParties());
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Optional<PartyModel>> getPartyById(@PathVariable Long id) {
        return ResponseEntity.ok(partyService.getPartyById(id));
    }

    @PostMapping()
    public ResponseEntity<Optional<PartyModel>> addParty(@RequestBody PartyModel partyModel) {
        return ResponseEntity.ok(partyService.addParty(partyModel));
    }

    @DeleteMapping("{id}")
    public void deleteParty(@PathVariable Long id) {
        partyService.deleteParty(id);
    }

    @PutMapping()
    public ResponseEntity<Optional<PartyModel>> updateProduct(@RequestBody PartyModel partyModel) {
        return ResponseEntity.ok(partyService.updateParty(partyModel));
    }
}
