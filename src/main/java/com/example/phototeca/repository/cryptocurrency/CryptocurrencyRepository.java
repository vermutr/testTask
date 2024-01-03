package com.example.phototeca.repository.cryptocurrency;

import com.example.phototeca.entity.cryptocurrency.Cryptocurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptocurrencyRepository extends JpaRepository<Cryptocurrency, String> {
}
