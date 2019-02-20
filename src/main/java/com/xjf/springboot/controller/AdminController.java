package com.xjf.springboot.controller;

import com.xjf.springboot.vo.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台管理 控制器
 * @author xjf
 * @date 2019/2/5 10:10
 */
@Controller
@RequestMapping("/admins")
public class AdminController {

    /**
     * 获取后台管理主页面
     * @param model
     * @return
     */
    @GetMapping
    public ModelAndView listUsers(Model model){
        List<Menu> list = new ArrayList<>();
        list.add(new Menu("用户管理","/users"));
        list.add(new Menu("角色管理","/roles"));
        list.add(new Menu("博客管理","/blog"));
        list.add(new Menu("评论管理","/commits"));
        model.addAttribute("list",list);

        return new ModelAndView("admins/index","model",model);
    }
}
