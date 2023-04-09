package dev.mousam.tictactoe.model;

public class Player {
    public String name;
    public Player opponent;
    public PlayingPiece playingPiece;
    public Boolean isTurn;

    public Player(PlayingPiece playingPiece, String name) {
        this.playingPiece = playingPiece;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
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
