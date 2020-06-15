package com.leson.rediscachedemo.controller;

import com.leson.rediscachedemo.exception.UserNotFoundException;
import com.leson.rediscachedemo.model.User;
import com.leson.rediscachedemo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users", key = "#userId", unless = "#result.followers < 12000")
    @GetMapping(value = "users/{userId}")
    public User getUser(@PathVariable String userId) {
        log.info("Getting user with ID {}", userId);
        if (!userRepository.findById(Long.valueOf(userId)).isPresent()) {
            log.warn("User with ID : {} not found!", userId);
            throw new UserNotFoundException("User with ID : " + userId + " not found!");
        }
        return userRepository.findById(Long.valueOf(userId)).get();
    }

    @PostMapping(value = "/users")
    public User addUser(@RequestBody User user) {
        log.info("Adding new user with name {}", user.getName());
        userRepository.save(user);
        return user;
    }

    @CachePut(value = "users", key = "#userId")
    @PutMapping(value = "/users/{userId}")
    public User updatePersonById(@PathVariable String userId, @RequestBody User user) {
        if (!userRepository.findById(Long.valueOf(userId)).isPresent()) {
            throw new UserNotFoundException("User with ID : " + userId + " not found!");
        }
        User userUpdate = userRepository.findById(Long.valueOf(userId)).get();
        userUpdate.setName(user.getName());
        userUpdate.setFollowers(user.getFollowers());
        userRepository.save(userUpdate);
        return user;
    }

    @CacheEvict(value = "users", allEntries = true)
    @DeleteMapping(value = "/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Deleting user with ID {}", userId);
        userRepository.deleteById(userId);
    }
}
