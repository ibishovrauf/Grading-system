package com.example.demo.Controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Models.Authority;
import com.example.demo.Models.Exam;
import com.example.demo.Models.Grade;
import com.example.demo.Models.Group;
import com.example.demo.Models.Semester;
import com.example.demo.Models.Subject;
import com.example.demo.Models.User;
import com.example.demo.Models.Year;
import com.example.demo.Services.AuthorityService;
import com.example.demo.Services.GradeService;
import com.example.demo.Services.GroupService;
import com.example.demo.Services.SubjectService;
import com.example.demo.Services.UserService;


@Controller
@RequestMapping("/admin")
public class GroupController {

    @Autowired
    GroupService groupService;

    @Autowired
    GradeService gradeService;

    @Autowired
    AuthorityService authorityService;

    @Autowired
    UserService userService;

    @Autowired
    SubjectService subjectService;

    @GetMapping("/create-group")
    public String createGroup(Model model, Principal principal){
        Group group = new Group();
        
        model.addAttribute("years", Year.values());
        model.addAttribute("semesters", Semester.values());
        model.addAttribute("group", group);
        return "createGroup";
    }

    @PostMapping("/create-group")
    public String createdGroup(Group group, @RequestParam String student_numbers, @RequestParam("flexRadioDefault") int flexRadioDefault, Model model, Principal principal, HttpSession session){
        User authenticatedUser1 = userService.loadUserByUsername(principal.getName());
        User authenticatedUser = userService.loadUserById(authenticatedUser1.getId()).get();

        // set admin of group
        group.setAdminUser(authenticatedUser);
        group.setCredits(flexRadioDefault);

        // save changes
        groupService.save(group);

        Group group2 = groupService.findById(group.getId()).get();
        
        List<String> numbereWithError;
        List<User> users;
        
        StudentCreationResult studentCreationResult = createStudents(student_numbers, group2);

        numbereWithError = studentCreationResult.getNumbereWithError();
        users = studentCreationResult.getUsers();

        
        //edit admin role_cole to forbid create anothor group
        User authenticatedUserAdmin = userService.loadUserById(authenticatedUser1.getId()).get();

        Authority authorityActiveGroupAdmin = authorityService.findByRoleCode("activeGroupAdmin").get();
        authenticatedUserAdmin.setAuthorities(List.of(authorityActiveGroupAdmin));

        userService.register(authenticatedUserAdmin);

        session.setAttribute("numbereWithError",numbereWithError);
        session.setAttribute("group",group);
        session.setAttribute("users",users);

        return "redirect:/admin/My-group";
    }

    @GetMapping("/group-list")
    public String groupList(Model model){
        
        List<Group> groups = groupService.findAllGroups();
        model.addAttribute("groups", groups);
        return "List";
    }

    @GetMapping("/group/{GroupId}")
    public String getGroupDetailsById(@PathVariable("GroupId") int GroupId, Model model){
        Group group = groupService.findById(GroupId).get();
        List<User> users = group.getUserList();
        model.addAttribute("users", users);
        model.addAttribute("group", group);
        return "Details";
    }

    @GetMapping("/My-group")
    public String myGroup(Principal principal, Model model, HttpSession session){
        List<String> numbereWithError;
        try {

            numbereWithError = (List<String>)session.getAttribute("numbereWithError");
            session.removeAttribute("numbereWithError");            
        } catch (Exception e) {
            numbereWithError = List.of();
        }

        User user = userService.loadUserByUsername(principal.getName());
        Group group = user.getGroup();

        model.addAttribute("numbereWithError", numbereWithError);
        model.addAttribute("group", group);
        List<User> users = group.getUserList();
        model.addAttribute("users", users);
        return "Details";
    }

    @PostMapping("/My-group")
    public String addStudents(@RequestParam String student_numbers, Principal principal, HttpSession session){
        List<String> numbereWithError;
        List<User> users;

        // get authenticated user
        User user = userService.loadUserByUsername(principal.getName());
        user = userService.loadUserById(user.getId()).get();

        // get users group
        Group group = groupService.findById(user.getGroup().getId()).get();
        
        StudentCreationResult studentCreationResult = createStudents(student_numbers, group);

        numbereWithError = studentCreationResult.getNumbereWithError();
        users = studentCreationResult.getUsers();

        //save usernames that dont created
        session.setAttribute("numbereWithError", numbereWithError);
        session.removeAttribute("numbereWithError");

        return "redirect:/admin/My-group";
    }

