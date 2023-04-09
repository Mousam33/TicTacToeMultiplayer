package dev.mousam.tictactoe.model;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlayerCache {
    public Map<String, Player> mapping;

    public PlayerCache() {
        this.mapping = new HashMap<>();
    }

    public void setPlayer(String name) {
        Player newPlayer = new Player(new PlayingPieceO(), name);
        this.mapping.put(name, newPlayer);
    }

    public Player getPlayer(String name) {
        return this.mapping.getOrDefault(name, null);
    }

}