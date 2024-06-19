package com.harris.usmob.service;

import com.harris.usmob.dto.CycleDTO;
import com.harris.usmob.entity.Cycle;
import com.harris.usmob.entity.User;
import com.harris.usmob.repository.CycleRepository;
import com.harris.usmob.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class CycleServiceTest {

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private UserRepository userRepository;

    private CycleService cycleService;

    @BeforeEach
    void setUp() {
        cycleRepository.deleteAll();
        userRepository.deleteAll();
        cycleService = new CycleService(cycleRepository, userRepository);
    }

    @AfterEach
    void tearDown() {
        cycleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testAddCycle() {
        User user = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);

        Cycle cycle = new Cycle("cycle-id-1", "2024600871", new Date(), new Date(), "userId1");
        CycleDTO addedCycle = cycleService.addCycle(cycle);

        assertNotNull(addedCycle);
        assertEquals("cycle-id-1", addedCycle.getCycleId());
        assertEquals(user.getId(), cycle.getUserId());
        assertEquals(user.getMdn(), cycle.getMdn());
        assertEquals(cycle.getStartDate(), addedCycle.getStartDate());
        assertEquals(cycle.getEndDate(), addedCycle.getEndDate());
    }

    @Test
    void testAddCycle_WhenUserNotFound() {
        Cycle cycle = new Cycle("cycle-id-1", "2024600871", new Date(), new Date(), "nonexistent-user-id");

        CycleDTO addedCycle = cycleService.addCycle(cycle);

        assertNull(addedCycle);
    }

    @Test
    void testAddCycle_WhenMdnMismatch() {
        User user = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);

        Cycle cycle = new Cycle("cycle-id-1", "2024600872", new Date(), new Date(), "usedId1");
        CycleDTO addedCycle = cycleService.addCycle(cycle);

        assertNull(addedCycle);
    }

    @Test
    void testAddCycle_WhenStartDateAfterEndDate() {
        User user = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);

        Date futureDate = new Date(System.currentTimeMillis() + 86400000); // One day in future
        Date pastDate = new Date(System.currentTimeMillis() - 86400000);   // One day in past

        Cycle cycle = new Cycle("cycle-id-1", "userId1", futureDate, pastDate, "2024600871");

        CycleDTO addedCycle = cycleService.addCycle(cycle);

        assertNull(addedCycle);
    }

    @Test
    void testAddCycle_WhenCycleOverlaps() {
        User user = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);

        Date startDate1 = new Date(System.currentTimeMillis() - 86400000);   // One day ago
        Date endDate1 = new Date();
        Cycle cycle1 = new Cycle("cycle-id-1", "2024600871", startDate1, endDate1, "userId1");
        cycleRepository.save(cycle1);

        Date startDate2 = new Date(System.currentTimeMillis() - 43200000);   // 12 hours ago
        Date endDate2 = new Date(System.currentTimeMillis() + 43200000);     // 12 hours in future
        Cycle cycle2 = new Cycle("cycle-id-2", "2024600871", startDate2, endDate2, "userId1");
        CycleDTO addedCycle = cycleService.addCycle(cycle2);

        assertNull(addedCycle);
    }

    @Test
    void testDeleteCycle() {
        User user = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);

        Cycle cycle = new Cycle("cycle-id-1", "2024600871", new Date(), new Date(), "userId1");
        cycleRepository.save(cycle);

        boolean result = cycleService.deleteCycle("cycle-id-1");

        assertTrue(result);
        assertFalse(cycleRepository.findById("cycle-id-1").isPresent());
    }

    @Test
    void testDeleteCycle_WhenCycleNotFound() {
        boolean result = cycleService.deleteCycle("nonexistent-cycle-id");
        assertFalse(result);
    }

    @Test
    void testGetAllCycles() {
        User user = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);

        Cycle cycle1 = new Cycle("cycle-id-1", "2024600871", new Date(), new Date(), "userId1");
        cycleRepository.save(cycle1);

        Cycle cycle2 = new Cycle("cycle-id-2", "2024600871", new Date(), new Date(), "userId1");
        cycleRepository.save(cycle2);

        List<CycleDTO> allCycles = cycleService.getAllCycles();

        assertEquals(2, allCycles.size());
    }

    @Test
    void testGetCycleHistory() {
        User user = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);
        user = new User("userId2", "2024600872", "Jane", "Doe", "jane@doe.com", "password");
        userRepository.save(user);

        List<Cycle> cycles = new ArrayList<>();

        Cycle cycle1 = new Cycle("cycle-id-1", "2024600871", new Date(), new Date(), "userId1");
        cycleRepository.save(cycle1);
        cycles.add(cycle1);

        Cycle cycle2 = new Cycle("cycle-id-2", "2024600871", new Date(), new Date(), "userId1");
        cycleRepository.save(cycle2);
        cycles.add(cycle2);

        Cycle cycle3 = new Cycle("cycle-id-3", "2024600872", new Date(), new Date(), "userId2");
        cycleRepository.save(cycle3);

        List<CycleDTO> cycleHistory = cycleService.getCycleHistory("userId1", "2024600871");

        assertEquals(2, cycleHistory.size());
        assertEquals(cycles.getFirst().getId(), cycleHistory.getFirst().getCycleId());
        assertEquals(cycles.get(1).getId(), cycleHistory.get(1).getCycleId());
    }

    @Test
    void testGetCycleHistory_WhenNoCyclesFound() {
        List<CycleDTO> cycleHistory = cycleService.getCycleHistory("nonexistent-user-id", "2024600871");
        assertTrue(cycleHistory.isEmpty());
    }

    @Test
    void testGetMostRecentCycle() {
        User user = new User("userId1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);

        Date startDate1 = new Date(System.currentTimeMillis() - 86400000);   // One day ago
        Date endDate1 = new Date();
        Cycle cycle1 = new Cycle("cycle-id-1", "2024600871", startDate1, endDate1, "userId1");
        cycleRepository.save(cycle1);

        Date startDate2 = new Date(System.currentTimeMillis() - 43200000);   // 12 hours ago
        Date endDate2 = new Date(System.currentTimeMillis() + 43200000);     // 12 hours in future
        Cycle cycle2 = new Cycle("cycle-id-2", "2024600871", startDate2, endDate2, "userId1");
        cycleRepository.save(cycle2);

        List<Date> mostRecentCycle = cycleService.getMostRecentCycle("userId1", "2024600871");

        assertNotNull(mostRecentCycle);
        assertEquals(startDate2, mostRecentCycle.get(0));
        assertEquals(endDate2, mostRecentCycle.get(1));
    }

    @Test
    void testGetMostRecentCycle_WhenNoCyclesFound() {
        List<Date> mostRecentCycle = cycleService.getMostRecentCycle("nonexistent-user-id", "2024600871");
        assertNull(mostRecentCycle);
    }
}
