package com.example.demo.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Exam;
import com.example.demo.Models.Grade;
import com.example.demo.Models.User;
import com.example.demo.Repositories.GradeRepo;

@Service
public class GradeService {
    @Autowired
    private GradeRepo gradeRepo;

    public void save(Grade grade){
        gradeRepo.save(grade);
    }

    public List<Grade> findByStudentAndExam(User user, Exam exam){
        return gradeRepo.findByStudentAndExam(user, exam);
    }

    public List<Grade> findByStudent(User user){
        return gradeRepo.findByStudent(user);
    }

    public List<Grade> findByExamaIn(List<Exam> exams){
        return gradeRepo.findByExamIn(exams);
    }
}
