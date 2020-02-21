package com.gfilangeri.echo.repositories;

import com.gfilangeri.echo.entities.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, String> {
    List<Message> findAllByConversationIdEquals(String conversationId);
}
