package dev.mousam.tictactoe.model;

public class Board {
    public int size;
    public PlayingPiece[][] board;

    public Board(int size) {
        this.size = size;
        this.board = new PlayingPiece[size][size];
    }

    public boolean addPiece(int row, int col, PlayingPiece playingPiece) {
        if(row < 0 || row >= this.size || col < 0 || col >= this.size || this.board[row][col] != null) return false;
        if(playingPiece != null) board[row][col] = playingPiece;
        return true;
    }
}
