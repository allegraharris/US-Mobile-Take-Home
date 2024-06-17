package com.harris.usmob.controller;

import com.harris.usmob.dto.CycleDTO;
import com.harris.usmob.entity.Cycle;
import com.harris.usmob.service.CycleService;
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
@RequestMapping("api/cycle")
public class CycleController {

    private final CycleService cycleService;

    @Operation(summary = "Add a new cycle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cycle added successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CycleDTO.class))}),
            @ApiResponse(responseCode = "409", description = "Error adding cycle",
                    content = @Content)
    })
    @PostMapping("/add")
    public ResponseEntity<Object> addCycle(@RequestBody Cycle cycle) {
        CycleDTO c = cycleService.addCycle(cycle);

        if (c == null) {
            return new ResponseEntity<>("Error adding cycle", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all cycles stored in the collection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cycles found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CycleDTO.class))}),
            @ApiResponse(responseCode = "404", description = "No cycles found",
                    content = @Content)
    })
    @GetMapping("/all")
    public ResponseEntity<Object> getAllCycles() {
        List<CycleDTO> cycles = cycleService.getAllCycles();

        if (cycles.isEmpty()) {
            return new ResponseEntity<>("No cycles found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cycles, HttpStatus.OK);
    }

    @Operation(summary = "Get cycle history for a user and MDN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cycle created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CycleDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Error adding cycle",
                    content = @Content)
    })
    @GetMapping("/history/{userId}/{mdn}")
    public ResponseEntity<Object> getCycleHistory(@PathVariable String userId, @PathVariable String mdn) {
        List<CycleDTO> cycleHistory = cycleService.getCycleHistory(userId, mdn);

        if (cycleHistory.isEmpty()) {
            return new ResponseEntity<>("No cycle history found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cycleHistory, HttpStatus.OK);
    }

}
