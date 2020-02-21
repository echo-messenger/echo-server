package com.gfilangeri.echo.repositories;

import com.gfilangeri.echo.entities.Conversation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends CrudRepository<Conversation, String> {
}