package com.harris.usmob.controller;

import com.harris.usmob.dto.CycleDTO;
import com.harris.usmob.entity.Cycle;
import com.harris.usmob.service.CycleService;
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

    @GetMapping("/cycle-history/{userId}/{mdn}")
    public ResponseEntity<Object> getCycleHistory(@PathVariable String userId, @PathVariable String mdn) {
        List<CycleDTO> cycleHistory = cycleService.getCycleHistory(userId, mdn);

        if (cycleHistory.isEmpty()) {
            return new ResponseEntity<>("No cycle history found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cycleHistory, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addCycle(@RequestBody Cycle cycle) {
        CycleDTO c = cycleService.addCycle(cycle);

        if (c == null) {
            return new ResponseEntity<>("Error adding cycle", HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllCycles() {
        List<CycleDTO> cycles = cycleService.getAllCycles();

        if (cycles.isEmpty()) {
            return new ResponseEntity<>("No cycles found.", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(cycles, HttpStatus.OK);
    }
}
