package dev.mousam.tictactoe.controller;

import dev.mousam.tictactoe.service.TicTacToeGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/")
public class TicTacToeController {
    private final TicTacToeGame ticTacToeGame;

    @Autowired
    public TicTacToeController(TicTacToeGame ticTacToeGame) {
        this.ticTacToeGame = ticTacToeGame;
    }

    @GetMapping
    public ResponseEntity<?> createPlayer(@RequestParam String name) {
        return ticTacToeGame.createPlayer(name);
    }

    @PatchMapping
    public ResponseEntity<?> connect(@RequestBody Map<String, String> params) {
        return ticTacToeGame.connect(params.get("name"), params.get("opponent"));
    }

    @PostMapping
    public ResponseEntity<?> play(@RequestBody Map<String, String> params) {
        return ticTacToeGame.play(params.get("name"), params.get("input"), params.get("boardId"));
    }
}