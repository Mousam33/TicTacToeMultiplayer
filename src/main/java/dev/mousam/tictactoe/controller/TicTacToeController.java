package dev.mousam.tictactoe.controller;

import dev.mousam.tictactoe.model.PlayerCache;
import dev.mousam.tictactoe.service.TicTacToeGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin
@RestController
@RequestMapping("/")
public class TicTacToeController {
    private final TicTacToeGame ticTacToeGame;
    private final Map<UUID, SseEmitter> subscribers;
    private final PlayerCache playerCache;

    @Autowired
    public TicTacToeController(TicTacToeGame ticTacToeGame, PlayerCache playerCache) {
        this.ticTacToeGame = ticTacToeGame;
        this.playerCache   = playerCache;
        this.subscribers   = new ConcurrentHashMap<>();
    }

    @GetMapping
    public ResponseEntity<?> createPlayer(@RequestParam String name) {
        if(name == null) return null;
        return ticTacToeGame.createPlayer(name);
    }

    @PatchMapping
    public ResponseEntity<?> connect(@RequestBody Map<String, String> params) {
        ResponseEntity<?> response = ticTacToeGame.connect(params.get("name"), params.get("opponent"));
        UUID boardId = UUID.fromString(response.getBody().toString());
        if(!this.subscribers.containsKey(boardId)) {
            SseEmitter emitter = new SseEmitter(-1L);
            this.subscribers.put(boardId, emitter);
            emitter.onCompletion(() -> this.subscribers.remove(boardId));
        }
        return response;
    }

    @GetMapping("/{player}")
    public SseEmitter subscribeToPlayerEvents(@PathVariable String player) {
        return this.playerCache.getPlayer(player).emitter;
    }

    @GetMapping("/lobby")
    public List<String> getUnassignedPlayers() {
        return this.playerCache.getUnassignedPlayers();
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam String boardId) {
        return this.subscribers.get(UUID.fromString(boardId));
    }

    @PostMapping
    public ResponseEntity<?> play(@RequestBody Map<String, String> params) {
        return ticTacToeGame.play(params.get("name"),
                params.get("input"),
                params.get("boardId"),
                this.subscribers.getOrDefault(UUID.fromString(params.get("boardId")), null));
    }
}