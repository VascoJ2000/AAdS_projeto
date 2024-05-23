package com.example.chathub.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "Users")
public class User {

    @Id
    private String id;

    private String email;
    private String password;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        if(password == null) {
            return String.format("User[id='%s', email='%s']", id, email);
        }
        return String.format("User[id='%s', email='%s', password='%s']", id, email, password);
    }
}
