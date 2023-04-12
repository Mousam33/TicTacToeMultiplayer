package dev.mousam.tictactoe.model;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class PlayerCache {
    public Map<String, Player> mapping;

    public PlayerCache() {
        this.mapping = new ConcurrentHashMap<>();
    }

    public void setPlayer(String name) {
        if(!this.mapping.containsKey(name)) {
            Player newPlayer = new Player(new PlayingPieceO(), name, new SseEmitter(-1L));
            this.mapping.put(name, newPlayer);
        }
    }

    public Player getPlayer(String name) {
        return this.mapping.getOrDefault(name, null);
    }

    public List<String> getUnassignedPlayers() {
        return this.mapping.values().stream()
                .filter(x -> x.opponent == null)
                .map(Player::getName).collect(Collectors.toList());
    }

    public void remove(String name) {
        this.mapping.remove(name);
    }

}
