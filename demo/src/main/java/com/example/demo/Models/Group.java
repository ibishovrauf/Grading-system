package com.example.demo.Models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.JoinColumn;

@Entity
@Table(name = "student_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column
    private String code;

    @Column
    private String description;

    @Column
    private Year year;

    @Column
    private Semester semester;

    @Column
    private int credits;
    
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private User adminUser;

    
    @JsonManagedReference
    @OneToMany(mappedBy = "participateInGroup")
    private List<User> userList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable( name = "subject_group",
                joinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))  
    private List<Subject> subjects;

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getCredits() {
        return credits;
    }

    public Group(){
        this.subjects = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public void setYear(Year year) {
        this.year = year;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Semester getSemester() {
        return semester;
    }

    public Year getYear() {
        return year;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public String getDescription() {
        return description;
    }
    
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }

    public void addUsers(List<User> users){
        this.userList.addAll(users);
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }
    
    public void addSubjects(List<Subject> subjects){
        this.subjects.addAll(subjects);
    }

    public void addSubjects(Subject subject){
        this.subjects.add(subject);
    }

    public void deleteSubjects(){
        this.subjects.clear();
    }
}
