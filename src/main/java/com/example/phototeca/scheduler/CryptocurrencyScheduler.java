package com.example.phototeca.scheduler;

import com.example.phototeca.service.cryptocurrency.CryptocurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CryptocurrencyScheduler {

    private final CryptocurrencyService cryptocurrencyService;


    @Scheduled(cron = "${api.phototeca.cron.save.cryptocurrency.task:*/5 * * * *}")
    public void fetchAndSaveTask() {
        cryptocurrencyService.fetchAndSaveCryptocurrencyData();
    }

}
