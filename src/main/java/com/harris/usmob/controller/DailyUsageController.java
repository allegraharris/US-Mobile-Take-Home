package com.harris.usmob.controller;

import com.harris.usmob.dto.DailyUsageDTO;
import com.harris.usmob.entity.DailyUsage;
import com.harris.usmob.error.ErrorResponse;
import com.harris.usmob.service.DailyUsageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api/daily-usage")
public class DailyUsageController {

    private final DailyUsageService dailyUsageService;

    @PostMapping("/add")
    public ResponseEntity<Object> addDailyUsage(@RequestBody DailyUsage dailyUsage) {
        DailyUsageDTO d = dailyUsageService.addDailyUsage(dailyUsage);

        if(d == null) {
            ErrorResponse errorResponse = new ErrorResponse("Error adding daily usage. Please try again.");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(d, HttpStatus.CREATED);
    }

}