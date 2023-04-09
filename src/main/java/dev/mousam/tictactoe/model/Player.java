package dev.mousam.tictactoe.model;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class Player {
    public String name;
    public Player opponent;
    public PlayingPiece playingPiece;
    public SseEmitter emitter;
    public Boolean isTurn;

    public Player(PlayingPiece playingPiece, String name, SseEmitter emitter) {
        this.playingPiece = playingPiece;
        this.name = name;
        this.emitter = emitter;
    }

    public void setPlayingPiece(PlayingPiece playingPiece) {
        this.playingPiece = playingPiece;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public void setTurn(Boolean turn) {
        isTurn = turn;
    }

    public String getName() {
        return name;
    }

    public PlayingPiece getPlayingPiece() {
        return playingPiece;
    }

    public Boolean getTurn() {
        return isTurn;
    }
}
