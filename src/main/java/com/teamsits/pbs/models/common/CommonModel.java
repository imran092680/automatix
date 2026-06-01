package com.teamsits.pbs.models.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Calendar;

/**
 * @author kazialimran.hussen
 * @since 13/04/2022
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonModel {
    private Long id;
    private Integer version;
    private Long createdBy;
    private Calendar createdAt;
    private Long updatedBy;
    private Calendar updatedAt;
}
