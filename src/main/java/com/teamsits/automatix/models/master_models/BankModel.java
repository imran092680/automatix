package com.teamsits.automatix.models.master_models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.teamsits.automatix.entities.master_entity.Bank;
import com.teamsits.automatix.models.common.CommonModel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class BankModel extends CommonModel {
    private String name;
    private String prefix;

    public BankModel(Bank bank) {
        super(
                bank.getId(),
                bank.getVersion(),
                bank.getCreatedBy(),
                bank.getCreatedAt(),
                bank.getUpdatedBy(),
                bank.getUpdatedAt()
        );
        this.setName(bank.getName());
        this.setPrefix(bank.getPrefix());
    }
}

