package com.xjf.springboot.controller;

import com.xjf.springboot.domain.Blog;
import com.xjf.springboot.domain.User;
import com.xjf.springboot.service.BlogService;
import com.xjf.springboot.service.UserService;
import com.xjf.springboot.util.ConstraintViolationExceptionHandler;
import com.xjf.springboot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

/**
 * 用户主页 控制器
 * @author xjf
 * @date 2019/2/5 10:10
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Qualifier("userServiceImpl")
    @Autowired(required = false)
    private UserDetailsService userDetailsService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;

    @Value("$(file.server.url)")
    private String fileServerUrl;

    /**
     * 进入用户的博客主页
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username){
        System.out.println("username:"+username);

        return "/userspace/u";
    }

    @GetMapping("/{username}/blogs/edit")
    public String editBlog(){
        return "/userspace/blogedit";
    }

    /**
     * 获取个人设置页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    //判断当前用户是否等于要修改的用户，即用户只能修改自己的信息
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("fileServerUrl",fileServerUrl);      //文件服务器的地址返回给客户端

        return new ModelAndView("/userspace/profile","userModel",model);
    }

    /**
     * 保存个人设置
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username,User user){
        User originalUser = (User) userDetailsService.loadUserByUsername(username);
        originalUser.setEmail(user.getEmail());
        originalUser.setName(user.getName());

        //判断密码是否做了更改
        String originalPassword = originalUser.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String inputPassword = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(originalPassword,inputPassword);
        if (!isMatch){
            originalUser.setPassword(inputPassword);
        }

        userService.saveOrUpdateUser(originalUser);

        return "redirect:/u/"+username+"/profile";
    }

    /**
     * 获取编辑头像界面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView avatar(@PathVariable("username") String username,Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);

        return new ModelAndView("/userspace/avatar","userModel",model);
    }

    /**
     * 保存头像
     * @param username
     * @param user
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username,@RequestBody User user){
        String avatarUrl = user.getAvatar();

        User originalUser = (User)userDetailsService.loadUserByUsername(username);
        originalUser.setAvatar(avatarUrl);

        userService.saveOrUpdateUser(originalUser);

        return ResponseEntity.ok().body(new Response(true,"处理成功",avatarUrl));
    }

    /**
     * 查看某用户的博客
     * @param username
     * @param order         排序方式
     * @param category      类别
     * @param keyword       关键字
     * @return
     */
    @RequestMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value = "order",required = false,defaultValue = "new") String order,
                                   @RequestParam(value = "category",required = false) Long category,
                                   @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                                   @RequestParam(value = "async",required = false) boolean async,
                                   @RequestParam(value = "pageIndex",required = false,defaultValue = "0") int pageIndex,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "10") int pageSize,
                                   Model model){
        User user = (User)userDetailsService.loadUserByUsername(username);
        model.addAttribute("user",user);

        if (category != null){
            System.out.println("category:"+category);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?category="+category);
            return "/u";
        }

        Page<Blog> page = null;
        final String orderHot = "hot";
        final String orderNew = "new";
        if (orderHot.equals(order)){
            //最热查询
            Sort sort = new Sort(Sort.Direction.DESC,"reading","comments","likes");
            Pageable pageable =PageRequest.of(pageIndex,pageSize,sort);
            page = blogService.listBlogsByTitleLike(user,keyword,pageable);
        }
        if (orderNew.equals(order)){
            //最新查询
            Pageable pageable = PageRequest.of(pageIndex,pageSize);
            page = blogService.listBlogsByTitleLikeAndSort(user,keyword,pageable);
        }

        //当前所在页面的数据列表
        List<Blog> list = Optional.ofNullable(page.getContent()).orElse(null);

        model.addAttribute("order",order);
        model.addAttribute("page",page);
        model.addAttribute("blogList",list);

        //同步和异步刷新
        return (async?"/userspace/u::#mainContainerReplace" : "/userspace/u");
    }

    /**
     * 获取某用户的某篇博客展示界面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/{id}")
    public String getBlogById(@PathVariable("username") String username, @PathVariable("id") Long id, Model model){
        //每次读取，简单的可以认为阅读量增加一次
        blogService.readingIncrease(id);

        boolean isBlogOwner = false;

        //判断操作用户是否是博客的所有者
        //SecurityContextHolder.getContext().getAuthentication() 获取当前认证了的当事人
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                //是通过身份验证的
            SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                //不是匿名用户
            !("anonymousUser").equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())){
            //判断从spring security中获取的用户和调用接口传入的用户名是否相等
            User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null && username.equals(principal.getUsername())){
                isBlogOwner = true;
            }
        }

        model.addAttribute("isBlogOwner",isBlogOwner);
        model.addAttribute("blogModel",blogService.getBlogById(id));

        return "/userspace/blog";
    }

    /**
     * 删除博客
     * @param username 用户名
     * @param id       id
     * @return
     */
    @DeleteMapping("/{username}/blogs/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username,
                                               @PathVariable("id") Long id){
        try {
            blogService.removeBlog(id);
        }catch (Exception e){
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        String redirectUrl = "/u/"+username+"/blogs";
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirectUrl));
    }

    /**
     * 获取新增博客的页面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit")
    public ModelAndView createBlog(Model model){
        model.addAttribute("blog",new Blog(null,null,null));
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 获取修改博客的页面
     * @param username
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogs/edit/{id}")
    public ModelAndView updateBlog(@PathVariable("username") String username,@PathVariable("id") Long id,Model model){
        model.addAttribute("blog",blogService.getBlogById(id));
        return new ModelAndView("/userspace/blogedit","blogModel",model);
    }

    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogs/edit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username,@RequestBody Blog blog){
        User user = (User)userDetailsService.loadUserByUsername(username);

        blog.setUser(user);

        try {
            blogService.saveBlog(blog);
        }catch (ConstraintViolationException e){
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        }catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false,e.getMessage()));
        }

        String redirecUrl = "/u/" + username + "/blogs" + blog.getId();
        return ResponseEntity.ok().body(new Response(true,"处理成功",redirecUrl));
    }
}
