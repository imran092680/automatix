package com.teamsits.automatix.entities;

import com.teamsits.automatix.entities.common.CommonColumn;
import com.teamsits.automatix.enums.SubscriptionType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "organization")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends CommonColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubscriptionType subscriptionType = SubscriptionType.UNLIMITED;

    @Column
    private Integer maxUsers;

    @Column(columnDefinition = "TEXT")
    private String agreementNotes;

    @Column(nullable = false)
    private Boolean isActive = true;

    public Organization(Long createdBy, Long updatedBy) {
        super(createdBy, updatedBy);
    }
}
