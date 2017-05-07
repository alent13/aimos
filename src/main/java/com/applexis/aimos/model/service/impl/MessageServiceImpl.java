package com.applexis.aimos.model.service.impl;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.Message;
import com.applexis.aimos.model.repository.MessageRepository;
import com.applexis.aimos.model.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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

    @Override
    public List<Message> getLastMessages(Dialog dialog, int offset, int limit) {
        int page = 0;
        if (limit != 0) {
            page = offset / limit;
        }
        return repository.findByDialogOrderByIdDesc(dialog, (Pageable) new PageRequest(page, limit));
    }

    @Override
    public Message lastMessageInDialog(Dialog dialog) {
        return repository.findFirstByDialogOrderByDatetimeDesc(dialog);
    }
}
