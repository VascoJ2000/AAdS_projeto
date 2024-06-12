package org.chathub.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Users")
public class User {

    @Id
    private String id;

    private String email;
    private String password;

    private UserRole role;

    public User(String email, String password, UserRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public String toString() {
        if(password == null) {
            return String.format("User[id='%s', email='%s']", id, email);
        }
        return String.format("User[id='%s', email='%s', password='%s']", id, email, password);
    }
}
