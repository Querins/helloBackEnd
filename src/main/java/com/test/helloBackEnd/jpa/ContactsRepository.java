package com.test.helloBackEnd.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface ContactsRepository extends CrudRepository<Contact, Long> {
}
