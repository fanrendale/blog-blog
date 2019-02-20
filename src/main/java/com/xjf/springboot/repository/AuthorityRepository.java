package com.xjf.springboot.repository;

import com.xjf.springboot.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: xjf
 * @Date: 2019/2/17 20:12
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
