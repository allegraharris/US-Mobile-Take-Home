package com.harris.usmob.repository;

import com.harris.usmob.entity.DailyUsage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DailyUsageRepository extends MongoRepository<DailyUsage, String> {
    List<DailyUsage> findByUserIdAndMdn(String userId, String mdn);
    void deleteByUserId(String userId);
}