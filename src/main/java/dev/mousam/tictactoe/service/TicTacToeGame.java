package dev.mousam.tictactoe.service;

import dev.mousam.tictactoe.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;

@Service
public class TicTacToeGame {
    private final PlayerCache playerCache;
    private final BoardCache boardCache;

    @Autowired
    public TicTacToeGame(PlayerCache playerCache, BoardCache boardCache) {
        this.playerCache = playerCache;
        this.boardCache  = boardCache;
    }

    public ResponseEntity<?> createPlayer(String name) {
        if(this.playerCache.getPlayer(name) != null)
            return new ResponseEntity<>("Player name taken", HttpStatus.CONFLICT);
        this.playerCache.setPlayer(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> connect(String name, String opponentName) {
        Player player   = this.playerCache.getPlayer(name);
        if(opponentName.equals(player.getName()))
            return new ResponseEntity<>("Cannot connect with yourself", HttpStatus.IM_USED);
        Player opponent = this.playerCache.getPlayer(opponentName);
        if(opponent == null || opponent.opponent != null)
            return new ResponseEntity<>("Cannot connect", HttpStatus.IM_USED);
        //@Todo: Request opponent to play instead of forcing them.
        player.setOpponent(opponent);
        opponent.setOpponent(player);
        sendEvent("Connected with " + player.name, opponent.emitter);
        opponent.setPlayingPiece(new PlayingPieceX());
        player.setPlayingPiece(new PlayingPieceO());
        opponent.setTurn(true);
        player.setTurn(false);
        UUID boardId;
        do {
            boardId = UUID.randomUUID();
        }while(this.boardCache.isIdPresent(boardId));
        this.boardCache.getBoard(boardId);
        return new ResponseEntity<>(boardId.toString(), HttpStatus.OK);
    }

    public ResponseEntity<?> play(String playerName, String input, String boardId, SseEmitter emitter) {
        Player player   = this.playerCache.getPlayer(playerName);
        Player opponent = player.opponent;
        if(opponent == null)
            return new ResponseEntity<>("Cannot start game without opponent", HttpStatus.EXPECTATION_FAILED);
        String[] values = input.split(",");
        int row = Integer.parseInt(values[0]);
        int col = Integer.parseInt(values[1]);
        Board gameBoard = this.boardCache.getBoard(UUID.fromString(boardId));
        PlayingPiece piece = player.getTurn() ? player.getPlayingPiece() : opponent.getPlayingPiece();
        if(player.getTurn() && gameBoard.addPiece(row, col, piece)) {
            sendEvent(gameBoard, emitter);
            if(checkIfPlayerWon(row, col, piece.pieceType, gameBoard)) {
                this.boardCache.deleteBoard(UUID.fromString(boardId));
                emitter.complete();
                return new ResponseEntity<>((player.getTurn() ? playerName : opponent.name) + " won", HttpStatus.OK);
            }
            player.setTurn(!player.getTurn());
            opponent.setTurn(!opponent.getTurn());
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>((player.getTurn() ? player.name : opponent.name) + "'s turn", HttpStatus.OK);
    }

    public boolean checkIfPlayerWon(int row, int col, PieceType pieceType, Board gameBoard) {
        boolean rowMatch = true;
        boolean columnMatch = true;
        boolean diagonalMatch = true;
        boolean antiDiagonalMatch = true;
        //need to check in row
        for(int i=0;i<gameBoard.size;i++) {
            if(gameBoard.board[row][i] == null || gameBoard.board[row][i].pieceType != pieceType) {
                rowMatch = false;
                break;
            }
        }
        //need to check in column
        for(int i=0;i<gameBoard.size;i++) {
            if(gameBoard.board[i][col] == null || gameBoard.board[i][col].pieceType != pieceType) {
                columnMatch = false;
                break;
            }
        }
        //need to check diagonals
        for(int i=0, j=0; i<gameBoard.size;i++,j++) {
            if (gameBoard.board[i][j] == null || gameBoard.board[i][j].pieceType != pieceType) {
                diagonalMatch = false;
                break;
            }
        }
        //need to check anti-diagonals
        for(int i=0, j=gameBoard.size-1; i<gameBoard.size;i++,j--) {
            if (gameBoard.board[i][j] == null || gameBoard.board[i][j].pieceType != pieceType) {
                antiDiagonalMatch = false;
                break;
            }
        }
        return rowMatch || columnMatch || diagonalMatch || antiDiagonalMatch;
    }

    public void sendEvent(Object obj, SseEmitter emitter) {
        try {
            emitter.send(SseEmitter.event().data(obj));
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
