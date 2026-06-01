package com.teamsits.pbs.service;

import com.teamsits.pbs.entities.Receivable;
import com.teamsits.pbs.entities.master_entity.Party;
import com.teamsits.pbs.models.receivable.ReceivableRequest;
import com.teamsits.pbs.models.receivable.ReceivableResponse;
import com.teamsits.pbs.repository.ReceivableRepo;
import com.teamsits.pbs.repository.master_data_repository.PartyRepo;
import com.teamsits.pbs.utils.ApplicationConstant;
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

    public List<ReceivableResponse> getReceivablesByDate(LocalDate date) {
        return receivableRepo.findReceivablesByDateWhereIsDeletedEqualsZero(date)
                .stream()
                .map(ReceivableResponse::new)
                .collect(Collectors.toList());
    }

    public Optional<ReceivableResponse> getReceivableById(Long id) {
        return receivableRepo.findReceivableByIdWhereIsDeletedEqualsZero(id).map(ReceivableResponse::new);
    }

    public Optional<ReceivableResponse> addReceivable(ReceivableRequest receivableRequest) {
        Party party = null;

        if (receivableRequest.getPartyId() != null) {
            party = partyRepo.findPartyByIdWhereIsDeletedEqualsZero(receivableRequest.getPartyId())
                    .orElseThrow(() -> new RuntimeException("Party by ID : " + receivableRequest.getPartyId() + " not found."));
        }

        Receivable receivable = new Receivable(receivableRequest, party);

        receivableRepo.save(receivable);

        return Optional.of(new ReceivableResponse(receivable));
    }

    public void deleteReceivable(Long id) {
        if (receivableRepo.existsById(id)) {
            Receivable receivable = receivableRepo
                    .findReceivableByIdWhereIsDeletedEqualsZero(id)
                    .orElseThrow(() -> new RuntimeException("ReceivableFromParty not found."));

            receivable.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            receivableRepo.save(receivable);
        }
    }

    public Optional<ReceivableResponse> updateReceivable(ReceivableRequest receivableRequest) {
        Party party = partyRepo.findById(receivableRequest.getPartyId())
                .orElseThrow(() -> new EntityNotFoundException("Party not Found"));

        return receivableRepo.findReceivableByIdWhereIsDeletedEqualsZero(receivableRequest.getId())
                .map((Receivable receivable) -> {
                    receivable.setParty(party);
                    receivable.setAmount(receivableRequest.getAmount());
                    receivable.setTransactionDate(receivableRequest.getTransactionDate());

                    return receivable;
                })
                .map(receivableRepo::save)
                .map(ReceivableResponse::new);
    }
}
