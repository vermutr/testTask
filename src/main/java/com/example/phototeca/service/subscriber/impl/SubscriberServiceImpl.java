package com.example.phototeca.service.subscriber.impl;

import com.example.phototeca.entity.subscriber.Subscriber;
import com.example.phototeca.repository.subscriber.SubscriberRepository;
import com.example.phototeca.service.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;


    @Override
    @Transactional
    public void save(final Subscriber subscriber) {
        subscriberRepository.save(subscriber);
    }

    @Override
    public long countSubscribers() {
        return subscriberRepository.count();
    }

    @Override
    @Transactional
    public void deleteSubscriber(final Long chatId) {
        subscriberRepository.deleteById(chatId);
    }

    @Override
    public List<Subscriber> getAll() {
        return subscriberRepository.findAll();
    }

}
