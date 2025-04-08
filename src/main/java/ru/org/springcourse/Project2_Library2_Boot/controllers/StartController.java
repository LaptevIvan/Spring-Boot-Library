package ru.org.springcourse.Project2_Library2_Boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/start")
public class StartController {

    @GetMapping()
    public String startMenu() {
        return "start/menu";
    }
}
