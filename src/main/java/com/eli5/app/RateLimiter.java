package com.eli5.app;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    private static final int MAX_REQUESTS = 10; // per minute
    private static final long WINDOW_MS = 60_000;

    private static final ConcurrentHashMap<String, Bucket> buckets =
            new ConcurrentHashMap<>();

    public static synchronized boolean allowRequest(String ip) {
        long now = Instant.now().toEpochMilli();

        Bucket bucket = buckets.getOrDefault(ip, new Bucket(0, now));

        if (now - bucket.startTime > WINDOW_MS) {
            bucket = new Bucket(0, now);
        }

        bucket.count++;
        buckets.put(ip, bucket);

        return bucket.count <= MAX_REQUESTS;
    }

    private static class Bucket {
        int count;
        long startTime;

        Bucket(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
}
