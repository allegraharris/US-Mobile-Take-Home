package com.harris.usmob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for Cycle
 */
@AllArgsConstructor
@Data
public class CycleDTO {
    /**
     * Cycle ID
     */
    private String cycleId;
    /**
     * Start Date of the cycle
     */
    private Date startDate;
    /**
     * End Date of the cycle
     */
    private Date endDate;
}