package com.example.chathub;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
    private final MongoClient MONGODB_CLIENT;
    public MongoDatabase MONGODB_DATABASE;

    public MongoDB(String database_url) {
        MONGODB_CLIENT = MongoClients.create(database_url);
        MONGODB_DATABASE = MONGODB_CLIENT.getDatabase("ChatHub");

        System.out.println("Connected to database successfully");
    }

    public void DisconnectMongo() {
        MONGODB_CLIENT.close();
    }
}
