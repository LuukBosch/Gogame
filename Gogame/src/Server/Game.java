package Server;

import java.util.HashMap;

import Game.Board;
import Game.Constants;
import Game.History;
import Game.MoveValidator;
import static Game.EnforceRule.enforceRules;
import static Game.Score.getScore;
/**
 * The game class is responsible for the gameflow and enforcing the rules of a game of go.
 * Two clienthandlers are assigned to this game by the server and the game is responsible for
 * communicating with these clients. 
 * @author luuk.bosch
 *
 */


public class Game {
	
	private Board board;
	private ClientHandler[] players = new ClientHandler[2];
	private HashMap<ClientHandler, Integer> player_Color;
	private String status = Constants.PLAYING;
	private History history;
	
	private int passed = 0;
	private int gameid;
	private int rematchcount = 0;
	private int turn = Constants.BLACK;
	
	private boolean configured = false;
	private boolean firstAssigned = false;
	private boolean secondAssigned = false;
	private boolean rematch = false;

	
	/**
	 * Creates a new game with a specific gameid
	 * @param gameid Gameid specific for this game. 
	 */
	public Game(int gameid) {
		history = new History();
		player_Color = new HashMap<ClientHandler, Integer>();
		this.gameid = gameid;

	}
	
	
	/**
	 * adds the first player that is assigned by the server
	 * @param player ClientHandler 
	 */
	public void addFirstPlayer(ClientHandler player) {
		players[0] = player;
	}
	
	/**
	 * adds the second player that is assigned by the server
	 * @param player ClientHandler 
	 */
	public void addSecondPlayer(ClientHandler player) {
		players[1] = player;
	}
	
	/**
	 * Sets a playername. 
	 * @param player Player that needs to be named.
	 * @param name player name
	 */
	public void setPlayerName(ClientHandler player, String name) {
		player.setPlayerName(name);
	}


	/**
	 * Returns the color of a given player.
	 * @param player Clienthandler
	 * @return returns color of the player.
	 */
	public int getColor(ClientHandler player) {
		return player_Color.get(player);
	}

	
	/**
	 * Returns the player with the given color
	 * @param color
	 * @return Player corresponding to the given color
	 */
	public ClientHandler getPlayer(int color) {
		if (getColor(players[0]) == color) {
			return players[0];
		} else {
			return players[1];
		}
	}

	/**
	 * Returns the opponent of a given player
	 * @param player Clienthandler
	 * @return opponent of given player
	 */
	public ClientHandler getOpponent(ClientHandler player) {
		if (player == players[0]) {
			return players[1];
		} else
			return players[0];
	}

	/**
	 * Returns the color that is not yet occupied by a player
	 * @return color that is not occupied
	 */
	public int getLeftcolor() {
		if (getColor(players[0]) == Constants.BLACK) {
			return Constants.WHITE;
		} else {
			return Constants.BLACK;

		}
	}
	
	/**
	 * Returns the board.
	 * @return Board of the game
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Returns the size of the board. 
	 * @return size
	 */
	public int getSize() {
		return board.getSize();
	}
	
	/**
	 * Returns the color that is to move.
	 * @return color of the player that is to move
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Returns the color that did the previous move. 
	 * @return color of the player that did the previous move
	 */
	public int getPrevTurn() {
		if (turn == Constants.BLACK) {
			return Constants.WHITE;
		}
		if (turn == Constants.WHITE) {
			return Constants.BLACK;
		}
		return 0;
	}
	
	/**
	 * Assigns a color to a player
	 * @param player Clienthandeler
	 * @param color color
	 */
	public void setColorPlayer(ClientHandler player, int color) {
		player_Color.put(player, color);
	}

	/**
	 * Changes status to finished. 
	 */
	public void finish() {
		status = Constants.FINISHED;
	}

	/**
	 * Changes turn. 
	 */
	public void changeTurn() {
		if (turn == Constants.WHITE) {
			turn = Constants.BLACK;
		} else if (turn == Constants.BLACK) {
			turn = Constants.WHITE;
		}
	}	
	
	/**
	 * checks if given color is to move. 
	 * @param player Clienthandler
	 * @return if player is to move or not
	 */
	public boolean isTurn(ClientHandler player) {
		return turn == player_Color.get(player);
	}


	/**
	 * Checks if a move suggested by a player is valid or not. 
	 * @param move
	 * @param player
	 * @return if voldig move or not
	 */
	public boolean isValidMove(int move, ClientHandler player) {
		return MoveValidator.isValidMove(board, move, getColor(player), history);
	}

	
	
	/**
	 * Removes player from the game after disconnect or exit request. 
	 * @param player
	 */

	public void removePlayer(ClientHandler player) {
		int pointsWhite = getScore(board, Constants.WHITE);
		int pointsBlack = getScore(board, Constants.BLACK);
		getOpponent(player).sendMessage(Constants.GAME_FINISHED + Constants.DELIMITER + gameid + Constants.DELIMITER
				+ getOpponent(player).getPlayerName() + Constants.DELIMITER  + pointsBlack + pointsWhite + "Other player left, You wint!!");
	}

