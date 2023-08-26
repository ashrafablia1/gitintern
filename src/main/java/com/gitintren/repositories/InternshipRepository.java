package com.gitintren.repositories;



import com.gitintren.model.CompanyUser;
import com.gitintren.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    List<Internship> findAllByCompany(CompanyUser company);


}
