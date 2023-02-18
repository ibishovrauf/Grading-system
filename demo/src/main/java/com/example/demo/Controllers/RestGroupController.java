package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Exam;
import com.example.demo.Models.Grade;
import com.example.demo.Models.Group;
import com.example.demo.Models.Subject;
import com.example.demo.Models.User;
import com.example.demo.Models.Year;
import com.example.demo.Services.ExamService;
import com.example.demo.Services.GradeService;
import com.example.demo.Services.GroupService;
import com.example.demo.Services.SubjectService;
import com.example.demo.Services.UserService;


@RestController
public class RestGroupController {

    @Autowired
    SubjectService subjectService;

    @Autowired
    GroupService groupService;

    @Autowired
    UserService userService;

    @Autowired
    GradeService gradeService;

    @Autowired
    ExamService examService;

    @GetMapping("/group-subjects")
    public List<Object> getSearchUserProfiles(HttpServletRequest request) {
        String Subject = request.getParameter("Subject");
        String courceId = request.getParameter("courceId");
        List<User> users = new ArrayList<>();
        Year year = Year.valueOf(courceId);
        List<Group> groups = groupService.findByYear(year);
        for (Group group : groups) {
            users.addAll(group.getUserList());
        }

        String[] SubjectArray = Subject.split(", ");
        List<Long> SubjectList = Arrays.stream(SubjectArray)
                                        .map(Long::parseLong)
                                        .collect(Collectors.toList());
        List<Subject> subjects = subjectService.findById(SubjectList);
        List<Exam> exams = new ArrayList<>();
        for (Subject subject : subjects) {
            exams.addAll(subject.getExams());
        }

        List<List<Double>> scores = new ArrayList<>();
        double score, maximumScore;
        List<Grade> grade;
        List<List<Object>> userPropetries= new ArrayList<>();
        List<Object> userProperty;
        for (User user : users) {
            score = 0; maximumScore = 0;
            for (Exam exam : exams) {
                // if exam annonced
                grade = gradeService.findByStudentAndExam(user, exam);
                if (grade.isEmpty() == false) {
                    if (grade.get(0).getScore() != null)
                        score += grade.get(0).getScore() * grade.get(0).getExam().getCoefficient()/groups.get(0).getCredits();
                    maximumScore += grade.get(0).getExam().getCoefficient()/groups.get(0).getCredits()*20;  
                }
            }
            scores.add(List.of(score, maximumScore));
            userProperty = new ArrayList<>();
            userProperty.add(user.getUsername());
            userProperty.add(user.getFirstName());
            userProperty.add(user.getLastName());
            userPropetries.add(userProperty);
        }
        
        return List.of(userPropetries, scores);
    }

    @GetMapping("/grades-subjects")
    public List<Object> gpaCalculator(Principal principal){
        User user = userService.loadUserByUsername(principal.getName());
        List<Grade> grades = gradeService.findByStudent(user);
        Group group = user.getParticipateInGroup();
        List<Subject> subjects = group.getSubjects();
        return List.of(grades, subjects);
    }
    
    @GetMapping("/ad")
    public Group test(Principal principal){
        User user = userService.loadUserByUsername(principal.getName());
        return user.getGroup();
    }

    @GetMapping("/save")
    public ResponseEntity<String> save(HttpServletRequest request, Principal principal){
        User user = userService.loadUserByUsername(principal.getName());
        String ids = request.getParameter("ids");
        String values = request.getParameter("values");
        String[] idsArray = ids.split(", ");
        String[] valuesArray = values.split(", ");
        System.out.println(ids);
        System.out.println(values);
        List<Integer> idsList = new ArrayList<>();
        List<Double> valuesList = new ArrayList<>(); 
        double d;
        Exam exam;
        int integer;
        Grade grade;
        for (int i=0; i<valuesArray.length; i++) {
            try {
                if (valuesArray[i] != "None") {
                    d = Double.parseDouble(valuesArray[i]);
                    integer = Integer.parseInt(idsArray[i]);
                    exam = examService.findById((long)integer).get();
                    List<Grade> grades = gradeService.findByStudentAndExam(user, exam);
                    if(grades.isEmpty()){
                        grade = new Grade();
                        grade.setExam(exam);
                        grade.setStudent(user);
                        grade.setScore(d);
                        grade.setAnnonced(false);
                        gradeService.save(grade);
                    } else{
                        grade = grades.get(0);
                        grade.setScore(d);
                        grade.setAnnonced(false);
                        gradeService.save(grade);
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(e);
                System.out.println(e);
                System.out.println(e);
                System.out.println(e);
            }
          }
        return ResponseEntity.ok().body("Example created successfully with parameter: " + "parameter");
    }
}
