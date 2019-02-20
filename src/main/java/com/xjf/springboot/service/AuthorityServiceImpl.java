package com.xjf.springboot.service;

import com.xjf.springboot.domain.Authority;
import com.xjf.springboot.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AuthorityService 的实现
 *
 * @Author: xjf
 * @Date: 2019/2/17 20:18
 */
@Service
public class AuthorityServiceImpl implements AuthorityService{
    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findById(id).get();
    }
}
