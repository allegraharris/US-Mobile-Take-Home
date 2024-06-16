package com.harris.usmob.controller;

import com.harris.usmob.dto.DailyUsageDTO;
import com.harris.usmob.entity.DailyUsage;
import com.harris.usmob.service.DailyUsageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/daily-usage")
public class DailyUsageController {

    private final DailyUsageService dailyUsageService;

    @PostMapping("/add")
    public ResponseEntity<Object> addDailyUsage(@RequestBody DailyUsage dailyUsage) {
        DailyUsageDTO d = dailyUsageService.addDailyUsage(dailyUsage);

        if(d == null) {
            return new ResponseEntity<>("Error adding daily usage. Please try again.", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(d, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllDailyUsages() {
        List<DailyUsage> d = dailyUsageService.getAllDailyUsages();

        if(d.isEmpty()) {
            return new ResponseEntity<>("No daily usages found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(d, HttpStatus.OK);
    }

    @GetMapping("/history/{userId}/{mdn}")
    public ResponseEntity<Object> getDailyUsageHistory(@PathVariable String userId, @PathVariable String mdn) {
        List<DailyUsageDTO> dailyUsageHistory = dailyUsageService.getDailyUsageHistory(userId, mdn);

        if (dailyUsageHistory.isEmpty()) {
            return new ResponseEntity<>("No daily usage history found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(dailyUsageHistory, HttpStatus.OK);
    }

}