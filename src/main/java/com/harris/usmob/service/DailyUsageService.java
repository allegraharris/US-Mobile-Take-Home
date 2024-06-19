package com.harris.usmob.service;

import com.harris.usmob.dto.DailyUsageDTO;
import com.harris.usmob.entity.DailyUsage;
import com.harris.usmob.entity.User;
import com.harris.usmob.repository.DailyUsageRepository;
import com.harris.usmob.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Daily Usage
 */
@AllArgsConstructor
@Service
public class DailyUsageService {

    /**
     * Cycle Service
     */
    private final CycleService cycleService;
    /**
     * Daily Usage Repository
     */
    private final DailyUsageRepository dailyUsageRepository;
    /**
     * User Repository
     */
    private final UserRepository userRepository;

    /**
     * Adds a new daily usage to the collection
     * @param dailyUsage Daily Usage
     * @return DailyUsageDTO object
     */
    public DailyUsageDTO addDailyUsage(DailyUsage dailyUsage) {
        User user = userRepository.findById(dailyUsage.getUserId()).orElse(null);

        //Foreign key error or mdn mismatch
        if (user == null || !user.getMdn().equals(dailyUsage.getMdn())) {
            return null;
        }

        List<DailyUsage> oldDailyUsages = dailyUsageRepository.findByUserIdAndMdn(dailyUsage.getUserId(), dailyUsage.getMdn());

        for (DailyUsage oldDailyUsage : oldDailyUsages) {
            // Check if daily usage already exists
            if (oldDailyUsage.getUsageDate().equals(dailyUsage.getUsageDate())) {
                return null;
            }
        }
        DailyUsage savedDailyUsage = dailyUsageRepository.save(dailyUsage);
        return new DailyUsageDTO(savedDailyUsage.getUsageDate(), savedDailyUsage.getUsedInMb());
    }

    /**
     * Gets all Daily Usages
     * @return List of DailyUsage objects
     */
    public List<DailyUsage> getAllDailyUsages() {
        return dailyUsageRepository.findAll();
    }

    /**
     * Gets Daily Usage History for a User
     * @param userId User ID
     * @param mdn MDN
     * @return List of DailyUsageDTO objects
     */
    public List<DailyUsageDTO> getDailyUsageHistory(String userId, String mdn) {
        List<DailyUsage> dailyUsages = dailyUsageRepository.findByUserIdAndMdn(userId, mdn);

        //filter out for only most recent cycle
        List<Date> mostRecentDates = cycleService.getMostRecentCycle(userId, mdn);
        dailyUsages.removeIf(dailyUsage -> dailyUsage.getUsageDate().before(mostRecentDates.getFirst()) || dailyUsage.getUsageDate().after(mostRecentDates.get(1)));


        return dailyUsages.stream()
                .map(dailyUsage -> new DailyUsageDTO(dailyUsage.getUsageDate(), dailyUsage.getUsedInMb()))
                .collect(Collectors.toList());
    }

    /**
     * Deletes a Daily Usage from the collection
     * @param usageId Daily Usage ID
     * @return Boolean
     */
    public Boolean deleteDailyUsage(String usageId) {
        DailyUsage dailyUsage = dailyUsageRepository.findById(usageId).orElse(null);

        if (dailyUsage == null) { return false; }

        dailyUsageRepository.delete(dailyUsage);
        return true;
    }

    /**
     * Updates the used in Mb for a daily usage
     * @param usageDate Usage Date
     * @param mdn MDN
     * @param usedInMb Used in Mb
     * @return DailyUsageDTO object
     */
    public DailyUsageDTO updateUsedInMb(Date usageDate, String mdn, int usedInMb) {
        DailyUsage dailyUsage = dailyUsageRepository.findByUsageDateAndMdn(usageDate, mdn);

        if (dailyUsage == null) {
            return null;
        }

        dailyUsage.setUsedInMb(usedInMb);
        dailyUsageRepository.save(dailyUsage);

        return new DailyUsageDTO(dailyUsage.getUsageDate(), dailyUsage.getUsedInMb());
    }
}