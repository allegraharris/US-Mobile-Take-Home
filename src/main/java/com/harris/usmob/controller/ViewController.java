package com.harris.usmob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/home")
    public String home() {
        return "forward:/homeScreen.html";
    }

    @GetMapping("/user/create")
    public String createUser() {
        return "forward:/createUser.html";
    }

    @GetMapping("/user/update")
    public String updateUser() {
        return "forward:/updateUser.html";
    }

    @GetMapping("/user/delete")
    public String deleteUser() {
        return "forward:/deleteUser.html";
    }

    @GetMapping("/user/search")
    public String searchUser() {
        return "forward:/searchByEmail.html";
    }

    @GetMapping("/user/all")
    public String allUsers() {
        return "forward:/getAllUsers.html";
    }

    @GetMapping("/cycle/history")
    public String cycle() {
        return "forward:/cycleHistory.html";
    }

    @GetMapping("/cycle/add")
    public String createCycle() {
        return "forward:/addCycle.html";
    }

    @GetMapping("/cycle/all")
    public String allCycles() { return "forward:/getAllCycles.html";}

    @GetMapping("/daily-usage/add")
    public String addDailyUsage() {
        return "forward:/addDailyUsage.html";
    }

    @GetMapping("/daily-usage/history")
    public String dailyUsage() {
        return "forward:/dailyUsageHistory.html";
    }

    @GetMapping("/daily-usage/all")
    public String allDailyUsages() { return "forward:/getAllDailyUsages.html";}
}