package com.gitintren.dto;


import com.gitintren.model.Internship;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternshipDto {


    private Long internshipId;

    private Long companyId;

    private String companyName;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Location cannot be empty")
    private String location;

    @NotEmpty(message = "Duration cannot be empty")
    private int duration;

    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @NotEmpty(message = "Requirements cannot be empty")
    private String requirements;

    @NotEmpty(message = "Responsibilities cannot be empty")
    private String responsibilities;

    @NotEmpty(message = "Qualifications cannot be empty")
    private String qualifications;

    @NotEmpty(message = "Application deadline cannot be empty")
    private LocalDate applicationDeadline;

    private String contactEmail;

    @NotEmpty(message = "is Paid cannot be empty")
    private boolean isPaid;

    @NotEmpty(message = "Number of positions cannot be empty")
    private int numberOfPositions;

    @NotEmpty(message = "is Remote cannot be empty")
    private boolean isRemote;

    public InternshipDto(Internship internship) {
        this.internshipId = internship.getInternshipId();
        this.companyId = internship.getCompany().getId();
        this.companyName = internship.getCompany().getCompanyName();
        this.title = internship.getTitle();
        this.location = internship.getLocation();
        this.duration = internship.getDuration();
        this.description = internship.getDescription();
        this.requirements = internship.getRequirements();
        this.responsibilities = internship.getResponsibilities();
        this.qualifications = internship.getQualifications();
        this.applicationDeadline = internship.getApplicationDeadline();
        this.contactEmail = internship.getContactEmail();
        this.isPaid = internship.isPaid();
        this.numberOfPositions = internship.getNumberOfPositions();
        this.isRemote = internship.isRemote();

    }
}
