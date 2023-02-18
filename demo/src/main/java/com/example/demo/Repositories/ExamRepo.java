package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Exam;

@Repository
public interface ExamRepo extends JpaRepository<Exam, Long>{
    
}
