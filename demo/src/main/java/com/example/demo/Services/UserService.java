package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Authority;
import com.example.demo.Models.Grade;
import com.example.demo.Models.User;
import com.example.demo.Repositories.GradeRepo;
import com.example.demo.Repositories.UserRepo;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	GradeRepo gradeRepo;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username);
	}

	public Optional<User> loadUserById(Long Id){
		return userRepo.findById(Id);
	}

	public List<User> findAll(){
		return userRepo.findAll();
	}

	public List<User> findByAuthority(Authority authorities){
		return userRepo.findByAuthorities(authorities);
	}

	public void register(User user){
		userRepo.save(user);
	}

	public double calculateScore(String username){
		User user = this.loadUserByUsername(username);
		List<Grade> grades = gradeRepo.findByStudent(user);
		
		double score = 0.0;
		for (Grade grade : grades) {
			if (grade.getScore() == null) {
				continue;
			}else{
				score += grade.getScore()/20*grade.getExam().getCoefficient();
			}
		}
		return score;
	}
}
