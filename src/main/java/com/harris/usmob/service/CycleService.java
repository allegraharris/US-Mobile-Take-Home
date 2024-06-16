package com.harris.usmob.service;

import com.harris.usmob.dto.CycleDTO;
import com.harris.usmob.entity.Cycle;
import com.harris.usmob.entity.User;
import com.harris.usmob.repository.CycleRepository;
import com.harris.usmob.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CycleService {

    private final CycleRepository cycleRepository;
    private final UserRepository userRepository;

    public List<CycleDTO> getCycleHistory(String userId, String mdn) {
        List<Cycle> cycles = cycleRepository.findByUserIdAndMdn(userId, mdn);
        return cycles.stream()
                .map(cycle -> new CycleDTO(cycle.getId(), cycle.getStartDate(), cycle.getEndDate(), cycle.getUserId(), cycle.getMdn()))
                .collect(Collectors.toList());
    }

    public CycleDTO addCycle(Cycle cycle) {
        //Foreign key error
        if(!idExists(cycle.getUserId())) {
            return null;
        }

        List<Cycle> oldCycles = cycleRepository.findByUserId(cycle.getUserId());

        if(!oldCycles.isEmpty()) {

            //Check if mdn is different
            if(!Objects.equals(cycle.getMdn(), oldCycles.getFirst().getMdn())) {
                return null;
            }

            for (Cycle oldCycle : oldCycles) {
                // Check if cycle already exists
                if (oldCycle.getStartDate().equals(cycle.getStartDate()) && oldCycle.getEndDate().equals(cycle.getEndDate()) && oldCycle.getMdn().equals(cycle.getMdn())) {
                    return null;
                }
            }
        }

        Cycle savedCycle = cycleRepository.save(cycle);
        return new CycleDTO(savedCycle.getId(), savedCycle.getStartDate(), savedCycle.getEndDate(), savedCycle.getUserId(), savedCycle.getMdn());
    }

    private Boolean idExists(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent();
    }

    public List<CycleDTO> getAllCycles() {
        List<Cycle> cycles = cycleRepository.findAll();
        return cycles.stream()
                .map(cycle -> new CycleDTO(cycle.getId(), cycle.getStartDate(), cycle.getEndDate(), cycle.getUserId(), cycle.getMdn()))
                .collect(Collectors.toList());
    }

    public List<Date> getMostRecentCycle(String userId, String mdn) {
        List<Cycle> cycles = cycleRepository.findByUserIdAndMdn(userId, mdn);

        if (cycles.isEmpty()) {
            return null;
        }

        Date latestEndDate = null;
        Date latestStartDate = null;

        for (Cycle cycle : cycles) {
            if (latestEndDate == null || cycle.getEndDate().after(latestEndDate)) {
                latestEndDate = cycle.getEndDate();
                latestStartDate = cycle.getStartDate();
            }
        }

        List<Date> mostRecentCycle = new ArrayList<>();
        mostRecentCycle.add(latestStartDate);
        mostRecentCycle.add(latestEndDate);

        return mostRecentCycle;
    }
}

