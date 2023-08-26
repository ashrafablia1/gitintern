package com.gitintren.controller;

import com.gitintren.authentication.EmailSenderService;
import com.gitintren.dto.CompanyDto;
import com.gitintren.dto.InternshipDto;
import com.gitintren.model.InternshipApplication;
import com.gitintren.model.Status;
import com.gitintren.services.CompanyServiceImpl;
import com.gitintren.services.InternshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    @Value("${spring.mail.username}") private String sender;


    private final CompanyServiceImpl companyService;
    private final InternshipServiceImpl internshipService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/profile")
    public String CompanyProfileForm(Model model) {
        CompanyDto companyDto = companyService.getProfile();
        model.addAttribute("company", companyDto);
        return "company-profile";
    }

    @GetMapping("/profile/edit")
    public String editCompanyProfileForm(Model model) {
        CompanyDto companyDto = companyService.getProfile();
        model.addAttribute("company", companyDto);
        return "company-profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editCompanyProfile(CompanyDto company) {
        companyService.editProfile(company);
        return "redirect:/company/profile";
    }


    @GetMapping("/internship/posting")
    public String postingInternshipForm(Model model) {
        model.addAttribute("internshipDto", new InternshipDto());
        return "posting-internship";
    }

    @PostMapping("/posting/internship")
    public String createInternship(@ModelAttribute("internshipDto") InternshipDto internshipDto) {
        Long internshipId = internshipService.createInternship(internshipDto);
        return "redirect:/internship/" + internshipId;
    }

    @GetMapping("/internship/edit/{internshipId}")
    public String editInternshipForm(@PathVariable Long internshipId, Model model) {
        InternshipDto internship = internshipService.getInternship(internshipId);
        model.addAttribute("internship", internship);
        if (!internship.getCompanyId().equals(companyService.extractCompanyId())) {return "redirect:/internship/" + internshipId;}
        return "internship-edit";
    }

    @PostMapping("/internship/edit/{internshipId}")
    public String editInternship(@PathVariable Long internshipId, @ModelAttribute("internship") InternshipDto internshipDto) {
        internshipService.editInternship(internshipId, internshipDto);
        return "redirect:/internship/" + internshipId;
    }

    @GetMapping("/my/internships")
    public String getMyInternships(Model model) {
        model.addAttribute("internships", companyService.getMyInternships());
        return "my-internships";
    }

    @GetMapping("/internship/applications/{internshipId}")
    public String getInternshipApplications(@PathVariable Long internshipId, Model model) {
        InternshipDto internship = internshipService.getInternship(internshipId);
        if (internship.getCompanyId().equals(companyService.extractCompanyId())) {
            model.addAttribute("internshipApplications", companyService.getMyInternshipApplications(internshipId));
            return "all-applications";
        } else {return "redirect:/internship/" + internshipId;}
    }

    @PostMapping("/application/{internshipApplicationId}/status")
    public String changeApplicationStatus(@PathVariable Long internshipApplicationId, @RequestParam String status, @RequestParam Long internshipId) {
       InternshipApplication internshipApplication = internshipService.getInternshipApplication(internshipApplicationId);
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(internshipApplication.getEmail());
        mailMessage.setSubject("GitIntern, Update about your Application!");
        mailMessage.setFrom(sender);
        if (status.equals(Status.ACCEPTED.toString())) {
            mailMessage.setText("Congratulations!! your Application for " + internshipApplication.getInternship().getTitle() + " Internship is " + status + ". The company will contact you soon.");
        } else {
            mailMessage.setText("Unfortunately!! your Application for " + internshipApplication.getInternship().getTitle() + " Internship is " + status + ". Dont worry! try your chance with other internships");
        }
        emailSenderService.sendEmail(mailMessage);
        companyService.setApplicationStatus(internshipApplicationId, Status.valueOf(status));
        return "redirect:/company/internship/applications/" + internshipId;
    }
}
