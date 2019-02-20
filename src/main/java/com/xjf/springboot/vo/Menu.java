package com.xjf.springboot.vo;

import java.io.Serializable;

/**
 * 菜单 值对象
 * @author xjf
 * @date 2019/2/14 14:59
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = -7191995853228706699L;

    private String name;

    private String url;

    public Menu(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
