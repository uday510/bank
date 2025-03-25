package com.app.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping(path = {"", "/" })
    public String Welcome() {
        return "Hello, World!";
    }

}
