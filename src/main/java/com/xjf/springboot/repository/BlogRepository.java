package com.xjf.springboot.repository;

import com.xjf.springboot.domain.Blog;
import com.xjf.springboot.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BLog 仓库
 * @author xjf
 * @date 2019/2/25 19:42
 */
@Repository
public interface BlogRepository extends JpaRepository<Blog,Long> {
    /**
     * 根据用户名分页查询用户列表
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLikeOrderByCreateTimeDesc(User user, String title, Pageable pageable);

    /**
     * 根据用户名分页查询用户列表
     * @param user
     * @param title
     * @param pageable
     * @return
     */
    Page<Blog> findByUserAndTitleLike(User user,String title, Pageable pageable);
}
