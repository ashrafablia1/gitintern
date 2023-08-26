package com.gitintren.controller;

import com.gitintren.services.InternshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final InternshipServiceImpl internshipService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("internships", internshipService.getAllInternships());
        return "index";
    }

    @GetMapping("/user")
    public String authenticatedHome(Model model){
        model.addAttribute("internships", internshipService.getAllInternships());
        return "index";
    }
}
