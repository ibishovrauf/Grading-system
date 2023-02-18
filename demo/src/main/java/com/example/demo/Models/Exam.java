package com.example.demo.Models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private double coefficient;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    public Exam(){}

    public double getCoefficient() {
        return coefficient;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Subject getSubject() {
        return subject;
    }
}
