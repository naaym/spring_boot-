package com.delta_nutritionMVC.delta.landing;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class LandingController {
    @GetMapping
    public String landing() {
        return "landing/landing";
    }
}
