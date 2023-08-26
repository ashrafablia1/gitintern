package com.gitintren.controller;


import com.gitintren.dto.InternshipDto;
import com.gitintren.model.*;
import com.gitintren.services.CompanyServiceImpl;
import com.gitintren.services.InternServiceImpl;
import com.gitintren.services.InternshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/internship")
public class InternshipController {

    private final InternshipServiceImpl internshipService;
    private final CompanyServiceImpl companyService;
    private final InternServiceImpl internService;

    @GetMapping("/{internshipId}")
    public String getInternship(@PathVariable Long internshipId, Model model) {
        CompanyUser currentCompany = companyService.getCurrentCompany();
        InternshipApplication currentInternshipApplication = internshipService.findByInternship(internshipId);
        InternshipDto internshipDto = internshipService.getInternship(internshipId);

        if (currentCompany != null && internshipDto.getCompanyId().equals(currentCompany.getId())) {
            model.addAttribute("owner", true);
        } else if (currentInternshipApplication != null && currentInternshipApplication.getInternUser().getId().equals(internService.extractInternId())) {
            model.addAttribute("applied", false);
        }else {
            model.addAttribute("owner", false);
            model.addAttribute("applied", true);
        }
        model.addAttribute("internship", internshipDto);
        return "internship-view";
    }
}



