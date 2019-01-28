package Server;

import java.util.HashMap;

import Game.Board;
import Game.Constants;
import Game.History;
import Game.MoveValidator;
import static Game.EnforceRule.enforceRules;
import static Game.Score.getScore;


public class Game {
	Board board;
	ClientHandler[] players = new ClientHandler[2];
	HashMap<ClientHandler, Integer> player_Color;
	String status = Constants.PLAYING;
	int turn = Constants.BLACK;
	History history;
	int passed = 0;
	int gameid;
	boolean configured = false;
	boolean firstAssigned = false;
	boolean secondAssigned = false;
	boolean rematch = false;
	int rematchcount = 0;

	public Game(int gameid) {
		history = new History();
		player_Color = new HashMap<ClientHandler, Integer>();
		this.gameid = gameid;

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

	public ClientHandler getPlayer(int color) {
		if (getColor(players[0]) == color) {
			return players[0];
		} else {
			return players[1];
		}
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

	public int getLeftcolor() {
		if (getColor(players[0]) == Constants.BLACK) {
			return Constants.WHITE;
		} else {
			return Constants.BLACK;

		}
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
	
	public int getTurn() {
		return turn;
	}

	public int getPrevTurn() {
		if (turn == Constants.BLACK) {
			return Constants.WHITE;
		}
		if (turn == Constants.WHITE) {
			return Constants.BLACK;
		}
		return 0;
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
		enforceRules(board, getColor(player));
		history.addSituation(board.getStringRepresentation());
		changeTurn();
	}

	public void removePlayer(ClientHandler player) {
		int pointsWhite = getScore(board, Constants.WHITE);
		int pointsBlack = getScore(board, Constants.BLACK);
		getOpponent(player).sendMessage(Constants.GAME_FINISHED + Constants.DELIMITER + 1 + Constants.DELIMITER
				+ getOpponent(player).getPlayerName() + Constants.DELIMITER + 1 + ";" + pointsBlack + ";" + 2 + ";"
				+ pointsWhite + "Other player left, You wint!!");
	}

	public void HandleIncommingMesg(ClientHandler player, String message) {
		System.out.println(message);
		String[] messagesplit = message.split("\\" + Constants.DELIMITER);
		if (messagesplit[0].equals(Constants.HANDSHAKE) && secondAssigned != true) {
			if (!firstAssigned) {
				assignFirstPlayer(player, messagesplit);
				firstAssigned = true;
			} else {
				assignSecondPlayer(player, messagesplit);
				secondAssigned = true;
			}
		} else if (messagesplit[0].equals(Constants.SET_CONFIG)) {
			if (configured == false && player.equals(players[0])) {
				configureGame(messagesplit);
			}
		} else if (messagesplit[0].equals(Constants.MOVE)) {
			handleMove(player, messagesplit);
		} else if (messagesplit[0].equals(Constants.EXIT)) {
			removePlayer(player);
		} else if(messagesplit[0].equals(Constants.SET_REMATCH)) {
			setRematch(messagesplit[1]);

		} else {
			System.out.println(message);
		}
	}

	public synchronized void handleMove(ClientHandler player, String[] message) {
		int move = Integer.parseInt(message[3]);
		ClientHandler opponent = getOpponent(player);
		if (isTurn(player)) {
			if (isValidMove(move, player)) {
				if (move == -1) {
					passed++;
					System.out.println(passed);
					if (passed == 2) {
						finish();
						endGame();
					}
					else {
						changeTurn();
						sendAcknowledgeMove(player, move);
						sendAcknowledgeMove(opponent, move);
					}
				} else {
					playMove(move, player);
					sendAcknowledgeMove(player, move);
					sendAcknowledgeMove(opponent, move);
					System.out.println(board.toString());
					passed = 0;
				}
			} else {
				player.sendMessage(Constants.INVALID_MOVE + Constants.DELIMITER + "not a valid move!");
			}
		} else {
			player.sendMessage(Constants.INVALID_MOVE + Constants.DELIMITER + "Not your turn!");
		}
	}

	public void endGame() {
		sendGameOverMessage();
		sendNewGameRequest();
	}
	public void setRematch(String choice) {
		rematchcount++;
		if(choice.equals("1")) {
			rematch = true;
			System.out.println(true);
		} else {
			rematch = false;
		}
		if (rematchcount == 2) {
			if (rematch) {
				sendAcknowledgeRematch(1);
				resetGame();
			}else {
			sendAcknowledgeRematch(0);
			}
		}
	}

	public ClientHandler getWinner(int pointsWhite, int pointsBlack) {
		if (pointsWhite < pointsBlack) {
			return getPlayer(Constants.BLACK);
		} else {
			return getPlayer(Constants.WHITE);
		}
	}

	public void setPlayerName(ClientHandler player, String name) {
		player.setPlayerName(name);
	}

	public void assignFirstPlayer(ClientHandler player, String[] message) {
		setPlayerName(player, message[1]);
		sendAcknowledgeHandshake(player, gameid, 1);
		player.sendMessage(Constants.REQUEST_CONFIG + Constants.DELIMITER + Constants.REQUEST_CONFIG_MESSAGE);
		System.out.println(Constants.REQUEST_CONFIG + Constants.DELIMITER + Constants.REQUEST_CONFIG_MESSAGE);
	}

	public void assignSecondPlayer(ClientHandler player, String[] message) {
		setPlayerName(player, message[1]);
		sendAcknowledgeHandshake(player, gameid, 0);
		if (configured == true) {
			setColorPlayer(players[1], getLeftcolor());
			System.out.println("Sendacknowledgeconfig");
			sendAcknowledgeConfig();
		}
	}
	public void resetGame() {
		board = new Board(board.getSize());
		history = new History();
		turn = Constants.BLACK;
	}

	public void configureGame(String[] message) {
		int color = Integer.parseInt(message[2]);
		int size = Integer.parseInt(message[3]);
		System.out.println("color is:" + color);
		System.out.println("size is: " + size);
		setColorPlayer(players[0], color);
		board = new Board(size);
		System.out.println(board.toString());
		configured = true;
		if (secondAssigned) {
			setColorPlayer(players[1], getLeftcolor());
			System.out.println("Sendacknowledgeconfig");
			sendAcknowledgeConfig();
		}

	}

	public void sendGameOverMessage() {
		int pointsWhite = getScore(board, Constants.WHITE);
		int pointsBlack = getScore(board, Constants.BLACK);
		ClientHandler winner = getWinner(pointsWhite, pointsBlack);
		for (ClientHandler player : players) {
			player.sendMessage(Constants.GAME_FINISHED + Constants.DELIMITER + 1 + Constants.DELIMITER + winner.getPlayerName()
					+ Constants.DELIMITER + 1 + ";" + pointsBlack + ";" + 2 + ";" + pointsWhite + "");
		}
	}
	
	public void sendNewGameRequest() {
		for (ClientHandler player : players) {
			player.sendMessage(Constants.REQUEST_REMATCH);
		}
		
	}
	
	public void sendAcknowledgeRematch(int choice) {
		for (ClientHandler player : players) {
			player.sendMessage(Constants.ACKNOWLEDGE_REMATCH + Constants.DELIMITER + choice);
		}
		
	}

	public void sendAcknowledgeMove(ClientHandler player, int move) {
		System.out.println("color of player is:   "  + getColor(player));
		player.sendMessage(Constants.ACKNOWLEDGE_MOVE + Constants.DELIMITER + gameid + Constants.DELIMITER + move + ";"
				+ getPrevTurn()  + Constants.DELIMITER + getGameState());

	}

	public void sendAcknowledgeConfig() {
		for (ClientHandler player : players) {
			player.sendMessage(Constants.ACKNOWLEDGE_CONFIG + Constants.DELIMITER + player.getPlayerName()
					+ Constants.DELIMITER + getColor(player) + Constants.DELIMITER + getSize() + Constants.DELIMITER
					+ getGameState() + Constants.DELIMITER + getOpponent(player).getPlayerName());
		}
	}

	public void sendAcknowledgeHandshake(ClientHandler client, int gameid, int isLeader) {
		client.sendMessage(
				Constants.ACKNOWLEDGE_HANDSHAKE + Constants.DELIMITER + gameid + Constants.DELIMITER + isLeader);
	}
	


}
