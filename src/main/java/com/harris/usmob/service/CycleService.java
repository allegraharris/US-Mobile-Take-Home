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

/**
 * Service for Cycle
 */
@AllArgsConstructor
@Service
public class CycleService {

    /**
     * Cycle Repository
     */
    private final CycleRepository cycleRepository;
    /**
     * User Repository
     */
    private final UserRepository userRepository;

    /**
     * Adds a new cycle to the collection
     * @param cycle Cycle
     * @return CycleDTO object
     */
    public CycleDTO addCycle(Cycle cycle) {
        String userId = cycle.getUserId();
        String mdn = cycle.getMdn();

        User user = userRepository.findById(userId).orElse(null);

        //Foreign key error or mdn mismatch
        if(user == null || !Objects.equals(user.getMdn(), mdn)) {
            return null;
        }

        Date startDate = cycle.getStartDate();
        Date endDate = cycle.getEndDate();

        if (startDate.after(endDate)) {
            return null;
        }

        List<Cycle> oldCycles = cycleRepository.findByUserIdAndMdn(userId, mdn);

        if (!oldCycles.isEmpty()) {
            for (Cycle oldCycle : oldCycles) {

                // Cycle overlaps with any previous cycle
                // Check logic on this
                if (afterOrEquals(startDate, oldCycle.getStartDate()) && beforeOrEquals(startDate, oldCycle.getEndDate())) {
                    return null;
                }

                // Check if cycle already exists
                if (oldCycle.getStartDate().equals(startDate) && oldCycle.getEndDate().equals(endDate)) {
                    return null;
                }
            }
        }

        Cycle savedCycle = cycleRepository.save(cycle);
        return new CycleDTO(savedCycle.getId(), savedCycle.getStartDate(), savedCycle.getEndDate());
    }

    /**
     * Check if Date 1 is after or equals Date 2
     * @param date1 Date 1
     * @param date2 Date 2
     * @return Boolean
     */
    private Boolean afterOrEquals(Date date1, Date date2) {
        return date1.after(date2) || date1.equals(date2);
    }

    /**
     * Check if Date 1 is before or equals Date 2
     * @param date1 Date 1
     * @param date2 Date 2
     * @return Boolean
     */
    private Boolean beforeOrEquals(Date date1, Date date2) {
        return date1.before(date2) || date1.equals(date2);
    }

    /**
     * Deletes a cycle from the collection
     * @param cycleId Cycle ID
     * @return Boolean
     */
    public Boolean deleteCycle(String cycleId) {
        Optional<Cycle> cycle = cycleRepository.findById(cycleId);

        if (cycle.isEmpty()) {
            return false;
        }

        cycleRepository.deleteById(cycleId);
        return true;
    }

    /**
     * Get All Cycles
     * @return List of CycleDTO objects
     */
    public List<CycleDTO> getAllCycles() {
        List<Cycle> cycles = cycleRepository.findAll();
        return cycles.stream()
                .map(cycle -> new CycleDTO(cycle.getId(), cycle.getStartDate(), cycle.getEndDate()))
                .collect(Collectors.toList());
    }

    /**
     * Gets the cycle history for a user and MDN
     * @param userId User ID
     * @param mdn MDN
     * @return List of CycleDTO objects
     */
    public List<CycleDTO> getCycleHistory(String userId, String mdn) {
        List<Cycle> cycles = cycleRepository.findByUserIdAndMdn(userId, mdn);
        return cycles.stream()
                .map(cycle -> new CycleDTO(cycle.getId(), cycle.getStartDate(), cycle.getEndDate()))
                .collect(Collectors.toList());
    }

    /**
     * Gets Most Recent Cycle dates for a user and MDN
     *
     * Helper method for getDailyUsageHistory
     *
     * @param userId User ID
     * @param mdn MDN
     * @return List of Date objects
     */
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

