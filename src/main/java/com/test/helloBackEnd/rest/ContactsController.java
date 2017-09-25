package com.test.helloBackEnd.rest;

import com.test.helloBackEnd.jpa.Contact;
import com.test.helloBackEnd.jpa.ContactsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContactsController {

    private ContactsRepository repository;

    @Autowired
    public ContactsController(ContactsRepository repo) {

        this.repository = repo;

        if(repository.count() == 0) {
            repository.save(new Contact("Roman"));
            repository.save(new Contact("Darlene"));
            repository.save(new Contact("Jhoyce"));
            repository.save(new Contact("Myynt"));
        }
    }

    @GetMapping("/hello/contacts")
    Iterable<Contact> getContacts() {
        return repository.findAll();
    }

}
