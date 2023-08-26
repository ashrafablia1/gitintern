package com.gitintren.controller;

import com.gitintren.model.CompanyUser;
import com.gitintren.model.InternshipApplication;
import com.gitintren.model.Resume;
import com.gitintren.services.CompanyServiceImpl;
import com.gitintren.services.InternshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/application")
public class ApplicationController {
    private final CompanyServiceImpl companyService;
    private final InternshipServiceImpl internshipService;

    @GetMapping("/{internshipApplicationId}")
    public String getInternshipApplication(@PathVariable Long internshipApplicationId, Model model) {
        CompanyUser currentCompany = companyService.getCurrentCompany();
        InternshipApplication internshipApplication = internshipService.getInternshipApplication(internshipApplicationId);

        if (currentCompany != null && internshipApplication.getInternship().getCompany().equals(currentCompany)) {
            model.addAttribute("owner", true);
        } else {
            model.addAttribute("owner", false);
        }

        model.addAttribute("internshipApplication", internshipApplication);
        Resume resume =internshipApplication.getInternUser().getResume();
        if ( resume != null && resume.getData() != null) {
            model.addAttribute("resumeFound", true);
            model.addAttribute("resumeUrl", "/intern/" + resume.getResumeId() + "/download");
            model.addAttribute("resumeFileName", resume.getFileName()+"."+resume.getFileType());
            model.addAttribute("resumeViewName", resume.getFileName());
        } else {
            model.addAttribute("resumeFound", false);
        }

        return "internship-application-view";
    }
}
