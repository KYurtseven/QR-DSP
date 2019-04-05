package com.qrsynergy.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DenemeController {

    @RequestMapping("/rest")
    public String rest(){
        return "Rest works";
    }

    @RequestMapping("/rest2")
    public String rest2(){
        return "rest2 works";
    }
}
