package com.gitintren.repositories;


import com.gitintren.model.InternUser;
import com.gitintren.model.Internship;
import com.gitintren.model.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {

    InternshipApplication findByInternship(Internship internship);


    List<InternshipApplication> findAllByInternUser(InternUser intern);

    List<InternshipApplication> findAllByInternship(Internship internship);
}
