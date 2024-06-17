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
        String userId = cycle.getUserId();
        String mdn = cycle.getMdn();

        //Foreign key error
        if(!idExists(userId)) {
            return null;
        }

        Date startDate = cycle.getStartDate();
        Date endDate = cycle.getEndDate();

        if(startDate.after(endDate)) {
            return null;
        }

        List<Cycle> oldCycles = cycleRepository.findByUserId(userId);

        if(!oldCycles.isEmpty()) {

            //Check if mdn is different
            if(!Objects.equals(mdn, oldCycles.getFirst().getMdn())) {
                return null;
            }

            for (Cycle oldCycle : oldCycles) {

                // Cycle overlaps with any previous cycle
                // Check logic on this
                if(afterOrEquals(startDate, oldCycle.getStartDate()) && beforeOrEquals(startDate, oldCycle.getEndDate())) {
                    return null;
                }

                // Check if cycle already exists
                if (oldCycle.getStartDate().equals(startDate) && oldCycle.getEndDate().equals(endDate)) {
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

    private Boolean afterOrEquals(Date date1, Date date2) {
        return date1.after(date2) || date1.equals(date2);
    }

    private Boolean beforeOrEquals(Date date1, Date date2) {
        return date1.before(date2) || date1.equals(date2);
    }
}

