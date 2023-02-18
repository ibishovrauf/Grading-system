package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Authority;
import com.example.demo.Models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    User findByUsername(String username);
    
    List<User> findByAuthorities(Authority authorities);
}
