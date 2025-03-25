package com.app.bank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NoticesController {

    @GetMapping("/notices")
    public String getNotices() {
        return "Notices from DB";
    }
}
