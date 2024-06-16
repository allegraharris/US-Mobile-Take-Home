package com.harris.usmob.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class CycleDTO {
    private String cycleId;
    private Date startDate;
    private Date endDate;
    private String userId;
    private String mdn;
}