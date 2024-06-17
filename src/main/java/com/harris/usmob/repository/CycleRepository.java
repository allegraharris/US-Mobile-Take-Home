package com.harris.usmob.repository;

import com.harris.usmob.entity.Cycle;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CycleRepository extends MongoRepository<Cycle, String> {
    List<Cycle> findByUserIdAndMdn(String userId, String mdn);
    List<Cycle> findByUserId(String userId);
    void deleteByUserId(String userId);

}