	/**
	 * Handles all the incoming messages of the clients.
	 * Only messages obeying the protocol are processed. 
	 * @param player ClientHandeler sending the message
	 * @param message Message containing information from the client
	 */
	public void HandleIncommingMesg(ClientHandler player, String message) {
		try {
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
			System.out.println(message + "is not a valid message");
		}
		} catch (NumberFormatException e) {
			System.out.println("Incomming message is not in the right format");
		}
	}
	
	/**
	 * Handles the incoming move. Checks if it is the player his turn, if the move is valid and 
	 * if it is a pass. The results the function depends on the parameter stated before. 
	 * @param player The player that wants to play the move
	 * @param message	message containing the move index
	 */

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
					passed = 0;
				}
			} else {
				player.sendMessage(Constants.INVALID_MOVE + Constants.DELIMITER + "not a valid move!");
			}
		} else {
			player.sendMessage(Constants.INVALID_MOVE + Constants.DELIMITER + "Not your turn!");
		}
	}
	
	/**
	 * Plays a move on the board. Enforces the rules, adds board position to history 
	 * and changes turn. 
	 * @param move
	 * @param player
	 */
	public void playMove(int move, ClientHandler player) {
		int col = move % board.getSize();
		int row = (move - col) / board.getSize();
		board.setField(row, col, getColor(player));
		enforceRules(board, getColor(player));
		history.addSituation(board.getStringRepresentation());
		changeTurn();
	}

	/**
	 * calls SendGameoverMessage and SendNewGameRequest
	 */
	
	public void endGame() {
		sendGameOverMessage();
		sendNewGameRequest();
	}
	
	
	/**
	 * Checks if both players have answered an responds by calling the correct functions 
	 * @param choice
	 */
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
				resetGame();
				sendAcknowledgeRematch(1);
				sendAcknowledgeConfig();

			}else {
			sendAcknowledgeRematch(0);
			}
		}
	}
	
	/**
	 * Returns a winner based upon the scores
	 * @param pointsWhite points scored by white
	 * @param pointsBlack points scored by black
	 * @return
	 */

	public ClientHandler getWinner(int pointsWhite, int pointsBlack) {
		if (pointsWhite < pointsBlack) {
			return getPlayer(Constants.BLACK);
		} else {
			return getPlayer(Constants.WHITE);
		}
	}

	

	/**
	 * Assigns first player and send Request_config message
	 * @param player Clienthandler that send the message.
	 * @param message message containing player name.
	 */
	public void assignFirstPlayer(ClientHandler player, String[] message) {
		setPlayerName(player, message[1]);
		sendAcknowledgeHandshake(player, gameid, 1);
		player.sendMessage(Constants.REQUEST_CONFIG + Constants.DELIMITER + Constants.REQUEST_CONFIG_MESSAGE);
	}

	
	/**
	 * Assigns second player
	 * @param player Clienthandler that send the message.
	 * @param message containing player name.
	 */
	public void assignSecondPlayer(ClientHandler player, String[] message) {
		setPlayerName(player, message[1]);
		sendAcknowledgeHandshake(player, gameid, 0);
		if (configured == true) {
			setColorPlayer(players[1], getLeftcolor());
			sendAcknowledgeConfig();
		}
	}
	
	/**
	 * Resets the Game
	 */
	public void resetGame() {
		board = new Board(board.getSize());
		history = new History();
		turn = Constants.BLACK;
		status = Constants.PLAYING;
		
	}

	
	/**
	 * Configures the game based upon the message send by the client
	 * @param message contains information about the game configuration
	 */
	public void configureGame(String[] message) {
		int color = Integer.parseInt(message[2]);
		int size = Integer.parseInt(message[3]);
		setColorPlayer(players[0], color);
		board = new Board(size);
		configured = true;
		if (secondAssigned) {
			setColorPlayer(players[1], getLeftcolor());
			sendAcknowledgeConfig();
		}

	}
	/**
	 * Returns the game state. agreed upon in the protocol. 
	 * @return
	 */
	public String getGameState() {
		return status + ";" + turn + ";" + board.getStringRepresentation();

	}
	
	
	//----- Sendmessage functions

	public void sendGameOverMessage() {
		int pointsWhite = getScore(board, Constants.WHITE);
		int pointsBlack = getScore(board, Constants.BLACK);
		ClientHandler winner = getWinner(pointsWhite, pointsBlack);
		for (ClientHandler player : players) {
			player.sendMessage(Constants.GAME_FINISHED + Constants.DELIMITER + gameid + Constants.DELIMITER + winner.getPlayerName()
					+ Constants.DELIMITER + pointsBlack + ";"  + pointsWhite + ""); //AANPASSEN
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
