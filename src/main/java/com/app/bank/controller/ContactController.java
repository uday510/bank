package com.app.bank.controller;

import com.app.bank.model.Contact;
import com.app.bank.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/contact")
    public Contact saveContactInquiryDetails(@RequestBody Contact contact) {
       return contactService.saveContactDetails(contact);
    }

}
