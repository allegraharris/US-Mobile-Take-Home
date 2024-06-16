package com.harris.usmob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/home")
    public String home() {
        return "forward:/homeScreen.html";
    }

    @GetMapping("/create")
    public String createUser() {
        return "forward:/createUser.html";
    }

    @GetMapping("/update")
    public String updateUser() {
        return "forward:/updateUser.html";
    }

    @GetMapping("/search")
    public String searchUser() {
        return "forward:/searchByEmail.html";
    }

    @GetMapping("/cycle")
    public String cycle() {
        return "forward:/cycleHistory.html";
    }

    @GetMapping("/create-cycle")
    public String createCycle() {
        return "forward:/addCycle.html";
    }
}