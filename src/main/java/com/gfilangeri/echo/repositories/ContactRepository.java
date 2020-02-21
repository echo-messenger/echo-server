package com.gfilangeri.echo.repositories;

import com.gfilangeri.echo.entities.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
}
