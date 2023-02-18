package com.example.demo.Controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.EncryptedDocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.example.demo.Models.Exam;
import com.example.demo.Models.Grade;
import com.example.demo.Models.User;
import com.example.demo.Services.ExamService;
import com.example.demo.Services.GradeService;
import com.example.demo.Services.GroupService;
import com.example.demo.Services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/admin")
public class GradeController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private GroupService groupService;

    @Autowired
    private ExamService examService;

    @Autowired
    private GradeService gradeService;

    @GetMapping("/add-grades")
    public String addGrades(Model model, Principal principal){
        User admin = userService.loadUserByUsername(principal.getName());
        model.addAttribute("subjects", admin.getGroup().getSubjects());
        return "addGrades";
    }

    @PostMapping("/add-grades")
    public String addGradePost(@RequestParam("inputGroupFile01") MultipartFile file, @RequestParam("subject") Long subject, Model model, @RequestParam("exam") Long exam_id, @RequestParam("numExams") String numExams) throws EncryptedDocumentException, IOException, URISyntaxException{

        String fileName = file.getOriginalFilename();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URIBuilder builder = new URIBuilder("http://localhost:5000/home");
        
        byte[] fileBytes = file.getBytes();
        String encodedFile = Base64.encodeBase64String(fileBytes);
        
        builder.addParameter("file_name", fileName);
        builder.addParameter("team", encodedFile);
        builder.addParameter("exam_id", numExams);
        HttpGet httpGet = new HttpGet(builder.build());
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(responseString, new TypeReference<Map<String, Object>>() {});
            List<Map<String, Object>> assa = (List<Map<String, Object>>) responseMap.get("data");

            Exam exam = examService.findById(exam_id).get();

            for (Map<String,Object> group : assa) {
                
                for (Map.Entry<String, Object> entry : group.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    User user = userService.loadUserByUsername(key);

                    List<Grade> grades = gradeService.findByStudentAndExam(user, exam);
                    Grade grade;
                    if (grades.size() != 0) {
                        grade = grades.get(0);
                        grade.setScore((Double) value);
                    } else{
                        grade = new Grade();
                        grade.setScore((Double)value);
                        grade.setStudent(user);
                        grade.setExam(exam);
                    }
                    grade.setAnnonced(true);
                    gradeService.save(grade);
                    user.addToMaximumScore(exam.getCoefficient());
                    user.setCurrecntScore(userService.calculateScore(user.getUsername()));
                    userService.register(user);
                }
            }
        } finally {
            response.close();
        }
        return "redirect:";
    }
}
