package com.convict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Convict
 * @Date 2018/10/16 11:23
 */

@Controller
@RequestMapping("/show")
public class ShowPageController {

    @RequestMapping("/admin/{pageName}")
    public String admin(@PathVariable String pageName) {
        return "admin/" + pageName;
    }

    @RequestMapping("/rent/{pageName}")
    public String rent(@PathVariable String pageName) {
        return "rent/" + pageName;
    }

    @RequestMapping("/rent/impl/{pageName}")
    public String rentImpl(@PathVariable String pageName) {
        return "rent/impl/" + pageName;
    }

    @RequestMapping("/spider/{pageName}")
    public String spider(@PathVariable String pageName) {
        return "spider/" + pageName;
    }

    @RequestMapping("/log/{pageName}")
    public String log(@PathVariable String pageName) {
        return "log/" + pageName;
    }

    @RequestMapping("/log/impl/{pageName}")
    public String logImpl(@PathVariable String pageName) {
        return "log/impl/" + pageName;
    }

    @RequestMapping("/other/{pageName}")
    public String other(@PathVariable String pageName) {
        return "other/" + pageName;
    }
}
