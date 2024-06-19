package com.harris.usmob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ViewController.class)
public class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddDailyUsage() throws Exception {
        mockMvc.perform(get("/daily-usage/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/addDailyUsage.html"));
    }

    @Test
    void testAllCycles() throws Exception {
        mockMvc.perform(get("/cycle/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/getAllCycles.html"));
    }

    @Test
    void testAllDailyUsages() throws Exception {
        mockMvc.perform(get("/daily-usage/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/getAllDailyUsages.html"));
    }

    @Test
    void testAllUsers() throws Exception {
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/getAllUsers.html"));
    }

    @Test
    void testCreateCycle() throws Exception {
        mockMvc.perform(get("/cycle/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/addCycle.html"));
    }

    @Test
    void testCreateUser() throws Exception {
        mockMvc.perform(get("/user/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/createUser.html"));
    }

    @Test
    void testCycleHistory() throws Exception {
        mockMvc.perform(get("/cycle/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/cycleHistory.html"));
    }

    @Test
    void testDailyUsageHistory() throws Exception {
        mockMvc.perform(get("/daily-usage/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/dailyUsageHistory.html"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(get("/user/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/deleteUser.html"));
    }

    @Test
    void testHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/homeScreen.html"));
    }

    @Test
    void testSearchUser() throws Exception {
        mockMvc.perform(get("/user/search"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/searchByEmail.html"));
    }

    @Test
    void testUpdateUser() throws Exception {
        mockMvc.perform(get("/user/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/updateUser.html"));
    }

    @Test
    void testDeleteCycle() throws Exception {
        mockMvc.perform(get("/cycle/delete"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/deleteCycle.html"));
    }

    @Test
    void testTransferMDN() throws Exception {
        mockMvc.perform(get("/user/transfer"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/transferMDN.html"));
    }

    @Test
    void testUpdateDailyUsage() throws Exception {
        mockMvc.perform(get("/daily-usage/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("forward:/updateUsedInMb.html"));
    }
}
