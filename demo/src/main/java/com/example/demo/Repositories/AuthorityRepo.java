package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Authority;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Long>{
    Optional<Authority> findByRoleCode(String roleCode);
}
