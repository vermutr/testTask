package com.example.phototeca.facade.subscriber;

import com.example.phototeca.entity.subscriber.Subscriber;
import com.example.phototeca.mapper.SubscriberMapper;
import com.example.phototeca.service.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;


// Do not need use facade here, but it is way to make code better in the future, Facade can be used with Cryptocurrency
@Component
@RequiredArgsConstructor
public class SubscriberFacade {

    private final SubscriberService subscriberService;

    @Value("${api.phototeca.subscribers.capacity:30}")
    private long availableSubscribersSize;

    @Transactional
    public void saveSubscriber(final Long chatId, final Message message) {
        verifyAvailableSubscribersCapacity();

        int percent = parsePercentFromMessage(message);
        Subscriber subscriber = SubscriberMapper.createSubscriber(chatId, percent);
        subscriberService.save(subscriber);
    }

    @Transactional
    public void deleteSubscriber(final Long chatId) {
        subscriberService.deleteSubscriber(chatId);
    }

    private void verifyAvailableSubscribersCapacity() {
        if (availableSubscribersSize < subscriberService.countSubscribers()) {
            throw new RuntimeException("Max subscriber capacity reached.");
        }
    }

    private int parsePercentFromMessage(Message message) {
        try {
            return Integer.parseInt(message.getText());
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Message text is not a valid integer.");
        }
    }

}
