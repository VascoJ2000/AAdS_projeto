package com.example.chathub.repository;

import com.example.chathub.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String> {

    @Query("{'Users':  ?0}")
    List<Chat> findByUsers(String User);

    Optional<Chat> findByName(String name);
}
