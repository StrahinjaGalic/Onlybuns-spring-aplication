package com.onlybuns.onlybuns.Service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class FollowRateLimiterService {
    private static final int MAX_FOLLOWS = 50;
    private static final long ONE_MINUTE = 60 * 1000L;

    private final Map<String, AtomicInteger> userFollowCounts = new ConcurrentHashMap<>();

    public boolean canFollow(String username)
    {
        AtomicInteger followCount = userFollowCounts.computeIfAbsent(username, k -> new AtomicInteger(0));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                followCount.set(0);
            }
        }, ONE_MINUTE);

        if(followCount.get() >= MAX_FOLLOWS) 
        {
            return false;
        }
        else
        {
            followCount.incrementAndGet();
            return true;
        }

    }
}