    @GetMapping("/add-subjects")
    public String addSubject(Model model, Principal principal){
        User admin = userService.loadUserByUsername(principal.getName());
        Group group = groupService.findById(admin.getGroup().getId()).get();
        Year year = group.getYear();
        Semester semester = group.getSemester();
        List<Subject> exsistingSubjects = group.getSubjects();
        
        List<Subject> subjects = subjectService.findByYearAndSemester(year, semester);

        List<Subject> newSubjects = subjects.stream()
            .filter(subject -> !exsistingSubjects.contains(subject))
            .collect(Collectors.toList());

        model.addAttribute("add", true);
        model.addAttribute("subjects", newSubjects);
        return "List";
    }

    @PostMapping("/add-subjects")
    public String addSubjectPost(Model model, @RequestParam List<Long> subjectIds, Principal principal){
        List<Subject> subjects = new ArrayList<>();
        for (Subject subject : subjectService.findById(subjectIds)) {
            subjects.add(subject);
        }

        User groupAdmin = userService.loadUserByUsername(principal.getName());
        groupAdmin = userService.loadUserById(groupAdmin.getId()).get();
        Group group = groupService.findById(groupAdmin.getGroup().getId()).get();
        group.addSubjects(subjects);
        groupService.save(group);

        return "redirect:/admin/group-subjects";
    }

    @GetMapping("/group-subjects")
    public String groupSubjcets(Model model, Principal principal){
        User admin = userService.loadUserByUsername(principal.getName());
        admin = userService.loadUserById(admin.getId()).get();
        Group group = groupService.findById(admin.getGroup().getId()).get();
        List<Subject> groupSubjects = group.getSubjects();


        model.addAttribute("groupSubjects", groupSubjects);
        return "List";
    }

    @GetMapping("/student-arrangement")
    public String studentArrangement(Model model, Principal principal){
        User admin = userService.loadUserByUsername(principal.getName());
        Group group = admin.getGroup();

        List<User> students = group.getUserList();
        User user = students.get(0);
        double  credits = 0;
        for (Grade grades : gradeService.findByStudent(user)) {
            credits += grades.getExam().getCoefficient();
        }
        credits = credits*2/3;
        students.sort((user1, user2) -> Double.compare(user1.getCurrecntScore(), user2.getCurrecntScore()));
        Collections.reverse(students);
        model.addAttribute("credits", credits);
        model.addAttribute("students", students);
        return "List";
    }

    private StudentCreationResult createStudents(String student_numbers, Group group){
        List<String> numbereWithError = new ArrayList<String>();
        List<User> users = new ArrayList<User>();

        List<String> items = Arrays.asList(student_numbers.split("\\s*,\\s*"));

        Authority authority = authorityService.findByRoleCode("User").get();

        for (String item : items) {
            try {
                Integer.parseInt(item);
                User newUser = new User();
                if (userService.loadUserByUsername(item) != null) {
                    throw new UsernameNotFoundException("student_numbers");
                }
                newUser.setUsername(item);
                newUser.setEnable(false);
                newUser.setAuthorities(List.of(authority));
                newUser.setParticipateInGroup(group);
                userService.register(newUser);
                users.add(newUser);
            } catch (Exception e) {

                numbereWithError.add(item);
            }
        }
        StudentCreationResult studentCreationResult = new StudentCreationResult(users, numbereWithError);
        return studentCreationResult;
    }

    private class StudentCreationResult {
        private List<User> users;
        private List<String> numbereWithError;
    
        public StudentCreationResult(List<User> users, List<String> numbereWithError) {
            this.users = users;
            this.numbereWithError = numbereWithError;
        }
    
        public List<User> getUsers() {
            return users;
        }
    
        public List<String> getNumbereWithError() {
            return numbereWithError;
        }
    }
}