package com.example.phototeca.service.bot;

import com.example.phototeca.constants.Constants;
import com.example.phototeca.enumeration.UserState;
import com.example.phototeca.facade.subscriber.SubscriberFacade;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

import static com.example.phototeca.constants.Constants.START_TEXT;

public class SubscriberResponseHandler {

    private final SilentSender sender;

    private final SubscriberFacade subscriberFacade;

    private final Map<Long, UserState> chatStates;


    public SubscriberResponseHandler(final SilentSender sender,
                                     final SubscriberFacade subscriberFacade,
                                     final DBContext db) {
        this.sender = sender;
        this.subscriberFacade = subscriberFacade;
        chatStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyToStart(final long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(START_TEXT);
        sender.execute(message);
        chatStates.put(chatId, UserState.AWAITING_THRESHOLD_PERCENTAGE);
    }

    public void replyToMessage(final long chatId, final Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            subscriberFacade.deleteSubscriber(chatId);
            sendMessage(chatId, "Unsubscribed");
        }

        switch (chatStates.get(chatId)) {
            case AWAITING_THRESHOLD_PERCENTAGE -> {
                subscriberFacade.saveSubscriber(chatId, message);
                sendMessage(chatId, "Subscribed");
            }
            default -> sendMessage(chatId, "I did not expect that.");
        }
    }

    private void sendMessage(final long chatId, final String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        sender.execute(sendMessage);
    }

    public boolean userIsActive(final Long chatId) {
        return chatStates.containsKey(chatId);
    }

}
