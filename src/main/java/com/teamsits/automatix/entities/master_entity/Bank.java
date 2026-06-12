package com.teamsits.automatix.entities.master_entity;

import com.teamsits.automatix.entities.common.CommonColumn;
import com.teamsits.automatix.models.master_models.BankModel;
import com.teamsits.automatix.utils.ApplicationConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank extends CommonColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(columnDefinition = "VARCHAR(40)")
    private String prefix = ApplicationConstant.DOMAIN_STATUS_EMPTY_STRING;

    public Bank(BankModel bankModel) {
        super(
                bankModel.getCreatedBy(),
                bankModel.getUpdatedBy()
        );
        this.setName(bankModel.getName());

//        if (bankModel.getPrefix() != null && !bankModel.getPrefix().isEmpty()) {
//            this.setPrefix(bankModel.getPrefix());
//        }
    }
}
