package com.gfilangeri.echo.repositories;

import com.gfilangeri.echo.entities.Contact;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends CassandraRepository<Contact, String> {
}
