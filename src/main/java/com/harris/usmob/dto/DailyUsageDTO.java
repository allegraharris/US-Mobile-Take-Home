package com.harris.usmob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object for Daily Usage
 */
@AllArgsConstructor
@Data
public class DailyUsageDTO {
    /**
     * Usage Date
     */
    private Date usageDate;
    /**
     * Usage in Mb
     */
    private int usedInMb;
}

