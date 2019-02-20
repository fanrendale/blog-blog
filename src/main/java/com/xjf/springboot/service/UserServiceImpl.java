package com.xjf.springboot.service;

import com.xjf.springboot.domain.User;
import com.xjf.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 用户服务接口实现
 * @author xjf
 * @date 2019/2/13 15:38
 */
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRespository;

    @Transactional
    @Override
    public User saveOrUpdateUser(User user) {
        return userRespository.save(user);
    }

    @Transactional
    @Override
    public User registerUser(User user) {
        return userRespository.save(user);
    }

    @Transactional
    @Override
    public void removeUser(Long id) {
        userRespository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        /*User user = new User(id,"xjf","1053314919@qq.com");
        Example<User> example = Example.of(user);
        Optional<User> optional = userRespository.findOne(example);
        return optional.get();*/
        return userRespository.findById(id).get();
    }

    @Override
    public Page<User> listUsersByNameLike(String name, Pageable pageable) {
        //模糊查询
        name = "%"+name+"%";
        return userRespository.findByNameLike(name,pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRespository.findByUsername(username);
    }
}
