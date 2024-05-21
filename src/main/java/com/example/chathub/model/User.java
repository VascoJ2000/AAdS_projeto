package com.example.chathub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {

    @Id
    private String id;

    private String email;
    private String password;

    public User(String email) {
        this.email = email;
        this.password = null;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        if(password == null) {
            return String.format("User[id='%s', email='%s']", id, email);
        }
        return String.format("User[id='%s', email='%s', password='%s']", id, email, password);
    }
}
