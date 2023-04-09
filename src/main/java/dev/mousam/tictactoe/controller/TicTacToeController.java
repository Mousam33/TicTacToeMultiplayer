package dev.mousam.tictactoe.controller;

import dev.mousam.tictactoe.service.TicTacToeGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/")
public class TicTacToeController {
    private final TicTacToeGame ticTacToeGame;
    private final Map<UUID, SseEmitter> subscribers;

    @Autowired
    public TicTacToeController(TicTacToeGame ticTacToeGame) {
        this.ticTacToeGame = ticTacToeGame;
        this.subscribers   = new ConcurrentHashMap<>();
    }

    @GetMapping
    public ResponseEntity<?> createPlayer(@RequestParam String name) {
        return ticTacToeGame.createPlayer(name);
    }

    @PatchMapping
    public ResponseEntity<?> connect(@RequestBody Map<String, String> params) {
        ResponseEntity<?> response = ticTacToeGame.connect(params.get("name"), params.get("opponent"));
        UUID boardId = UUID.fromString(response.getBody().toString());
        if(!this.subscribers.containsKey(boardId)) {
            SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
            this.subscribers.put(boardId, emitter);
            emitter.onCompletion(() -> this.subscribers.remove(boardId));
        }
        return response;
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