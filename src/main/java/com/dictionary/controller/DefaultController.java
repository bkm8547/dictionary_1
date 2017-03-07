package com.dictionary.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Bae on 2017-03-06.
 */
@Controller
public class DefaultController {
    @RequestMapping("/swagger")
    public String swagger(){
        return "redirect:swagger/dist/index.html";
    }
}
