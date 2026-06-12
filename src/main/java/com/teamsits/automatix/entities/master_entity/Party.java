package com.teamsits.automatix.entities.master_entity;

import com.teamsits.automatix.entities.common.TenantAuditBase;
import com.teamsits.automatix.enums.PartyType;
import com.teamsits.automatix.models.master_models.PartyModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Party extends TenantAuditBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartyType partyType;

    @Column(nullable = false)
    private String name;

    private String address;
    private String phoneNumber;
//    private String contactPerson;
//    private Boolean isActive = true;

    public Party(PartyModel partyModel) {
        super(
                partyModel.getCreatedBy(),
                partyModel.getUpdatedBy()
        );
        this.setPartyType(partyModel.getPartyType());
        this.setName(partyModel.getName());
        this.setAddress(partyModel.getAddress());
        this.setAddress(partyModel.getAddress());
        this.setPhoneNumber(partyModel.getPhoneNumber());
//        this.setContactPerson(partyModel.getContactPerson());
    }
}
