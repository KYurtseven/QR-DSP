package com.qrsynergy.service;

import com.qrsynergy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qrsynergy.model.User;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    /**
     * Save user to the database
     * @param user user
     */
    public void saveUser(User user){
        userRepository.save(user);
    }

    /**
     * Fetch user from the database by email
     * @param email email of the user
     * @return user
     */
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }
}
