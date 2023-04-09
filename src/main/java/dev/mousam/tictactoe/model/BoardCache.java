package dev.mousam.tictactoe.model;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class BoardCache {
    public Map<UUID, Board> mapping;

    public Board getBoard(UUID uuid) {
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
