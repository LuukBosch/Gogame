package Server;

import java.util.HashMap;

import Game.Board;
import Game.Constants;
import Game.EnforceRule;
import Game.History;
import Game.MoveValidator;
import Game.Player;

public class Game extends Thread {
	Board board;
	ClientHandler[] players = new ClientHandler[2];
	HashMap<ClientHandler, Integer> player_Color;
	String status = Constants.WAITING;
	int turn = Constants.BLACK;
	History history;
	int passed = 0;
	boolean configured = false;

	public Game() {
		history = new History();
		player_Color = new HashMap<ClientHandler, Integer>();
	}

	public void addPlayer(ClientHandler player) {
		if (players[0] != null) {
			players[0] = player;
		} else {
			players[1] = player;
		}
	}

	public void addFirstPlayer(ClientHandler player) {
		players[0] = player;
	}

	public void addSecondPlayer(ClientHandler player) {
		players[1] = player;
	}

	public int getColor(ClientHandler player) {
		return player_Color.get(player);
	}

	public ClientHandler getOpponent(ClientHandler player) {
		if (player == players[0]) {
			return players[1];
		} else
			return players[0];
	}

	public int getOpponentColor(ClientHandler player) {
		return getColor(getOpponent(player));
	}

	public void setColorPlayer(ClientHandler player, int color) {
		player_Color.put(player, color);
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
		if (turn == Constants.WHITE) {
			turn = Constants.BLACK;
		} else if (turn == Constants.BLACK) {
			turn = Constants.WHITE;
		}
	}

	public boolean isTurn(ClientHandler player) {
		return turn == player_Color.get(player);
	}

	public String getGameState() {
		return status + ";" + turn + ";" + board.getStringRepresentation();

	}

	public boolean isValidMove(int move, ClientHandler player) {
		return MoveValidator.isValidMove(board, move, getColor(player), history);
	}

	public void playMove(int move, ClientHandler player) {
		int col = move % board.getSize();
		int row = (move - col) / board.getSize();
		board.setField(row, col, getColor(player));
		EnforceRule.apply(board, getColor(player));
		changeTurn();
	}

	public void RemovePlayer(ClientHandler player) {

	}

	public void HandleIncommingMesg(ClientHandler client, String message) {
		System.out.println(message);
		String[] messagesplit = message.split("\\" + Constants.DELIMITER);
		if (messagesplit[0].equals(Constants.HANDSHAKE)) {
			if (players[0] != null || players[1] != null) {
				String name = messagesplit[1];
				client.setPlayerName(name);
				if (players[0] == null) {
					assignFirstPlayer(client);
				} else {
					assignSecondPlayer(client);
				}
			}
		} else if (messagesplit[0].equals(Constants.SEND_CONFIG)) {
			if (configured == false && client.equals(players[0])) {
				int color = Integer.parseInt(messagesplit[1]);
				int size = Integer.parseInt(messagesplit[2]);
				configureGame(color, size);
			}
		} else if (messagesplit[0].equals(Constants.MOVE)) {
			handleMove(client, messagesplit);
		} else if (messagesplit[0].equals(Constants.EXIT)) {

		} else {
			System.out.println(message);
		}
	}

	public void handleMove(ClientHandler client, String[] message) {
		int move = Integer.parseInt(message[3]);
		if (isTurn(client)) {
			if (isValidMove(move, client)) {
				if (move == -1) {
					passed++;
					System.out.println("Player passed!");
					changeTurn();
					if(passed ==2) {
						finish();
						endGame();
					} else {
						client.sendMessage(Constants.ACKNOWLEDGE_MOVE + Constants.DELIMITER + message[1] + Constants.DELIMITER
								+ move +";"+ getColor(client) + Constants.DELIMITER + getGameState());
						ClientHandler opponent = getOpponent(client);
						opponent.sendMessage(Constants.ACKNOWLEDGE_MOVE + Constants.DELIMITER + message[1] + Constants.DELIMITER
								+ move +";"+ getColor(opponent)+ Constants.DELIMITER + getGameState());
					}
				} else {
				playMove(move, client);
				client.sendMessage(Constants.ACKNOWLEDGE_MOVE + Constants.DELIMITER + message[1] + Constants.DELIMITER
						+ move +";"+ getColor(client) + Constants.DELIMITER + getGameState());
				ClientHandler opponent = getOpponent(client);
				opponent.sendMessage(Constants.ACKNOWLEDGE_MOVE + Constants.DELIMITER + message[1] + Constants.DELIMITER
						+ move +";"+ getColor(opponent)+ Constants.DELIMITER + getGameState());
				passed = 0;
				}
			} else {
				client.sendMessage(Constants.INVALID_MOVE + "not a valid move!");
			}
		} else {
			client.sendMessage(Constants.INVALID_MOVE + "Not your turn!");
		}
		// TODO sent confirmation!
	}
	
	public void endGame() {
		players[0].sendMessage("Game over!");
		players[1].sendMessage("Game over!");
	}

	public void assignFirstPlayer(ClientHandler player) {
		sendAcknowledgeHandshake(player, 1, true);
		player.sendMessage(Constants.REQUEST_CONFIG + Constants.DELIMITER + Constants.REQUEST_CONFIG_MESSAGE);
		addFirstPlayer(player);

	}

	public void assignSecondPlayer(ClientHandler player) {
		addSecondPlayer(player);
		sendAcknowledgeHandshake(player, 1, false);
		if (configured == true) {
			setColorPlayer(players[1], 2); /////// TODO invullen!!!!!!!!!!!!!!!!!!!!
			AcknowledgeConfig();
		}
	}
	

	public void configureGame(int color, int size) {
		setColorPlayer(players[0], color);
		board = new Board(size);
		configured = true;

	}

	public void AcknowledgeConfig() {
		for (ClientHandler player : players) {
			player.sendMessage(Constants.ACKNOWLEDGE_CONFIG + Constants.DELIMITER + player.getPlayerName()
					+ Constants.DELIMITER + getColor(player) + Constants.DELIMITER + getSize() + Constants.DELIMITER
					+ getGameState() + Constants.DELIMITER + getOpponent(player).getPlayerName());
		}
	}

	public void sendAcknowledgeHandshake(ClientHandler client, int gameid, boolean isleader) {
		client.sendMessage(
				Constants.ACKNOWLEDGE_HANDSHAKE + Constants.DELIMITER + gameid + Constants.DELIMITER + isleader);
	}

}
