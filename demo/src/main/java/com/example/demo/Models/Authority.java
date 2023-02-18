package com.example.demo.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "AUTH_AUTHORITY")
public class Authority implements GrantedAuthority{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "role_code")
    private String roleCode;

    @Column(name = "Role_description")
    private String roleDescription;

    public Authority(){}
    
    @Override
    public String getAuthority() {
        return this.roleCode;
    }

    public long getId() {
        return id;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getRoleDescription() {
        return roleDescription;
    }
    
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
}
