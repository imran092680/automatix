package com.teamsits.pbs.service.master_data_service;

import com.teamsits.pbs.entities.master_entity.Party;
import com.teamsits.pbs.models.master_models.PartyModel;
import com.teamsits.pbs.repository.master_data_repository.PartyRepo;
import com.teamsits.pbs.utils.ApplicationConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyService {
    private final PartyRepo partyRepo;

    public List<PartyModel> getParties() {
        return partyRepo.findPartiesWhereIsDeletedEqualsZero()
                .stream()
                .map(PartyModel::new)
                .collect(Collectors.toList());
    }

    public Page<PartyModel> getPartiesByPage(Integer offset, Integer pageSize, String field, Boolean ascending) {
        Pageable pageable = PageRequest.of(
                offset != null ? offset : 0,
                pageSize != null ? pageSize : 10,
                ascending == null || ascending ? Sort.Direction.ASC : Sort.Direction.DESC,
                StringUtils.isNotBlank(field) ? field : "id"
        );
        return partyRepo.findPageByIsDeletedEqualsZero(pageable).map(PartyModel::new);
    }

    public List<PartyModel> getPurchaseParties() {
        return partyRepo.findPurchasePartiesWhereIsDeletedEqualsZero()
                .stream()
                .map(PartyModel::new)
                .collect(Collectors.toList());
    }

    public List<PartyModel> getSalesParties() {
        return partyRepo.findSalesPartiesWhereIsDeletedEqualsZero()
                .stream()
                .map(PartyModel::new)
                .collect(Collectors.toList());
    }

    public Optional<PartyModel> getPartyById(Long id) {
        return partyRepo.findPartyByIdWhereIsDeletedEqualsZero(id).map(PartyModel::new);
    }

    public Optional<PartyModel> addParty(PartyModel partyModel) {
        boolean exists = partyRepo.findPartiesWhereIsDeletedEqualsZero()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(partyModel.getName()));
        if (exists) {
            throw new IllegalArgumentException(partyModel.getName() + " already exists.");
        }

        Party party = new Party(partyModel);
        partyRepo.save(party);

        return Optional.of(new PartyModel(party));
    }

    public void deleteParty(Long id) {
        if (partyRepo.existsById(id)) {
            Party party = partyRepo
                    .findPartyByIdWhereIsDeletedEqualsZero(id)
                    .orElseThrow(() -> new RuntimeException("PartyInfo not found."));

            party.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            partyRepo.save(party);
        }
    }

    public Optional<PartyModel> updateParty(PartyModel partyModel) {
        boolean exists = partyRepo.findPartiesWhereIsDeletedEqualsZero()
                .stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(partyModel.getName()));

        if (exists) {
            throw new IllegalArgumentException(partyModel.getName() + " already exists.");
        }

        return partyRepo.findById(partyModel.getId())
                .map((Party party) -> {
                    party.setPartyType(partyModel.getPartyType());
                    party.setName(partyModel.getName());
                    party.setAddress(partyModel.getAddress());
                    party.setPhoneNumber(partyModel.getPhoneNumber());

                    return party;
                })
                .map(partyRepo::save)
                .map(PartyModel::new);
    }
}
