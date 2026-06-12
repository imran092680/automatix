package com.teamsits.automatix.service;

import com.teamsits.automatix.entities.Receivable;
import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.models.receivable.ReceivableRequest;
import com.teamsits.automatix.models.receivable.ReceivableResponse;
import com.teamsits.automatix.repository.ReceivableRepo;
import com.teamsits.automatix.repository.master_data_repository.PartyRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceivableService {
    private final ReceivableRepo receivableRepo;
    private final PartyRepo partyRepo;
    private final SecurityUtils securityUtils;

    public List<ReceivableResponse> getReceivablesByDate(LocalDate date) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return receivableRepo.findReceivablesByDateWhereIsDeletedEqualsZero(orgId, date)
                .stream()
                .map(ReceivableResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<ReceivableResponse> getReceivableById(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return receivableRepo.findReceivableByIdWhereIsDeletedEqualsZero(orgId, id).map(ReceivableResponse::new);
    }

    public Optional<ReceivableResponse> addReceivable(ReceivableRequest receivableRequest) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        Long userId = securityUtils.getCurrentUserId();

        Party party = null;
        if (receivableRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(orgId, receivableRequest.getPartyId())
                    .orElseThrow(() -> new RuntimeException("Party by ID : " + receivableRequest.getPartyId() + " not found."));
        }

        Receivable receivable = new Receivable(receivableRequest, party);
        receivable.setOrganization(securityUtils.getCurrentOrganization());
        receivable.setCreatedBy(userId);
        receivable.setUpdatedBy(userId);
        receivableRepo.save(receivable);

        return Optional.of(new ReceivableResponse(receivable));
    }

    public void deleteReceivable(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        if (receivableRepo.existsById(id)) {
            Receivable receivable = receivableRepo
                    .findReceivableByIdWhereIsDeletedEqualsZero(orgId, id)
                    .orElseThrow(() -> new RuntimeException("Receivable not found."));

            receivable.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            receivableRepo.save(receivable);
        }
    }

    public Optional<ReceivableResponse> updateReceivable(ReceivableRequest receivableRequest) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        Party party = partyRepo.findById(receivableRequest.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException("Party not Found"));

        return receivableRepo.findReceivableByIdWhereIsDeletedEqualsZero(orgId, receivableRequest.getId())
                .map((Receivable receivable) -> {
                    receivable.setParty(party);
                    receivable.setAmount(receivableRequest.getAmount());
                    receivable.setTransactionDate(receivableRequest.getTransactionDate());
                    receivable.setUpdatedBy(securityUtils.getCurrentUserId());
                    return receivable;
                })
                .map(receivableRepo::save)
                .map(ReceivableResponse::new);
    }
}
