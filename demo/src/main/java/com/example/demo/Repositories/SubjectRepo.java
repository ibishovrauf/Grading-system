package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Semester;
import com.example.demo.Models.Subject;
import com.example.demo.Models.Year;

@Repository
public interface SubjectRepo extends JpaRepository<Subject, Long>{
    List<Subject> findByYearAndSemester(Year year, Semester semester);
    List<Subject> findByYear(Year year);
}
