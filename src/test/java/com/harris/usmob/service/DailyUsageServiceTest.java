package com.harris.usmob.service;

import com.harris.usmob.dto.DailyUsageDTO;
import com.harris.usmob.entity.Cycle;
import com.harris.usmob.entity.DailyUsage;
import com.harris.usmob.entity.User;
import com.harris.usmob.repository.CycleRepository;
import com.harris.usmob.repository.DailyUsageRepository;
import com.harris.usmob.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DailyUsageService class.
 */
@DataMongoTest
public class DailyUsageServiceTest {

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private DailyUsageRepository dailyUsageRepository;

    private DailyUsageService dailyUsageService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Set up the test environment.
     */
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        dailyUsageRepository.deleteAll();
        CycleService cycleService = new CycleService(cycleRepository, userRepository);
        dailyUsageService = new DailyUsageService(cycleService, dailyUsageRepository, userRepository);
    }

    /**
     * Tear down the test environment.
     */
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        dailyUsageRepository.deleteAll();
    }

    /**
     * Test adding a daily usage.
     * Expect the daily usage to be added successfully.
     */
    @Test
    void testAddDailyUsage() {
        User user = new User("user-id-1", "2024600871", "John", "Doe", "john@doe.com", "password");
        userRepository.save(user);

        DailyUsage dailyUsage = new DailyUsage("usage-id-1", "2024600871", new Date(), 100, "user-id-1");

        DailyUsageDTO createdDailyUsage = dailyUsageService.addDailyUsage(dailyUsage);

        assertNotNull(createdDailyUsage);
        assertEquals(100, createdDailyUsage.getUsedInMb());
    }

    /**
     * Test adding a daily usage when there is a duplicate usage date.
     * Expect the daily usage to not be added.
     */
    @Test
    void testAddDailyUsage_WhenDuplicateUsageDate() {
        User user = new User("user-id-4", "2024600874", "Bob", "Johnson", "bob@example.com", "password");
        userRepository.save(user);

        Date usageDate = new Date();
        DailyUsage initialDailyUsage = new DailyUsage("usage-id-4", "2024600874", usageDate, 400, "user-id-4");
        dailyUsageRepository.save(initialDailyUsage);

        DailyUsage duplicateDailyUsage = new DailyUsage("usage-id-5", "2024600874", usageDate, 500, "user-id-4");

        DailyUsageDTO createdDailyUsage = dailyUsageService.addDailyUsage(duplicateDailyUsage);

        assertNull(createdDailyUsage);
    }

    /**
     * Test adding a daily usage when the MDN does not match the user.
     * Expect the daily usage to not be added.
     */
    @Test
    void testAddDailyUsage_WhenMdnMismatch() {
        User user = new User("user-id-3", "2024600873", "Alice", "Smith", "alice@example.com", "password");
        userRepository.save(user);

        DailyUsage dailyUsage = new DailyUsage("usage-id-3", "2024600871", new Date(), 300, "user-id-3");

        DailyUsageDTO createdDailyUsage = dailyUsageService.addDailyUsage(dailyUsage);

        assertNull(createdDailyUsage);
    }

    /**
     * Test adding a daily usage when the user does not exist in user collection.
     * Expect the daily usage to not be added.
     */
    @Test
    void testAddDailyUsage_WhenUserNotFound() {
        DailyUsage dailyUsage = new DailyUsage("usage-id-2", "2024600871", new Date(), 400, "nonexistent-user-id");

        DailyUsageDTO createdDailyUsage = dailyUsageService.addDailyUsage(dailyUsage);

        assertNull(createdDailyUsage);
    }

    /**
     * Test deleting a daily usage.
     * Expect the daily usage to be deleted successfully.
     */
    @Test
    void testDeleteDailyUsage() {
        DailyUsage dailyUsage = new DailyUsage("usage-id-12", "2024600871", new Date(), 1200, "user-id-1");
        dailyUsageRepository.save(dailyUsage);

        boolean result = dailyUsageService.deleteDailyUsage("usage-id-12");

        assertTrue(result);
        assertFalse(dailyUsageRepository.findById("usage-id-12").isPresent());
    }

    /**
     * Test deleting a daily usage when the usage is not found.
     * Expect the daily usage to not be deleted.
     */
    @Test
    void testDeleteDailyUsage_WhenUsageNotFound() {
        boolean result = dailyUsageService.deleteDailyUsage("nonexistent-usage-id");
        assertFalse(result);
    }

    /**
     * Test getting all daily usages.
     * Expect all daily usages to be returned.
     */
    @Test
    void testGetAllDailyUsages() {
        DailyUsage dailyUsage1 = new DailyUsage("usage-id-6", "2024600871", new Date(), 600, "user-id-1");
        DailyUsage dailyUsage2 = new DailyUsage("usage-id-7", "2024600872", new Date(), 700, "user-id-2");
        dailyUsageRepository.saveAll(List.of(dailyUsage1, dailyUsage2));

        List<DailyUsageDTO> allDailyUsages = dailyUsageService.getAllDailyUsages();

        assertEquals(2, allDailyUsages.size());
        assertEquals(600, allDailyUsages.get(0).getUsedInMb());
        assertEquals(700, allDailyUsages.get(1).getUsedInMb());
    }

    /**
     * Test getting daily usage history for a given user.
     * Expect the correct daily usage history to be returned.
     */
    @Test
    void testGetDailyUsageHistory() {
        User user = new User("user-id-5", "2024600875", "Charlie", "Brown", "charlie@example.com", "password");
        userRepository.save(user);

        Date startDate = new Date();

        DailyUsage dailyUsage1 = new DailyUsage("usage-id-8", "2024600875", new Date(), 800, "user-id-5");
        DailyUsage dailyUsage2 = new DailyUsage("usage-id-9", "2024600875", new Date(), 900, "user-id-5");
        dailyUsageRepository.saveAll(List.of(dailyUsage1, dailyUsage2));

        Date endDate = new Date();
        Cycle cycle = new Cycle("cycle-id-1", "2024600875", startDate, endDate, "user-id-5");
        cycleRepository.save(cycle);

        List<DailyUsageDTO> usageHistory = dailyUsageService.getDailyUsageHistory("user-id-5", "2024600875");

        assertEquals(2, usageHistory.size());
        assertEquals(800, usageHistory.get(0).getUsedInMb());
        assertEquals(900, usageHistory.get(1).getUsedInMb());
    }

    /**
     * Test getting daily usage history for a given user when no recent cycle exists.
     * Expect null to be returned.
     */
    @Test
    void testGetDailyUsageHistory_WhenNoRecentCycle() {
        User user = new User("user-id-6", "2024600876", "Eve", "Davis", "eve@example.com", "password");
        userRepository.save(user);

        DailyUsage dailyUsage1 = new DailyUsage("usage-id-10", "2024600876", new Date(), 1000, "user-id-6");
        DailyUsage dailyUsage2 = new DailyUsage("usage-id-11", "2024600876", new Date(), 1100, "user-id-6");
        dailyUsageRepository.saveAll(List.of(dailyUsage1, dailyUsage2));

        List<DailyUsageDTO> usageHistory = dailyUsageService.getDailyUsageHistory("user-id-6", "2024600876");

        assertNull(usageHistory);
    }

    /**
     * Test update used in Mb for a daily usage.
     * Expect the used in Mb to be updated successfully.
     */
    @Test
    void testUpdateUsedInMb() {
        User user = new User("user-id-7", "2024600877", "Frank", "Evans", "frank@example.com", "password");
        userRepository.save(user);

        Date usageDate = new Date();
        DailyUsage dailyUsage = new DailyUsage("usage-id-13", "2024600877", usageDate, 1300, "user-id-7");
        dailyUsageRepository.save(dailyUsage);

        DailyUsageDTO updatedDailyUsage = dailyUsageService.updateUsedInMb(usageDate, "2024600877", 1500);

        assertNotNull(updatedDailyUsage);
        assertEquals(1500, updatedDailyUsage.getUsedInMb());
    }

    /**
     * Test updating the used in Mb for a daily usage when the usage is not found.
     * Expect null to be returned.
     */
    @Test
    void testUpdateUsedInMb_WhenUsageNotFound() {
        DailyUsageDTO updatedDailyUsage = dailyUsageService.updateUsedInMb(new Date(), "nonexistent-mdn", 1500);
        assertNull(updatedDailyUsage);
    }
}

