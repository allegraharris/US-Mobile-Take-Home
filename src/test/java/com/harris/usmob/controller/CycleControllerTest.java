package com.harris.usmob.controller;

import com.harris.usmob.dto.CycleDTO;
import com.harris.usmob.entity.Cycle;
import com.harris.usmob.service.CycleService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CycleController.class)
public class CycleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CycleService cycleService;

    private static final String BASE_URL = "/api/cycle";

    private Cycle mockCycle;
    private CycleDTO mockCycleDTO;

    @BeforeEach
    public void setup() {
        Date startDate = new Date();
        Date endDate = new Date();
        mockCycle = new Cycle("cycleId", "2024600871", startDate, endDate, "2024-06-18");
        mockCycleDTO = new CycleDTO("cycleId", startDate, endDate);
    }

    @Test
    public void testAddCycle() throws Exception {
        Mockito.when(cycleService.addCycle(any(Cycle.class))).thenReturn(mockCycleDTO);

        mockMvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCycle)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cycleId").value(mockCycleDTO.getCycleId()))
                .andExpect(jsonPath("$.startDate").value(mockCycleDTO.getStartDate()))
                .andExpect(jsonPath("$.endDate").value(mockCycleDTO.getEndDate()));
    }

    @Test
    public void testAddCycleConflict() throws Exception {
        Mockito.when(cycleService.addCycle(any(Cycle.class))).thenReturn(null);

        mockMvc.perform(post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockCycle)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Error adding cycle"));
    }

    @Test
    public void testDeleteCycle() throws Exception {
        Mockito.when(cycleService.deleteCycle(anyString())).thenReturn(true);

        mockMvc.perform(post(BASE_URL + "/delete/{cycleId}", mockCycle.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Cycle deleted successfully."));
    }

    @Test
    public void testDeleteCycleNotFound() throws Exception {
        Mockito.when(cycleService.deleteCycle(anyString())).thenReturn(false);

        mockMvc.perform(post(BASE_URL + "/delete/{cycleId}", mockCycle.getId()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cycle does not exist."));
    }

    @Test
    public void testGetAllCycles() throws Exception {
        Mockito.when(cycleService.getAllCycles()).thenReturn(Collections.singletonList(mockCycleDTO));

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cycleId").value(mockCycleDTO.getCycleId()))
                .andExpect(jsonPath("$[0].startDate").value(mockCycleDTO.getStartDate()))
                .andExpect(jsonPath("$[0].endDate").value(mockCycleDTO.getEndDate()));
    }

    @Test
    public void testGetAllCyclesNotFound() throws Exception {
        Mockito.when(cycleService.getAllCycles()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/all"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No cycles found."));
    }

    @Test
    public void testGetCycleHistory() throws Exception {
        Mockito.when(cycleService.getCycleHistory(anyString(), anyString())).thenReturn(Collections.singletonList(mockCycleDTO));

        mockMvc.perform(get(BASE_URL + "/history/{userId}/{mdn}", mockCycle.getUserId(), mockCycle.getMdn()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cycleId").value(mockCycleDTO.getCycleId()))
                .andExpect(jsonPath("$[0].startDate").value(mockCycleDTO.getStartDate()))
                .andExpect(jsonPath("$[0].endDate").value(mockCycleDTO.getEndDate()));
    }

    @Test
    public void testGetCycleHistoryNotFound() throws Exception {
        Mockito.when(cycleService.getCycleHistory(anyString(), anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/history/{userId}/{mdn}", mockCycle.getUserId(), mockCycle.getMdn()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No cycle history found."));
    }
}