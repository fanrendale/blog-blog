package com.xjf.springboot.service;

import com.xjf.springboot.domain.Blog;
import com.xjf.springboot.domain.User;
import com.xjf.springboot.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Blog服务接口实现.
 *
 * @author xjf
 * @date 2019/2/27 14:25
 */
@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    @Transactional
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    @Transactional
    public void removeBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Blog updateBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog getBlogById(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlogsByTitleLike(User user, String title, Pageable pageable) {
        //模糊查询
        title = "%" + title + "%";

        Page<Blog> blogs = blogRepository.findByUserAndTitleLikeOrderByCreateTimeDesc(user,title,pageable);

        return blogs;
    }

    @Override
    public Page<Blog> listBlogsByTitleLikeAndSort(User user, String title, Pageable pageable) {
        //模糊查询
        title = "%" + title + "%";

        Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user,title,pageable);

        return blogs;
    }

    @Override
    public void readingIncrease(Long id) {
        Blog blog = blogRepository.getOne(id);
        blog.setReading(blog.getReading() + 1);
        blogRepository.save(blog);
    }
}
