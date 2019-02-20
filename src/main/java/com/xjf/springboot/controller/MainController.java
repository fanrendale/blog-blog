package com.xjf.springboot.controller;

import com.xjf.springboot.domain.Authority;
import com.xjf.springboot.domain.User;
import com.xjf.springboot.service.AuthorityService;
import com.xjf.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页控制器
 * @author xjf
 * @date 2019/2/5 10:10
 */
@Controller
public class MainController {
    private static final Long ROLE_USER_AUTHORITY_ID = 2L;
    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @GetMapping("/")
    public String root(){
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError",true);
        model.addAttribute("errorMsg","登录失败，用户名或密码错误！");

        return "login";
    }

    /**
     * 获取注册页面
     * @return
     */
    @GetMapping("/register")
    public String register(){
        return "register";
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String registerUser(User user){
        //将权限添加到用户上
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
        user.setAuthorities(authorities);

        //对密码进行BCryptPasswordEncoder加密
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword().trim());
        System.out.println("BCrypt加密后："+encodedPassword);
        user.setPassword(encodedPassword);
        
        userService.saveOrUpdateUser(user);

        return "redirect:/login";
    }
}
