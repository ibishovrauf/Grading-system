package com.example.demo.Models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
        
    @JsonManagedReference
    @OneToMany(mappedBy = "subject")
    List<Exam> exams;

    @Column
    private double coefficient;

    @Column
    private Year year;

    @Column
    private Semester semester;

    @ManyToMany(mappedBy = "subjects")
    private List<Group> groups;

    public Subject(){}

    public Semester getSemester() {
        return semester;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    // public void setGroups(List<Group> groups) {
    //     this.groups = groups;
    // }

    public void setName(String name) {
        this.name = name;
    }
}
