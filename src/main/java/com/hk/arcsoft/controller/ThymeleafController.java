package com.hk.arcsoft.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by HomKey on 2020/1/9.
 */
@Controller
public class ThymeleafController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

}
