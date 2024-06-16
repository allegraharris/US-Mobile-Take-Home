package com.harris.usmob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class DailyUsageDTO {
    private Date usageDate;
    private int usedInMb;
}

