package com.test.helloBackEnd.rest;

import com.test.helloBackEnd.jpa.Contact;
import com.test.helloBackEnd.jpa.ContactsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class ContactsController {

    private static Logger logger = LoggerFactory.getLogger(ContactsController.class);

    private ContactsRepository repository;

    @Autowired
    public ContactsController(ContactsRepository repo) {

        this.repository = repo;

        if(repository.count() == 0) {
            repository.save(new Contact(0,"Roman"));
            repository.save(new Contact(1,"Darlene"));
            repository.save(new Contact(2,"Jhoyce"));
            repository.save(new Contact(2, "Myynt"));
        }
    }

    @GetMapping(value = "/hello/contacts")
    String getContacts(@RequestParam("nameFilter") String regexp) {

        logger.info("Parameter passed: " + regexp);

        List<Contact> contacts = StreamSupport.stream(repository.findAll().spliterator(), true)
                .filter( contact -> {
                    return contact.getName().matches(regexp);
                } ).collect(Collectors.toList());

        return "Contacts" + contacts;

    }

    @GetMapping("/hello/contact/{Id}")
    ResponseEntity<Contact> getContactById( @PathVariable("Id") long id ) {

        Contact contact = repository.findOne(id);
        HttpStatus status = contact == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        ResponseEntity<Contact> response = new ResponseEntity<Contact>(contact, status);
        return response;

    }

    // put new contact
    @PutMapping(value = "/hello/contact")
    ResponseEntity putContact(@RequestBody Contact contact) {

        if(repository.exists(contact.getId())) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("error", "Contact with given id already exists");
            return new ResponseEntity(headers, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    // replace old one
    @PostMapping("/hello/contact")
    HttpStatus postNewContact(@RequestBody Contact contact) {
        repository.save(contact);
        return HttpStatus.OK;
    }

    @DeleteMapping("/hello/contacts/{Id}")
    HttpStatus deleteContact(@PathVariable long id) {
        if(repository.exists(id)) {
            repository.delete(id);
            return HttpStatus.OK;
        } else {
            return HttpStatus.NOT_FOUND;
        }
    }

}
