/*
package com.xjf.springboot.repository.impl;

import com.xjf.springboot.domain.User;
import com.xjf.springboot.repository.UserRespository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

*/
/**
 * @author xjf
 * @date 2019/1/28 13:46
 *//*

@Repository
public class UserRespositoryImpl implements UserRespository {
    private static AtomicLong counter = new AtomicLong();
    private final ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<>();

    @Override
    public User saveOrUpdteUser(User user) {
        Long id = user.getId();
        if (id == null){
            id = counter.incrementAndGet();
            user.setId(id);
        }

        userMap.put(id,user);

        return null;
    }

    @Override
    public void deleteUserById(Long id) {
        this.userMap.remove(id);
    }

    @Override
    public User getUserById(Long id) {
        return this.userMap.get(id);
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(this.userMap.values());
    }
}
*/
