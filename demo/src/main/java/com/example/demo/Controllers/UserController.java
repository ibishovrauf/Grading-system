package com.example.demo.Controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.Models.Grade;
import com.example.demo.Models.Group;
import com.example.demo.Models.Subject;
import com.example.demo.Models.User;
import com.example.demo.Models.Year;
import com.example.demo.Services.GradeService;
import com.example.demo.Services.GroupService;
import com.example.demo.Services.SubjectService;
import com.example.demo.Services.UserService;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    GroupService groupService;

    @Autowired
    SubjectService subjectService;

    @Autowired
    GradeService gradeService;

    @GetMapping("/activate-account")
    public String createUser(Model model){
        model.addAttribute("user", new User());
        return "createUser";
    }

    @PostMapping("/activate-account")
    public String createUserPost(User user, BindingResult bindingResult){
        
        User userInData = userService.loadUserByUsername(user.getUsername());
        User userInDatabase = userService.loadUserById(userInData.getId()).get();

        userInDatabase.setFirstName(user.getFirstName());
        userInDatabase.setLastName(user.getLastName());
        userInDatabase.setCreated_at(user.getCreated_at());
        userInDatabase.setEmail(user.getEmail());
        userInDatabase.setPassword(user.getPassword(), false);
        userInDatabase.setEnable(true);

        userService.register(userInDatabase);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userInDatabase, null, AuthorityUtils.createAuthorityList(userInDatabase.getAuthorityNames()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/userHome";
    }

    @GetMapping("/userHome")
    public String userHome(){
        return "userHome";
    }

    // Gpa calculator
    @GetMapping("/gpa-calculator")
    public String gpaCalculator(){
        return "gpa";
    }

    // arrangement
    @GetMapping("/group-arrangement/{groupId}")
    public String groupArrangement(@PathVariable("groupId") long groupId, Model model){
        Group group = groupService.findById(groupId).get();
        List<User> users = group.getUserList();
        users.sort((user1, user2) -> Double.compare(user1.getCurrecntScore(), user2.getCurrecntScore()));
        Collections.reverse(users);
        model.addAttribute("students", users);
        return "List";
    }
    
    // subject arrangement
    @GetMapping("/subject-arrangement/{courceId}")
    public String subjectArrangement(@PathVariable("courceId") String courceId, Model model){
        Year year = Year.valueOf(courceId);
        List<Subject> subjects = subjectService.findByYear(year);
        model.addAttribute("courceId", courceId);
        model.addAttribute("subjectsForArrangement", subjects);
        return "List";
    }

    //all university L1, L2, L3, L4
    @GetMapping("/course-arrangement/{courceId}")
    public String courceArrangement(@PathVariable("courceId") String courceId, Model model){
        List<User> users = new ArrayList<>();
        Year year = Year.valueOf(courceId);
        List<Group> groups = groupService.findByYear(year);
        for (Group group : groups) {
            users.addAll(group.getUserList());
        }
        users.sort((user1, user2) -> Double.compare(user1.getCurrecntScore()/user1.getMaximumScore(), user2.getCurrecntScore()/user2.getMaximumScore()));
        Collections.reverse(users);
        model.addAttribute("students", users);
        return "List";
    }
}