package com.onlybuns.onlybuns.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserCleanUpService {
    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 0 L * ?")
    @Transactional
    public void deleteInactiveUsers()
    {
        userRepository.deleteByActivationTokenIsNotNull();
        System.out.println("Inactive users deleted successfully.");
    }

}
