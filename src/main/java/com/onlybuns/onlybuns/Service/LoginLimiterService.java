package com.onlybuns.onlybuns.Service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LoginLimiterService {

    private static final int MAX_ATTEMPTS = 5;
    private static final long WINDOW_IN_MILLIS = TimeUnit.MINUTES.toMillis(1); // 1 minute
    private final ConcurrentHashMap<String, Attempt> attemptMap = new ConcurrentHashMap<>();

    public boolean isAllowed(String ipAddress) {
        long currentTime = System.currentTimeMillis();
        
        attemptMap.compute(ipAddress, (key, previousAttempt) -> {
            if (previousAttempt == null || currentTime - previousAttempt.timestamp > WINDOW_IN_MILLIS) {
                // Reset count and timestamp for the new window
                return new Attempt(1, currentTime);
            } else {
                // Increment attempt count
                return new Attempt(previousAttempt.attempts + 1, previousAttempt.timestamp);
            }
        });

        Attempt attempt = attemptMap.get(ipAddress);
        return attempt != null && attempt.attempts <= MAX_ATTEMPTS;
    }

    private static class Attempt {
        int attempts;
        long timestamp;

        Attempt(int attempts, long timestamp) {
            this.attempts = attempts;
            this.timestamp = timestamp;
        }
    }
}

