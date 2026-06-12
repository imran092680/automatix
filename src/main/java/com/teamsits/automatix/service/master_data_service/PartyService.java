package com.teamsits.automatix.service.master_data_service;

import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.models.master_models.PartyModel;
import com.teamsits.automatix.repository.master_data_repository.PartyRepo;
import com.teamsits.automatix.utils.ApplicationConstant;
import com.teamsits.automatix.utils.SecurityUtils;
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
    private final SecurityUtils securityUtils;

    public List<PartyModel> getParties() {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return partyRepo.findPartiesWhereIsDeletedEqualsZero(orgId)
                .stream()
                .map(PartyModel::new)
                .collect(Collectors.toList());
    }

    public Page<PartyModel> getPartiesByPage(Integer offset, Integer pageSize, String field, Boolean ascending) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        Pageable pageable = PageRequest.of(
                offset != null ? offset : 0,
                pageSize != null ? pageSize : 10,
                ascending == null || ascending ? Sort.Direction.ASC : Sort.Direction.DESC,
                StringUtils.isNotBlank(field) ? field : "id"
        );
        return partyRepo.findPageByIsDeletedEqualsZero(orgId, pageable).map(PartyModel::new);
    }

    public List<PartyModel> getPurchaseParties() {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return partyRepo.findPurchasePartiesWhereIsDeletedEqualsZero(orgId)
                .stream()
                .map(PartyModel::new)
                .collect(Collectors.toList());
    }

    public List<PartyModel> getSalesParties() {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return partyRepo.findSalesPartiesWhereIsDeletedEqualsZero(orgId)
                .stream()
                .map(PartyModel::new)
                .collect(Collectors.toList());
    }

    public Optional<PartyModel> getPartyById(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        return partyRepo.findPartyByIdWhereIsDeletedEqualsZero(orgId, id).map(PartyModel::new);
    }

    public Optional<PartyModel> addParty(PartyModel partyModel) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        if (partyRepo.existsByOrganizationIdAndName(orgId, partyModel.getName())) {
            throw new IllegalArgumentException(partyModel.getName() + " already exists.");
        }

        Party party = new Party(partyModel);
        party.setOrganization(securityUtils.getCurrentOrganization());
        party.setCreatedBy(securityUtils.getCurrentUserId());
        party.setUpdatedBy(securityUtils.getCurrentUserId());
        partyRepo.save(party);

        return Optional.of(new PartyModel(party));
    }

    public void deleteParty(Long id) {
        Long orgId = securityUtils.getCurrentOrganizationId();
        if (partyRepo.existsById(id)) {
            Party party = partyRepo
                    .findPartyByIdWhereIsDeletedEqualsZero(orgId, id)
                    .orElseThrow(() -> new RuntimeException("Party not found."));

            party.setIsDeleted(ApplicationConstant.DOMAIN_STATUS_ONE);
            partyRepo.save(party);
        }
    }

    public Optional<PartyModel> updateParty(PartyModel partyModel) {
        Long orgId = securityUtils.getCurrentOrganizationId();

        if (partyRepo.existsByOrganizationIdAndName(orgId, partyModel.getName())) {
            throw new IllegalArgumentException(partyModel.getName() + " already exists.");
        }

        return partyRepo.findById(partyModel.getId())
                .map((Party party) -> {
                    party.setPartyType(partyModel.getPartyType());
                    party.setName(partyModel.getName());
                    party.setAddress(partyModel.getAddress());
                    party.setPhoneNumber(partyModel.getPhoneNumber());
                    party.setUpdatedBy(securityUtils.getCurrentUserId());
                    return party;
                })
                .map(partyRepo::save)
                .map(PartyModel::new);
    }
}
