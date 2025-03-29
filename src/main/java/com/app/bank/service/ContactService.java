package com.app.bank.service;

import com.app.bank.model.Contact;
import com.app.bank.repository.ContactRepository;
import com.app.bank.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final Random random = new Random();

    public Contact saveContactDetails(Contact contact) {
        contact.setContactId(getServiceReqNumber());
        contact.setCreatedAt(DateTimeUtils.getUTCDate());
        return contactRepository.save(contact);
    }

    public String getServiceReqNumber() {
        int ranNum = random.nextInt(999999999 - 9999) + 9999;
        return "SR" + ranNum;
    }
}
