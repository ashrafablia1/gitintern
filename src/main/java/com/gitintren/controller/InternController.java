package com.gitintren.controller;


import com.gitintren.dto.InternDto;
import com.gitintren.dto.InternshipDto;
import com.gitintren.model.Resume;
import com.gitintren.services.InternServiceImpl;
import com.gitintren.services.InternshipServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/intern")

public class InternController {
    private final InternServiceImpl internService;
    private final InternshipServiceImpl internshipService;

    @GetMapping("/profile")
    public String InternProfileView(Model model) {
        InternDto internDto = internService.getProfile();
        model.addAttribute("intern", internDto);

        Resume resume =internDto.getResume();
        if ( resume != null && resume.getData() != null) {
            model.addAttribute("resumeFound", true);
            model.addAttribute("resumeUrl", "/intern/" + resume.getResumeId() + "/download");
            model.addAttribute("resumeFileName", resume.getFileName()+"."+resume.getFileType());
            model.addAttribute("resumeViewName", resume.getFileName());
        } else {model.addAttribute("resumeFound", false);}
        return "intern-profile";
    }

    @GetMapping("/profile/edit")
    public String editInternProfile(Model model) {
        InternDto internDto = internService.getProfile();
        model.addAttribute("intern", internDto);
        return "intern-profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editInternProfile(@RequestParam("file") MultipartFile file, InternDto intern) {
        if (!file.isEmpty()) {
            Resume resume = new Resume();
            resume.setFileName(file.getOriginalFilename());
            resume.setFileType(file.getContentType());
            try { resume.setData(file.getBytes());}
            catch (IOException e) {return "/intern/profile/edit?resumeerror"; }
            intern.setResume(resume);
        }
        internService.editProfile(intern);
        return "redirect:/intern/profile";
    }

    @GetMapping("/{resumeId}/download")
    public ResponseEntity<byte[]> downloadResumeFile(@PathVariable Long resumeId) {
        Resume resume = internService.getResumeByResumeId(resumeId);
        if (resume.getData() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.attachment().filename(resume.getFileName()).build());
            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resume.getData());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/internship/apply/{internshipId}")
    public String applyToInternship(@PathVariable Long internshipId, Model model) {
        InternDto intern = internService.getProfile();
        InternshipDto internshipDto = new InternshipDto(internshipService.getById(internshipId));

        model.addAttribute("intern", intern);
        model.addAttribute("internshipId", internshipDto.getInternshipId());
        model.addAttribute("internshipTitle", internshipDto.getTitle());

        Resume resume =intern.getResume();
        if ( resume != null && resume.getData() != null) {
            model.addAttribute("resumeFound", true);
            model.addAttribute("resumeUrl", "/intern/" + resume.getResumeId() + "/download");
            model.addAttribute("resumeFileName", resume.getFileName()+"."+resume.getFileType());
            model.addAttribute("resumeViewName", resume.getFileName());
        } else {
            model.addAttribute("resumeFound", false);
        }
        return "internship-apply";
    }

    @PostMapping("/internship/apply/{internshipId}")
    public String applyToInternship(@PathVariable Long internshipId,@RequestParam("file") MultipartFile file, @ModelAttribute("intern") InternDto intern) {
        if (!file.isEmpty()) {
            Resume resume = new Resume();
            resume.setFileName(file.getOriginalFilename());
            resume.setFileType(file.getContentType());
            try {resume.setData(file.getBytes());}
            catch (IOException e) {throw new RuntimeException(e);}
            intern.setResume(resume);
        }
        internService.editProfile(intern);
        internService.createInternshipApplication(internshipId);
        return "redirect:/internship/" + internshipId;
    }

    @GetMapping("/Applications")
    public String getApplications(Model model) {
        model.addAttribute("internshipApplications", internService.getMyInternshipsApplications());
        return "all-applications";
    }
}
