package dev.mousam.tictactoe.model;

public class Board {
    public int size;
    public int emptyCells;
    public PlayingPiece[][] board;

    public Board(int size) {
        this.size = size;
        this.board = new PlayingPiece[size][size];
        this.emptyCells = size * size;
    }

    public boolean addPiece(int row, int col, PlayingPiece playingPiece) {
        if(row < 0 || row >= this.size || col < 0 || col >= this.size || this.board[row][col] != null || playingPiece == null)
            return false;
        this.board[row][col] = playingPiece;
        this.emptyCells--;
        return true;
    }

    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] != null) {
                    System.out.print(board[i][j].pieceType.name() + "   ");
                } else {
                    System.out.print("    ");

                }
                System.out.print(" | ");
            }
            System.out.println();

        }
    }

}
