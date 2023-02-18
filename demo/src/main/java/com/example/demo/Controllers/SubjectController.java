package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.Models.Exam;
import com.example.demo.Models.Semester;
import com.example.demo.Models.Subject;
import com.example.demo.Models.Year;
import com.example.demo.Services.ExamService;
import com.example.demo.Services.SubjectService;

@Controller
@RequestMapping("/admin")
public class SubjectController {
	
	@Autowired
	private SubjectService subjectService;

	@Autowired
	private ExamService examService;

	@GetMapping("/subjects")
	String subject(Model model){
		List<Subject> subjects = subjectService.findAll();
		model.addAttribute("subjects", subjects);
		return "List";
	}
	
    @GetMapping("/subject/{subjectId}")
    public String getSubjectDetailsById(@PathVariable("subjectId") int SubjectId, Model model){
        Subject subject = subjectService.findById(SubjectId).get();

        List<Exam> exams = subject.getExams();
		
        model.addAttribute("exams", exams);
        model.addAttribute("subject", subject);
        return "Details";
    }

	@GetMapping("/create-subject")
	String createSubject(Model model){
		model.addAttribute("years", Year.values());
		model.addAttribute("semesters", Semester.values());
		model.addAttribute("subject", new Subject());
		return "createSubject";
	}

	@PostMapping("/create-subject")
	String createSubjectPost(Model model, Subject subject, HttpSession session, @RequestParam Integer numExams){

		session.setAttribute("subject", subject);
		List<Exam> exams = new ArrayList<>();
		for(int i = 0; i < numExams; i++){
		  exams.add(new Exam());
		}

		ExamForm examForm = new ExamForm();
		examForm.setExams(exams);
	  
		model.addAttribute("examForm", examForm);
		return "createSubject";
	}

	@PostMapping("/create-exams")
	String createExam(Model model, ExamForm examForm, HttpSession session){
		Subject subject =  (Subject) session.getAttribute("subject");
		session.removeAttribute("subject");
		subjectService.save(subject);

		List<Exam> exams = examForm.getExams();

		for (Exam exam : exams) {
			exam.setSubject(subject);
			examService.save(exam);
		}
		subject.setExams(examForm.getExams());


		return "redirect:/admin/add-subjects";
	}
}

class ExamForm {
	private List<Exam> exams;

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public List<Exam> getExams() {
		return exams;
	}
} 