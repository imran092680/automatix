package com.teamsits.automatix.models.master_models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamsits.automatix.entities.master_entity.Party;
import com.teamsits.automatix.enums.PartyType;
import com.teamsits.automatix.models.common.CommonModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartyModel extends CommonModel {
    private String name;
    private PartyType partyType;
    private String address;
    private String phoneNumber;
//    private String contactPerson;

    public PartyModel(Party party) {
        super(
                party.getId(),
                party.getVersion(),
                party.getCreatedBy(),
                party.getCreatedAt(),
                party.getUpdatedBy(),
                party.getUpdatedAt()
        );
        this.setPartyType(party.getPartyType());
        this.setName(party.getName());
        this.setAddress(party.getAddress());
        this.setPhoneNumber(party.getPhoneNumber());
//        this.setContactPerson(party.getContactPerson());
    }
}
