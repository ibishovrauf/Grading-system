package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Group;
import com.example.demo.Models.Year;
import com.example.demo.Repositories.GroupRepo;

@Service
public class GroupService {
    @Autowired
    GroupRepo groupRepo;

    public void save(Group group){
        groupRepo.save(group);
    }

    public List<Group> findAllGroups(){
        return groupRepo.findAll();
    }

    public Optional<Group> findById(long id){
        return groupRepo.findById(id);
    }

    public List<Group> findByYear(Year year){
        return groupRepo.findByYear(year);
    }
}
