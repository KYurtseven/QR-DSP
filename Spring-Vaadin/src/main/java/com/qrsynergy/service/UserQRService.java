package com.qrsynergy.service;

import com.qrsynergy.model.User;
import com.qrsynergy.model.UserQR;
import com.qrsynergy.repository.UserQRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserQRService {

    @Autowired
    UserQRRepository userQRRepository;

    public UserQR getUserQR(User user){
        return userQRRepository.findByO_info(user.getEmail());
    }
}
