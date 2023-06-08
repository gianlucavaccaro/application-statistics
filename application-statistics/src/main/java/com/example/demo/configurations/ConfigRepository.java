package com.example.demo.configurations;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepository extends MongoRepository<Configuration, String> {
}
