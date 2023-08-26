package com.gitintren.repositories;

import com.gitintren.model.InternUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternRepository extends JpaRepository<InternUser, Long> {
}
