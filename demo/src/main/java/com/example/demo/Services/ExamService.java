package com.example.demo.Services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Exam;
import com.example.demo.Repositories.ExamRepo;

@Service
public class ExamService {
    
    @Autowired
    private ExamRepo examRepo;

    public void save(Exam exam){
        examRepo.save(exam);
    }

    public Optional<Exam> findById(Long id){
        return examRepo.findById(id);
    }
}
