package com.note.main.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 첫 Main Controller
 */
@Slf4j
@Controller
public class MainController {

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("hello", "Hello, World!");
        return "/home";
    }
}
