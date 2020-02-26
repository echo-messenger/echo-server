package com.gfilangeri.echo.repositories;

import com.gfilangeri.echo.entities.Inbox;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboxRepository extends CassandraRepository<Inbox, String> {
}