package com.harris.usmob.repository;

import com.harris.usmob.entity.Cycle;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository for Cycle
 */
public interface CycleRepository extends MongoRepository<Cycle, String> {
    /**
     * Delete by User ID
     * @param userId User ID
     */
    void deleteByUserId(String userId);

    /**
     * Find by User ID and MDN
     * @param userId User ID
     * @param mdn MDN
     * @return List of Cycle objects
     */
    List<Cycle> findByUserIdAndMdn(String userId, String mdn);

}