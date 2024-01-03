package com.example.phototeca.service.cryptocurrency.impl;

import com.example.phototeca.entity.cryptocurrency.Cryptocurrency;
import com.example.phototeca.entity.subscriber.Subscriber;
import com.example.phototeca.repository.cryptocurrency.CryptocurrencyRepository;
import com.example.phototeca.service.cryptocurrency.CryptocurrencyService;
import com.example.phototeca.service.subscriber.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptocurrencyServiceImpl implements CryptocurrencyService {

    private static final String API_URL = "https://api.mexc.com/api/v3/ticker/price";

    private final RestTemplate restTemplate;

    private final CryptocurrencyRepository cryptocurrencyRepository;

    private final SubscriberService subscriberService;


    //Possible use hibernate.jdbc.batch_size or use Stateless Session approach for saving very large number of entities etc...
    //find all data may be optimized
    @Override
    public void fetchAndSaveCryptocurrencyData() {
        ResponseEntity<Cryptocurrency[]> response = restTemplate.getForEntity(API_URL, Cryptocurrency[].class);
        List<Cryptocurrency> cryptocurrenciesNew = Arrays.asList(Objects.requireNonNull(response.getBody()));
        List<Cryptocurrency> cryptocurrenciesOld = cryptocurrencyRepository.findAll();

        if (cryptocurrenciesOld.size() == cryptocurrenciesNew.size()
                && new HashSet<>(cryptocurrenciesOld).containsAll(cryptocurrenciesNew)) {
            return;
        }

        checkCryptocurrencyUpdates(cryptocurrenciesNew, cryptocurrenciesOld);
    }

    public void checkCryptocurrencyUpdates(final List<Cryptocurrency> cryptocurrenciesNew,
                                           final List<Cryptocurrency> cryptocurrenciesOld) {
        // Convert old cryptocurrency data to a map for efficient lookup
        Map<String, Cryptocurrency> oldCryptosMap = cryptocurrenciesOld.stream()
                .collect(Collectors.toMap(Cryptocurrency::getSymbol, crypto -> crypto));

        List<Subscriber> subscribers = subscriberService.getAll();

        // Map to hold subscribers and the list of cryptocurrencies that meet their threshold
        Map<Subscriber, List<Cryptocurrency>> subscriberNotifications = new HashMap<>();

        // Check each new cryptocurrency
        for (Cryptocurrency newCrypto : cryptocurrenciesNew) {
            Cryptocurrency oldCrypto = oldCryptosMap.get(newCrypto.getSymbol());

            if (oldCrypto != null) {
                double priceChangePercent = calculatePercentageChange(oldCrypto.getPrice(), newCrypto.getPrice());
                oldCrypto.setPrice(newCrypto.getPrice()); // Update the price

                for (Subscriber subscriber : subscribers) {
                    if (Math.abs(priceChangePercent) >= subscriber.getThresholdPercentage()) {
                        subscriberNotifications.computeIfAbsent(subscriber, k -> new ArrayList<>()).add(newCrypto);
                    }
                }
            } else {
                oldCryptosMap.put(newCrypto.getSymbol(), newCrypto); // Add new cryptocurrency if not present
            }
        }

        // Save all updated and new cryptocurrencies to the database
        cryptocurrencyRepository.saveAll(oldCryptosMap.values());

        // Notify each subscriber with the list of cryptocurrencies that met their threshold
        subscriberNotifications.forEach(this::notifySubscriber);
    }

    private double calculatePercentageChange(final double oldPrice, final double newPrice) {
        return ((newPrice - oldPrice) / oldPrice) * 100.0;
    }

    private void notifySubscriber(final Subscriber subscriber, final List<Cryptocurrency> cryptocurrencies) {
        // do not have time to implement this, but it is quit simple to just notify subscriber
        // a parser can be written for a pretty message
    }

}

