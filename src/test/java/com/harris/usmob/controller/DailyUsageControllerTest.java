package com.harris.usmob.controller;

import com.harris.usmob.dto.DailyUsageDTO;
import com.harris.usmob.entity.DailyUsage;
import com.harris.usmob.service.DailyUsageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DailyUsageController.class)
public class DailyUsageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DailyUsageService dailyUsageService;

    private static final String BASE_URL = "/api/daily-usage";

    private DailyUsage mockDailyUsage;
    private DailyUsageDTO mockDailyUsageDTO;

    @BeforeEach
    public void setup() {
        Date usageDate = new Date();
        mockDailyUsage = new DailyUsage("usageId", "2024600871", usageDate, 500, "userId");
        mockDailyUsageDTO = new DailyUsageDTO(usageDate, 500);
    }

    @Test
    public void testAddDailyUsage() throws Exception {
        Mockito.when(dailyUsageService.addDailyUsage(any(DailyUsage.class))).thenReturn(mockDailyUsageDTO);

        mockMvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockDailyUsage)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usageDate").value(mockDailyUsageDTO.getUsageDate()))
                .andExpect(jsonPath("$.usedInMb").value(mockDailyUsageDTO.getUsedInMb()));
    }

    @Test
    public void testAddDailyUsageConflict() throws Exception {
        Mockito.when(dailyUsageService.addDailyUsage(any(DailyUsage.class))).thenReturn(null);

        mockMvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockDailyUsage)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Error adding daily usage"));
    }

    @Test
    public void testGetAllDailyUsages() throws Exception {
        Mockito.when(dailyUsageService.getAllDailyUsages()).thenReturn(Collections.singletonList(mockDailyUsageDTO));

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usageDate").value(mockDailyUsageDTO.getUsageDate()))
                .andExpect(jsonPath("$[0].usedInMb").value(mockDailyUsageDTO.getUsedInMb()));
    }

    @Test
    public void testGetAllDailyUsagesNotFound() throws Exception {
        Mockito.when(dailyUsageService.getAllDailyUsages()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No daily usages found."));
    }

    @Test
    public void testGetDailyUsageHistory() throws Exception {
        Mockito.when(dailyUsageService.getDailyUsageHistory(anyString(), anyString()))
                .thenReturn(Collections.singletonList(mockDailyUsageDTO));

        mockMvc.perform(get(BASE_URL + "/history/{userId}/{mdn}", "userId", mockDailyUsage.getMdn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usageDate").value(mockDailyUsageDTO.getUsageDate()))
                .andExpect(jsonPath("$[0].usedInMb").value(mockDailyUsageDTO.getUsedInMb()));
    }

    @Test
    public void testGetDailyUsageHistoryNotFound() throws Exception {
        Mockito.when(dailyUsageService.getDailyUsageHistory(anyString(), anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/history/{userId}/{mdn}", "userId", mockDailyUsage.getMdn()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No daily usage history found."));
    }

    @Test
    public void testDeleteDailyUsage() throws Exception {
        Mockito.when(dailyUsageService.deleteDailyUsage(anyString())).thenReturn(true);

        mockMvc.perform(delete(BASE_URL + "/delete/{usageId}", mockDailyUsage.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Daily usage deleted successfully."));
    }

    @Test
    public void testDeleteDailyUsageNotFound() throws Exception {
        Mockito.when(dailyUsageService.deleteDailyUsage(anyString())).thenReturn(false);

        mockMvc.perform(delete(BASE_URL + "/delete/{usageId}", mockDailyUsage.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Daily usage does not exist."));
    }

    @Test
    public void testUpdateUsedInMb() throws Exception {
        DailyUsageDTO mockDailyUsageDTOUpdated = new DailyUsageDTO(mockDailyUsage.getUsageDate(), 1000);

        Mockito.when(dailyUsageService.updateUsedInMb(any(), anyString(), eq(1000))).thenReturn(mockDailyUsageDTOUpdated);

        mockMvc.perform(patch(BASE_URL + "/update/{usedInMb}", 1000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockDailyUsage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usageDate").value(mockDailyUsageDTOUpdated.getUsageDate()))
                .andExpect(jsonPath("$.usedInMb").value(1000));
    }

    @Test
    public void testUpdateUsedInMbNotFound() throws Exception {
        Mockito.when(dailyUsageService.updateUsedInMb(any(), anyString(), anyInt())).thenReturn(null);

        mockMvc.perform(patch(BASE_URL + "/update/{usedInMb}", 1000)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockDailyUsage)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Error updating daily usage"));
    }
}
