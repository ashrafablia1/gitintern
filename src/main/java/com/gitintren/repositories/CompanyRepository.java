package com.gitintren.repositories;

import com.gitintren.model.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CompanyRepository extends JpaRepository<CompanyUser, Long> {
}
