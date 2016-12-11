package com.applexis.aimos.model.service;

import com.applexis.aimos.model.entity.Dialog;
import com.applexis.aimos.model.entity.Message;

import java.util.List;

public interface MessageService {

    Message sendMessage(Message message);

    List<Message> getTop10(Dialog dialog);

}
