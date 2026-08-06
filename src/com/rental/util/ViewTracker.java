package com.rental.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.Random;

public class ViewTracker {
    private static final ViewTracker instance = new ViewTracker();
    // Maps propertyId to active view count
    private final Map<Integer, AtomicInteger> propertyViews = new ConcurrentHashMap<>();
    private final Random random = new Random();

    private ViewTracker() {}

    public static ViewTracker getInstance() {
        return instance;
    }

    public int getActiveViews(int propertyId) {
        // We will seed the initial views with a realistic random number between 5 and 25
        // if this is the first time it is queried, to simulate high traffic.
        return propertyViews.computeIfAbsent(propertyId, k -> new AtomicInteger(random.nextInt(21) + 5)).get();
    }

    public void incrementView(int propertyId) {
        propertyViews.computeIfAbsent(propertyId, k -> new AtomicInteger(random.nextInt(21) + 5)).incrementAndGet();
    }
}
