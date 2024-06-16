package com.harris.usmob.service;

import com.harris.usmob.dto.DailyUsageDTO;
import com.harris.usmob.entity.DailyUsage;
import com.harris.usmob.entity.User;
import com.harris.usmob.repository.DailyUsageRepository;
import com.harris.usmob.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class DailyUsageService {

    private final UserRepository userRepository;
    private final DailyUsageRepository dailyUsageRepository;

    public List<DailyUsageDTO> getDailyUsageHistory(String userId, String mdn) {
        List<DailyUsage> dailyUsages = dailyUsageRepository.findByUserIdAndMdn(userId, mdn);
        return dailyUsages.stream()
                .map(dailyUsage -> new DailyUsageDTO(dailyUsage.getUsageDate(), dailyUsage.getUsedInMb()))
                .collect(Collectors.toList());
    }

    public DailyUsageDTO addDailyUsage(DailyUsage dailyUsage) {
        if(!idExists(dailyUsage.getUserId())) {
            return null;
        }

        List<DailyUsage> oldDailyUsages = dailyUsageRepository.findByUserIdAndMdn(dailyUsage.getUserId(), dailyUsage.getMdn());
        DailyUsage savedDailyUsage = dailyUsageRepository.save(dailyUsage);
        return new DailyUsageDTO(savedDailyUsage.getUsageDate(), savedDailyUsage.getUsedInMb());
    }

    private Boolean idExists(String userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent();
    }
}