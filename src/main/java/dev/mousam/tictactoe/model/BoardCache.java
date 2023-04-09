package dev.mousam.tictactoe.model;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BoardCache {
    public Map<UUID, Board> mapping;

    public BoardCache() {
        this.mapping = new ConcurrentHashMap<>();
    }

    public Board getBoard(UUID uuid) {
        if(!this.mapping.containsKey(uuid))
            this.mapping.put(uuid, new Board(3));
        return this.mapping.get(uuid);
    }

    public void deleteBoard(UUID uuid) {
        if(!this.mapping.containsKey(uuid)) return;
        this.mapping.remove(uuid);
    }

    public Boolean isIdPresent(UUID uuid) {
        return this.mapping.containsKey(uuid);
    }
}
