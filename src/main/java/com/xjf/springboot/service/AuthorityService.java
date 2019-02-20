package com.xjf.springboot.service;

import com.xjf.springboot.domain.Authority;
import com.xjf.springboot.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Authority 服务接口
 *
 * @Author: xjf
 * @Date: 2019/2/17 20:15
 */
public interface AuthorityService {

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}
