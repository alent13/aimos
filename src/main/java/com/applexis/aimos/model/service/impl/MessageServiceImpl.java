package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.Message;
import com.applexis.aimos.model.repository.MessageRepository;
import com.applexis.aimos.model.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    public MessageRepository repository;

    @Override
    public Message sendMessage(Message message) {
        return repository.save(message);
    }

    @Override
    public List<Message> getTop10(Dialog dialog) {
        return repository.findFirst10ByDialogOrderByIdDesc(dialog);
    }
}
