package com.gitintren.services;

import com.gitintren.dto.CompanyDto;
import com.gitintren.dto.InternshipDto;
import com.gitintren.model.*;
import com.gitintren.repositories.CompanyRepository;
import com.gitintren.repositories.InternshipApplicationRepository;
import com.gitintren.repositories.InternshipRepository;
import com.gitintren.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final InternshipRepository internshipRepository;
    private final InternshipApplicationRepository internshipApplicationRepository;

    public CompanyDto getProfile() {
        Long userId = extractId();
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        CompanyUser companyUser = user.getCompanyUser();
        return buldCompanyDto(user, companyUser);
    }

    public void editProfile(CompanyDto companyDto) {
        Long companyId = extractCompanyId();
        CompanyUser companyUser = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        updateCompanyUser(companyDto, companyUser);
    }

    public Long extractCompanyId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        CompanyUser companyUser = user.getCompanyUser();
        return companyUser.getId();
    }

    private void updateCompanyUser(CompanyDto companyDto, CompanyUser companyUser) {
       if (companyDto.getName() != null){companyUser.setCompanyName(companyDto.getName());}
       if (companyDto.getCompanyLink() != null){companyUser.setCompanyLink(companyDto.getCompanyLink());}
       if (companyDto.getPhoneNumber() != null){companyUser.setPhoneNumber(companyDto.getPhoneNumber());}
       if (companyDto.getCountry() != null){companyUser.setCountry(companyDto.getCountry());}
       if (companyDto.getCity() != null){companyUser.setCity(companyDto.getCity());}
       companyRepository.save(companyUser);
    }


    private CompanyDto buldCompanyDto(User user, CompanyUser companyUser) {
        return CompanyDto.builder().name(companyUser.getCompanyName()).email(user.getEmail())
                .companyLink(companyUser.getCompanyLink()).phoneNumber(companyUser.getPhoneNumber())
                .country(companyUser.getCountry()).city(companyUser.getCity()).build();
    }

    public Long extractId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    public CompanyUser getCurrentCompany() {
        try {
            Long userId = extractId();
            User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            return user.getCompanyUser();
        } catch (Exception e) {return null;}
    }

    public List<InternshipDto> getMyInternships() {
        CompanyUser company = getCurrentCompany();
        List<Internship> internships = internshipRepository.findAllByCompany(company);
        return InternshipServiceImpl.getInternshipDtos(internships);
    }


    public List<InternshipApplication> getMyInternshipApplications(Long internshipId) {
        Internship internship = internshipRepository.findById(internshipId).orElseThrow(() -> new RuntimeException("Internship not found"));
       return internshipApplicationRepository.findAllByInternship(internship);
    }

    public void setApplicationStatus(Long internshipApplicationId, Status status) {
        InternshipApplication internshipApplication = internshipApplicationRepository.findById(internshipApplicationId).orElseThrow(() -> new RuntimeException("Internship application not found"));
        internshipApplication.setStatus(status);
        internshipApplicationRepository.save(internshipApplication);
    }
}
