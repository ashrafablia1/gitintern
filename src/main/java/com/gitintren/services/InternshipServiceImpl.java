package com.gitintren.services;


import com.gitintren.dto.InternshipDto;
import com.gitintren.model.CompanyUser;
import com.gitintren.model.Internship;
import com.gitintren.model.InternshipApplication;
import com.gitintren.repositories.InternshipApplicationRepository;
import com.gitintren.repositories.InternshipRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//checked getInternship & getAllInternships
@RequiredArgsConstructor
@Service
public class InternshipServiceImpl{

    private final InternshipRepository internshipRepository;
    private final CompanyServiceImpl companyService;
    private final ModelMapper modelMapper;
    private final InternshipApplicationRepository internshipApplicationRepository;

    public Long createInternship(InternshipDto internshipDto) {
        CompanyUser company = companyService.getCurrentCompany();
        Internship internship = BuildInternshipObj(internshipDto, company);
            internshipRepository.save(internship);
            return internship.getInternshipId();
    }

    public InternshipDto getInternship(Long internshipId) {
        Internship internship = internshipRepository.findById(internshipId).orElseThrow(() -> new RuntimeException("Internship not found"));
        InternshipDto internshipDto = new InternshipDto();
        modelMapper.map(internship, internshipDto);
        return internshipDto;
    }

    public void editInternship(Long internshipId, InternshipDto internshipDto) {
        Internship internship = internshipRepository.findById(internshipId).orElseThrow(() -> new RuntimeException("Internship not found"));
        internshipDtoToInternship(internshipDto, internship);
        internshipRepository.save(internship);
    }

    public List<InternshipDto> getAllInternships() {
        List<Internship> internships = internshipRepository.findAll();
        return getInternshipDtos(internships);
    }

    public Internship getById(Long internshipId) {
        return internshipRepository.findById(internshipId).orElse(null);
    }

    public InternshipApplication getInternshipApplication(Long internshipApplicationId) {
        return internshipApplicationRepository.findById(internshipApplicationId).orElseThrow(() -> new RuntimeException("Internship application not found"));
    }

    private static Internship BuildInternshipObj(InternshipDto internshipDto, CompanyUser company) {
        return Internship.builder().company(company).title(internshipDto.getTitle()).location(internshipDto.getLocation())
                .duration(internshipDto.getDuration()).description(internshipDto.getDescription())
                .requirements(internshipDto.getRequirements()).responsibilities(internshipDto.getResponsibilities())
                .qualifications(internshipDto.getQualifications()).contactEmail(internshipDto.getContactEmail())
                .isPaid(internshipDto.isPaid()).numberOfPositions(internshipDto.getNumberOfPositions())
                .isRemote(internshipDto.isRemote()).applicationDeadline(internshipDto.getApplicationDeadline()).build();
    }

    private static void internshipDtoToInternship(InternshipDto internshipDto, Internship internship) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.map(internshipDto, internship);
    }

    public static List<InternshipDto> getInternshipDtos(List<Internship> internships) {
        List<InternshipDto> internshipDtos = new ArrayList<>();
        for (Internship internship : internships) {
            InternshipDto internshipDto = new InternshipDto(internship);
            internshipDtos.add(internshipDto);
        }
        Collections.reverse(internshipDtos);
        return internshipDtos;
    }

    public InternshipApplication findByInternship(Long internshipId) {
      try {
          Internship internship = internshipRepository.findById(internshipId).orElseThrow(() -> new RuntimeException("Internship not found"));
          return internshipApplicationRepository.findByInternship(internship);
      } catch (Exception e) {return null;}
    }


}
