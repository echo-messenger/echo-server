package com.gfilangeri.echo.repositories;

import com.gfilangeri.echo.entities.Message;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends CassandraRepository<Message, String> {
}
