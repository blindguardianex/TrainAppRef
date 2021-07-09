package com.smartru.telegram.model;

import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UserProblemContext {

    private static final Map<Long, UserProblemCache> userCacheMap = Collections.synchronizedMap(new HashMap<>());

    public static boolean contains(long userId){
        return userCacheMap.containsKey(userId);
    }

    public static void add(User user){
        if (userCacheMap.containsKey(user.getId())){
            return;
        }
        UserProblemCache cache = new UserProblemCache();
        cache.setId(user.getId());
        cache.setUsername(user.getUserName());
        cache.setEquipped(false);
        userCacheMap.put(cache.getId(), cache);
    }

    public static UserProblemCache get(long userId){
        return userCacheMap.get(userId);
    }

    public static void clearUserCache(long userId){
        userCacheMap.remove(userId);
    }
}
