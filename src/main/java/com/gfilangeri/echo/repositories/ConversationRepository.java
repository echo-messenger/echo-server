package com.gfilangeri.echo.repositories;

import com.gfilangeri.echo.entities.Conversation;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends CassandraRepository<Conversation, String> {
}