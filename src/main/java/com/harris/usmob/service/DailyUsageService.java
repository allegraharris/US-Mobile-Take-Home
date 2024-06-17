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
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DailyUsageService {

    private final CycleService cycleService;
    private final DailyUsageRepository dailyUsageRepository;
    private final UserRepository userRepository;

    public DailyUsageDTO addDailyUsage(DailyUsage dailyUsage) {
        if (!idExists(dailyUsage.getUserId())) {
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

    //Maybe switch back to DTO? Output isn't specified tho..
    public List<DailyUsage> getAllDailyUsages() {
        return dailyUsageRepository.findAll();
    }

    public List<DailyUsageDTO> getDailyUsageHistory(String userId, String mdn) {
        List<DailyUsage> dailyUsages = dailyUsageRepository.findByUserIdAndMdn(userId, mdn);

        //filter out for only most recent cycle
        List<Date> mostRecentDates = cycleService.getMostRecentCycle(userId, mdn);
        dailyUsages.removeIf(dailyUsage -> dailyUsage.getUsageDate().before(mostRecentDates.getFirst()) || dailyUsage.getUsageDate().after(mostRecentDates.get(1)));


        return dailyUsages.stream()
                .map(dailyUsage -> new DailyUsageDTO(dailyUsage.getUsageDate(), dailyUsage.getUsedInMb()))
                .collect(Collectors.toList());
    }

    private Boolean idExists(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent();
    }
}