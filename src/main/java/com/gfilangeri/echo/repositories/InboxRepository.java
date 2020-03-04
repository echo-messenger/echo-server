package com.gfilangeri.echo.repositories;

import com.gfilangeri.echo.entities.Inbox;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxRepository extends CassandraRepository<Inbox, String> {
}