package com.gitintren.services;

import com.gitintren.dto.InternDto;
import com.gitintren.model.*;
import com.gitintren.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternServiceImpl {

    private final InternRepository internRepository;
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final ResumeRepository resumeRepository;
    private final InternshipApplicationRepository internshipApplicationRepository;

    public InternDto getProfile() {
        Long userId = extractId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        InternUser internUser = user.getInternUser();
        return buildInternDto(user, internUser);
    }

    public void editProfile(InternDto internDto) {
        Long internId = extractInternId();
        InternUser internUser = internRepository.findById(internId).orElseThrow(() -> new RuntimeException("Company not found"));
        updateInternUser(internDto, internUser);
    }

    public Long extractInternId() {
      try {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
         InternUser internUser = user.getInternUser();
        return internUser.getId();} catch (Exception e) { return  null;}
    }

    public InternUser extractIntern() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getInternUser();
    }

    private void updateInternUser(InternDto internDto, InternUser internUser) {
     if (internDto.getFirstName() != null) {internUser.setFirstName(internDto.getFirstName());}
        if (internDto.getLastName() != null) {internUser.setLastName(internDto.getLastName());}
        if (internDto.getPhoneNumber() != null) {internUser.setPhoneNumber(internDto.getPhoneNumber());}
        if (internDto.getCountry() != null) {internUser.setCountry(internDto.getCountry());}
        if (internDto.getCity() != null) {internUser.setCity(internDto.getCity());}
        if (internDto.getResume() != null) {internUser.setResume(internDto.getResume());}
        internRepository.save(internUser);
    }

    private InternDto buildInternDto(User user, InternUser internUser) {
        return InternDto.builder().internId(internUser.getId()).firstName(internUser.getFirstName())
                .lastName(internUser.getLastName()).email(user.getEmail())
                .phoneNumber(internUser.getPhoneNumber()).country(internUser.getCountry())
                .city(internUser.getCity()).resume(internUser.getResume()).build();
    }

    public Long extractId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    public Resume getResumeByResumeId(Long resumeId) {
        try {
            return resumeRepository.findById(resumeId).orElseThrow(() -> new RuntimeException("Resume not found"));
        } catch (Exception e) {return null;}
    }

    public void createInternshipApplication(Long internshipId) {
        Internship internship = internshipRepository.getById(internshipId);
        InternUser intern = extractIntern();
        var internshipApplication = new InternshipApplication(internship, intern);
        internshipApplicationRepository.save(internshipApplication);
    }

    public List<InternshipApplication> getMyInternshipsApplications() {
        InternUser intern = extractIntern();
        List<InternshipApplication> internshipApplications = internshipApplicationRepository.findAllByInternUser(intern);
        Collections.reverse(internshipApplications);
        return internshipApplications;
    }
}
