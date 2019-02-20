package com.xjf.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Blog 控制器
 * @author xjf
 * @date 2019/2/5 10:10
 */
@Controller
@RequestMapping("/blogs")
public class BlogController {

    /**
     * 根据关键字查询博客
     * @param order         排序方式
     * @param keyword       关键字
     * @return
     */
    @GetMapping
    public String listBlogs(@RequestParam(value = "order",required = false,defaultValue = "new") String order,
                            @RequestParam(value = "keyword",required = false,defaultValue = "") Long keyword){
        System.out.println("order:"+order+";keyword:"+keyword);
        return "redirect:/index?order="+order+"&keyword="+keyword;
    }

}
