package com.test.helloBackEnd.jpa;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Contact {

    @Id
    private long id;

    @Basic
    private String name;

    private Contact() {}

    public Contact(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Contact) {
            Contact contact = (Contact) obj;
            return contact.getId() == id && contact.getName().equals(name);
        } else return false;
    }
}
