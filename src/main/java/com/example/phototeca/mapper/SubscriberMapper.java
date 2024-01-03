package com.example.phototeca.mapper;

import com.example.phototeca.entity.subscriber.Subscriber;

import java.time.LocalDateTime;

public class SubscriberMapper {

    private SubscriberMapper() {

    }

    public static Subscriber createSubscriber(final Long chatId, final Integer thresholdPercentage) {
        Subscriber subscriber = new Subscriber();
        subscriber.setChatId(chatId);
        subscriber.setThresholdPercentage(thresholdPercentage);
        subscriber.setCreateTime(LocalDateTime.now());
        return subscriber;
    }

}
