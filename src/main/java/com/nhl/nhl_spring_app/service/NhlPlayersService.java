package com.nhl.nhl_spring_app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhl.nhl_spring_app.model.NhlPlayer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class NhlPlayersService {
    private final ObjectMapper objectMapper;

    public NhlPlayersService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<NhlPlayer> getPlayers() {
        try (InputStream in = new ClassPathResource("data/nhl_players.json").getInputStream()) {
            return objectMapper.readValue(in, new TypeReference<List<NhlPlayer>>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao ler data/nhl_players.json", e);
        }
    }
}

