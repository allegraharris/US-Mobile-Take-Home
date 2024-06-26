package com.harris.usmob.controller;

import com.harris.usmob.dto.DailyUsageDTO;
import com.harris.usmob.entity.DailyUsage;
import com.harris.usmob.service.DailyUsageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Add a new daily usage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Daily usage added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DailyUsageDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Error adding daily usage",
                    content = @Content)

    })
    @PostMapping("/add")
    public ResponseEntity<Object> addDailyUsage(@RequestBody DailyUsage dailyUsage) {
        DailyUsageDTO d = dailyUsageService.addDailyUsage(dailyUsage);

        if (d == null) {
            return new ResponseEntity<>("Error adding daily usage", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(d, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a daily usage")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily usage deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Error deleting daily usage",
                    content = @Content)
    })
    @DeleteMapping("/delete/{usageId}")
    public ResponseEntity<Object> deleteDailyUsage(@PathVariable String usageId) {
        Boolean b = dailyUsageService.deleteDailyUsage(usageId);

        if (!b) {
            return new ResponseEntity<>("Daily usage does not exist.", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Daily usage deleted successfully.", HttpStatus.OK);
    }

    @Operation(summary = "Get all daily usage stored in the collection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily usage found successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DailyUsageDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No daily usages found",
                    content = @Content)

    })
    @GetMapping("/all")
    public ResponseEntity<Object> getAllDailyUsages() {
        List<DailyUsageDTO> d = dailyUsageService.getAllDailyUsages();

        if (d.isEmpty()) {
            return new ResponseEntity<>("No daily usages found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(d, HttpStatus.OK);
    }

    @Operation(summary = "Get daily usage history for a user and MDN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily usage history found successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DailyUsageDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No daily usage history found",
                    content = @Content)

    })
    @GetMapping("/history/{userId}/{mdn}")
    public ResponseEntity<Object> getDailyUsageHistory(@PathVariable String userId, @PathVariable String mdn) {
        List<DailyUsageDTO> dailyUsageHistory = dailyUsageService.getDailyUsageHistory(userId, mdn);

        if (dailyUsageHistory.isEmpty()) {
            return new ResponseEntity<>("No daily usage history found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(dailyUsageHistory, HttpStatus.OK);
    }

    @Operation(summary = "Update the daily usage in MB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily usage updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DailyUsageDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Error updating daily usage",
                    content = @Content)
    })
    @PatchMapping("/update/{usedInMb}")
    public ResponseEntity<Object> updateUsedInMb(@RequestBody DailyUsage dailyUsage, @PathVariable int usedInMb) {
        DailyUsageDTO d = dailyUsageService.updateUsedInMb(dailyUsage.getUsageDate(), dailyUsage.getMdn(), usedInMb);

        if (d == null) {
            return new ResponseEntity<>("Error updating daily usage", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(d, HttpStatus.OK);
    }

}