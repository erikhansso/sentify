package com.example.sentiment.entities;




import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class SentUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(unique=true)
    private String email;
    private String password;
    @ElementCollection
    private List<String> savedKeywords = new ArrayList<String>();

    public SentUser() {
    }

    public SentUser(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getSavedKeywords() {
        return savedKeywords;
    }

    public void setSavedKeywords(List<String> savedKeywords) {
        this.savedKeywords = savedKeywords;
    }

}
