package com.history.bot.repository;

import com.history.bot.model.SentMessages;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SentMessagesRepository extends CrudRepository<SentMessages, Long> {

    List<SentMessages> findByUserChatId(Long chatId);
}
