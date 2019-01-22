package Server;

import java.util.HashMap;

import Game.Board;
import Game.Constants;
import Game.EnforceRule;
import Game.History;
import Game.MoveValidator;
import Game.Player;

public class Game {
	Board board;
	ClientHandler[] players = new ClientHandler[2];
	HashMap<ClientHandler, Integer> player_Color;
	String status = Constants.WAITING;
	int turn = Constants.BLACK;
	History history;
	
	public Game(int size) {
		board = new Board(size);
		history = new History();
	}
	
	public void addFirstPlayer(ClientHandler player, int color) {
		players[0] = player;
		player_Color.put(player, color);
	}
	public void addSecondPlayer(ClientHandler player) {
		players[1] = player;
		if(player_Color.get(players[0]) == Constants.BLACK) {
			player_Color.put(player, Constants.WHITE);
		} else {
			player_Color.put(player, Constants.BLACK);
		}
		status = Constants.PLAYING;
	}

	
	public int getColor(ClientHandler player) {
		return player_Color.get(player);
	}
	
	public ClientHandler getOpponent(ClientHandler player) {
		if(player == players[0]) {
			return players[1];
		} else
			return players[0];
		}
	
	public int getOpponentColor(ClientHandler player) {
		return getColor(getOpponent(player));
	}
	
	public Board getBoard() {
		return board;
	}
	public int getSize() {
		return board.getSize();
	}
	public void finish() {
		status = Constants.FINISHED;
	}
	
	public void changeTurn() {
		if(turn == Constants.WHITE) {
			turn = Constants.BLACK;
		} else if(turn == Constants.BLACK) {
			turn = Constants.WHITE;
		}
	}
	
	public boolean isTurn(ClientHandler player) {
		return turn == player_Color.get(player);
	}
	
	public String getGameState() {
		return  status+";"+turn+";"+board.getStringRepresentation();
		
	}
	
	public boolean isValidMove(int move, ClientHandler player) {
		return MoveValidator.isValidMove(board, move, getColor(player), history);
	}
	
	public void playMove(int move, ClientHandler player) {
		int col = move%board.getSize();
		int row = (move-col)/board.getSize();
		board.setField(row, col, getColor(player));
		EnforceRule.apply(board, getColor(player));
		changeTurn();
	}

}
