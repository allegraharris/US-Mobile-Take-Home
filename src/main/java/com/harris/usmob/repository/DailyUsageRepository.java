package com.harris.usmob.repository;

import com.harris.usmob.entity.DailyUsage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Repository for Daily Usage
 */
public interface DailyUsageRepository extends MongoRepository<DailyUsage, String> {
    /**
     * Delete by User ID
     * @param userId User ID
     */
    void deleteByUserId(String userId);

    /**
     * Find by Usage Date and MDN
     * @param usageDate Usage Date
     * @param mdn MDN
     * @return DailyUsage object
     */
    DailyUsage findByUsageDateAndMdn(Date usageDate, String mdn);

    /**
     * Find by User ID and MDN
     * @param userId User ID
     * @param mdn MDN
     * @return List of DailyUsage objects
     */
    List<DailyUsage> findByUserIdAndMdn(String userId, String mdn);
}