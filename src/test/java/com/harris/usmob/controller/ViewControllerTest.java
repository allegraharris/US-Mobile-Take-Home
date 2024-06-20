package com.harris.usmob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for the ViewController class.
 */
@WebMvcTest(ViewController.class)
public class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test the addDailyUsage method.
     * Expect a 200 status code and the addDailyUsage view.
     * @throws Exception if an error occurs
     */
    @Test
    void testAddDailyUsage() throws Exception {
        mockMvc.perform(get("/daily-usage/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/addDailyUsage.html"));
    }

    /**
     * Test the getAllCycles method.
     * Expect a 200 status code and the getAllCycles view.
     * @throws Exception if an error occurs
     */
    @Test
    void testAllCycles() throws Exception {
        mockMvc.perform(get("/cycle/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/getAllCycles.html"));
    }

    /**
     * Test the getAllDailyUsages method.
     * Expect a 200 status code and the getAllDailyUsages view.
     * @throws Exception if an error occurs
     */
    @Test
    void testAllDailyUsages() throws Exception {
        mockMvc.perform(get("/daily-usage/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/getAllDailyUsages.html"));
    }

    /**
     * Test the getAllUsers method.
     * Expect a 200 status code and the getAllUsers view.
     * @throws Exception if an error occurs
     */
    @Test
    void testAllUsers() throws Exception {
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/getAllUsers.html"));
    }

    /**
     * Test the createCycle method.
     * Expect a 200 status code and the addCycle view.
     * @throws Exception if an error occurs
     */
    @Test
    void testCreateCycle() throws Exception {
        mockMvc.perform(get("/cycle/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/addCycle.html"));
    }

    /**
     * Test the createUser method.
     * Expect a 200 status code and the createUser view.
     * @throws Exception if an error occurs
     */
    @Test
    void testCreateUser() throws Exception {
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/createUser.html"));
    }

    /**
     * Test the cycleHistory method.
     * Expect a 200 status code and the cycleHistory view.
     * @throws Exception if an error occurs
     */
    @Test
    void testCycleHistory() throws Exception {
        mockMvc.perform(get("/cycle/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/cycleHistory.html"));
    }

    /**
     * Test the dailyUsageHistory method.
     * Expect a 200 status code and the dailyUsageHistory view.
     * @throws Exception if an error occurs
     */
    @Test
    void testDailyUsageHistory() throws Exception {
        mockMvc.perform(get("/daily-usage/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/dailyUsageHistory.html"));
    }

    /**
     * Test the deleteCycle method.
     * Expect a 200 status code and the deleteCycle view.
     * @throws Exception if an error occurs
     */
    @Test
    void testDeleteCycle() throws Exception {
        mockMvc.perform(get("/cycle/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/deleteCycle.html"));
    }

    /**
     * Test the deleteDailyUsage method.
     * Expect a 200 status code and the deleteDailyUsage view.
     * @throws Exception if an error occurs
     */
    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(get("/user/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/deleteUser.html"));
    }

    /**
     * Test the home method.
     * Expect a 200 status code and the homeScreen view.
     * @throws Exception if an error occurs
     */
    @Test
    void testHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/homeScreen.html"));
    }

    /**
     * Test the searchByEmail method.
     * Expect a 200 status code and the searchByEmail view.
     * @throws Exception if an error occurs
     */
    @Test
    void testSearchUser() throws Exception {
        mockMvc.perform(get("/user/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/searchByEmail.html"));
    }

    /**
     * Test the transferMDN method.
     * Expect a 200 status code and the transferMDN view.
     * @throws Exception if an error occurs
     */
    @Test
    void testTransferMDN() throws Exception {
        mockMvc.perform(get("/user/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/transferMDN.html"));
    }

    /**
     * Test the updateUsedInMb method.
     * Expect a 200 status code and the updateUsedInMb view.
     * @throws Exception if an error occurs
     */
    @Test
    void testUpdateDailyUsage() throws Exception {
        mockMvc.perform(get("/daily-usage/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/updateUsedInMb.html"));
    }

    /**
     * Test the updateUser method.
     * Expect a 200 status code and the updateUser view.
     * @throws Exception if an error occurs
     */
    @Test
    void testUpdateUser() throws Exception {
        mockMvc.perform(get("/user/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/updateUser.html"));
    }
}
