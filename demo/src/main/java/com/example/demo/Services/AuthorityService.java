package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Authority;
import com.example.demo.Repositories.AuthorityRepo;


@Service
public class AuthorityService {
    
    @Autowired
    AuthorityRepo authorityRepo;

    public void createAuthority(Authority authority){
        authorityRepo.save(authority);
    }

    public List<Authority> findAll(){
        return authorityRepo.findAll();
    }

    public Optional<Authority> findByID(long id){
        return authorityRepo.findById(id);
    }

    public Optional<Authority> findByRoleCode(String roleCode){
        return authorityRepo.findByRoleCode(roleCode);
    }
}
