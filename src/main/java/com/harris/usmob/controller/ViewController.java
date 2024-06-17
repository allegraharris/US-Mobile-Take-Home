package com.harris.usmob.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Operation(summary = "Add daily usage page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add daily usage page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/daily-usage/add")
    public String addDailyUsage() {
        return "forward:/addDailyUsage.html";
    }

    @Operation(summary = "Get all cycles page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all cycles page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/cycle/all")
    public String allCycles() {
        return "forward:/getAllCycles.html";
    }

    @Operation(summary = "Get all daily usages page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all daily usages page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/daily-usage/all")
    public String allDailyUsages() {
        return "forward:/getAllDailyUsages.html";
    }

    @Operation(summary = "Get all users page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all users page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/user/all")
    public String allUsers() {
        return "forward:/getAllUsers.html";
    }

    @Operation(summary = "Add cycle page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create cycle page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/cycle/add")
    public String createCycle() {
        return "forward:/addCycle.html";
    }

    @Operation(summary = "Create user page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create user page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/user/create")
    public String createUser() {
        return "forward:/createUser.html";
    }

    @Operation(summary = "Cycle history page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cycle history page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/cycle/history")
    public String cycle() {
        return "forward:/cycleHistory.html";
    }

    @Operation(summary = "Get daily usage history page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get daily usage history page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/daily-usage/history")
    public String dailyUsage() {
        return "forward:/dailyUsageHistory.html";
    }

    @Operation(summary = "Delete user page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete user page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/user/delete")
    public String deleteUser() {
        return "forward:/deleteUser.html";
    }

    @Operation(summary = "Home page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Home page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/home")
    public String home() {
        return "forward:/homeScreen.html";
    }

    @Operation(summary = "Search user page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search user page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/user/search")
    public String searchUser() {
        return "forward:/searchByEmail.html";
    }

    @Operation(summary = "Update user page")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update user page",
                    content = {@Content(mediaType = "text/html",
                            schema = @Schema(implementation = String.class))})
    })
    @GetMapping("/user/update")
    public String updateUser() {
        return "forward:/updateUser.html";
    }
}