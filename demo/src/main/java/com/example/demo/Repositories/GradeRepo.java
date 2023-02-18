package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Exam;
import com.example.demo.Models.Grade;
import com.example.demo.Models.User;

@Repository
public interface GradeRepo extends JpaRepository<Grade, Long>{
    List<Grade> findByStudent(User user);

    List<Grade> findByStudentAndExam(User user, Exam exam);

    List<Grade> findByExamIn(List<Exam> exams);

}
