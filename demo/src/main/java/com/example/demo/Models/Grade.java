package com.example.demo.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "grade", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"exam_id", "user_id"})
})
public class Grade{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User student;

    @Column
    private Double score;

    @Column
    private boolean annonced;

    public Grade(){}

    public Exam getExam() {
        return exam;
    }

    public Long getId() {
        return id;
    }

    public Double getScore() {
        return score;
    }

    public User getStudent() {
        return student;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setAnnonced(boolean annonced) {
        this.annonced = annonced;
    }

    public boolean getAnnonced(){
        return this.annonced;
    }
}

