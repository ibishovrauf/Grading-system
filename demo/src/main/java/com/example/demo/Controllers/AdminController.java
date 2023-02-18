package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Models.Authority;
import com.example.demo.Models.User;
import com.example.demo.Services.AuthorityService;
import com.example.demo.Services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserService userService;

    @GetMapping("/authorities")
    public String allAurorities(final Model model){
        List<Authority> authorities = authorityService.findAll();
        model.addAttribute("authorities", authorities);
        return "Authorities";
    }

    @GetMapping("/Authority_form")
    public String createAuthorityGet(final Model model) {
        model.addAttribute("authority", new Authority());
        return "Authority_form";
    }

    @PostMapping("/Authority_form")
    public String createAuthorityPost(Authority authority, Model model) {
        authorityService.createAuthority(authority);
        model.addAttribute("authority", authority);
        return "redirect:/admin/authorities";
    }

    @GetMapping("/group-admins")
    public String groupAdmins(Model model){
        Authority authority = authorityService.findByRoleCode("groupAdmin").get();
        List<User> users = userService.findByAuthority(authority);
        model.addAttribute("users", users);
        return "groupAdmins";
    }

    @GetMapping("/create-group-admin")
    public String createGroupAdmin(Model model){
        model.addAttribute("user", new User());
        return "createGroupAdmin";
    }

    @PostMapping("/create-group-admin")
    public String createGroupAdminPost(User user){
        Authority authority = authorityService.findByRoleCode("groupAdmin").get();
        user.setAuthorities(List.of(authority));
        userService.register(user);
        return "redirect:/admin/group-admins";
    }
}
