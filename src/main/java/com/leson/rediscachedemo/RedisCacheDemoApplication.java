package com.leson.rediscachedemo;

import com.leson.rediscachedemo.model.User;
import com.leson.rediscachedemo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication
@EnableCaching
public class RedisCacheDemoApplication implements CommandLineRunner {
    private UserRepository userRepository;

    @Autowired
    public RedisCacheDemoApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisCacheDemoApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //Populating embedded database here
        log.info("Saving users. Current user count is {}.", userRepository.count());
        User shubham = new User("Shubham", 2000);
        User pankaj = new User("Pankaj", 29000);
        User lewis = new User("Lewis", 550);

        userRepository.save(shubham);
        userRepository.save(pankaj);
        userRepository.save(lewis);
        log.info("Done saving users. Data: {}.", userRepository.findAll());
    }
}
