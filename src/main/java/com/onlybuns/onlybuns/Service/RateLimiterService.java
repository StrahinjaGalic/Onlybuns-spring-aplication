package com.onlybuns.onlybuns.Service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<Long, Queue<Long>> userRequestTimestamps = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 5; // Maximum allowed requests
    private static final long TIME_WINDOW_MS = 60 * 1000; // 1 minute

    public boolean isRequestAllowed(Long userId) {
        long currentTime = System.currentTimeMillis();

        // Get the user's request timestamps, or create a new queue if absent
        userRequestTimestamps.putIfAbsent(userId, new LinkedList<>());
        Queue<Long> timestamps = userRequestTimestamps.get(userId);

        synchronized (timestamps) {
            // Remove timestamps older than the time window
            while (!timestamps.isEmpty() && (currentTime - timestamps.peek() > TIME_WINDOW_MS)) {
                timestamps.poll();
            }

            // Check if the user is within the allowed request limit
            if (timestamps.size() < MAX_REQUESTS) {
                timestamps.add(currentTime);
                return true;
            } else {
                return false;
            }
        }
    }
}
