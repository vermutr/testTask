package com.example.phototeca.service.subscriber;

import com.example.phototeca.entity.subscriber.Subscriber;

import java.util.List;

public interface SubscriberService {

    void save(final Subscriber subscriber);

    long countSubscribers();

    void deleteSubscriber(final Long chatId);

    List<Subscriber> getAll();

}
