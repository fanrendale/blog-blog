package com.xjf.springboot.controller;

import com.xjf.springboot.domain.User;
import com.xjf.springboot.service.UserService;
import com.xjf.springboot.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 用户主页 控制器
 * @author xjf
 * @date 2019/2/5 10:10
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Autowired
    private UserDetailsService userDetailsService;

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
                                   @RequestParam(value = "keyword",required = false) String keyword){
        if (category != null){
            System.out.println("category:"+category);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?category="+category);
            return "/userspace/u";
        }else if (keyword != null && keyword.isEmpty() == false){
            System.out.println("keyword:"+keyword);
            System.out.println("selflink:"+"redirect:/u/"+username+"/blogs?keyword="+keyword);
            return "/userspace/u";
        }

        System.out.println("order:"+order);
        System.out.println("selflink:"+"redirct:/u/"+username+"/blogs?order="+order);
        return "/userspace/u";
    }

    @GetMapping("/{username}/blogs/{id}")
    public String listBlogsByOrder(@PathVariable("id") Long id){
        System.out.println("blogId:"+id);

        return "/userspace/blog";
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
}