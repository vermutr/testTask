package com.example.phototeca.repository.subscriber;

import com.example.phototeca.entity.subscriber.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

}
