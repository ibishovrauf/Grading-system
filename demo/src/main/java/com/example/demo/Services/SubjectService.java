package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Semester;
import com.example.demo.Models.Subject;
import com.example.demo.Models.Year;
import com.example.demo.Repositories.SubjectRepo;

@Service
public class SubjectService {

    @Autowired
    SubjectRepo subjectRepo;

    public void save(Subject subject) {
        subjectRepo.save(subject);
    }

    public List<Subject> findAll(){
        return subjectRepo.findAll();
    }

    public Optional<Subject> findById(long id){
        return subjectRepo.findById(id);
    }
    
    public List<Subject> findByYearAndSemester(Year year, Semester semester){
        return subjectRepo.findByYearAndSemester(year, semester);
    }

    public List<Subject> findByYear(Year year){
        return subjectRepo.findByYear(year);
    }

    public List<Subject> findById(List<Long> ids){
        return subjectRepo.findAllById(ids);
    }
}
