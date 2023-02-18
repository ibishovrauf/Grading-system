package com.example.demo.Models;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "user_key")
    private String password;

    @Column(name = "Created_on")
    private Date created_at;

    @Column(name = "First_name")
    private String firstName;

    @Column(name = "Last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private boolean enable;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "AUTH_USER_AUTHORITY", joinColumns = @JoinColumn(referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(referencedColumnName ="id"))
    private List<Authority> authorities;

    @Column
    private Double maximumScore;

    @Column
    private Double currecntScore;

    @JsonManagedReference
    @OneToOne(mappedBy = "adminUser")
    private Group group;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group participateInGroup;

    static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public User(){
        this.enable = true;
        this.created_at = new Date(System.currentTimeMillis());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enable;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enable;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enable;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }

    public Double getMaximumScore() {
        return maximumScore;
    }

    public void setMaximumScore(Double maximumScore) {
        this.maximumScore = maximumScore;
    }

    public void addToMaximumScore(Double score) {
        if (this.maximumScore != null)
            this.maximumScore += score;
        else
            this.maximumScore = score;
    }

    public Double getCurrecntScore() {
        return currecntScore;
    }

    public void setCurrecntScore(Double currecntScore) {
        this.currecntScore = currecntScore;
    }
    
    public void addCurrentScore(Double score){
        if (this.currecntScore == null) {
            this.setCurrecntScore(score);
        }else{
            this.currecntScore += score;
        }
    }

    public void setAuthorities(List<Authority> authorities) {
        if (this.authorities != null) {
            this.authorities.clear();
            for (Authority authority : authorities) {
                this.authorities.add(authority);
            }
        }else{
            this.authorities = authorities;
        }
    }

    public void setAuthorities(Authority authorities) {
        this.authorities.add(authorities);
    }

    public void setPassword(String password) {
        this.password = encoder.encode(password);
    }

    public void setPassword(String password, boolean encoding) {
        if(encoding==true)
            this.password = encoder.encode(password);
        else
            this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean getEnable(){
        return this.enable;
    }

    public String[] getAuthorityNames(){
        String[] authorityNames = new String[this.authorities.size()];
        int i = 0;
        for (Authority authority : this.authorities) {
            authorityNames[i++] = authority.getRoleCode();
        }
        return authorityNames;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setParticipateInGroup(Group participateInGroup) {
        this.participateInGroup = participateInGroup;
    }

    public Group getParticipateInGroup() {
        return participateInGroup;
    }
}
