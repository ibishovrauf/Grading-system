package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Group;
import com.example.demo.Models.Year;

@Repository
public interface GroupRepo extends JpaRepository<Group, Long>{
    List<Group> findByYear(Year year);
    
}
