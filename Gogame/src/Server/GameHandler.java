package Server;

import java.util.HashMap;
import Game.Constants;
import Game.Game;
import static Game.Score.getScore;
/**
 * The game class is responsible for the gameflow and enforcing the rules of a game of go.
 * Two clienthandlers are assigned to this game by the server and the game is responsible for
 * communicating with these clients. 
 * @author luuk.bosch
 *
 */
public class GameHandler {
	
	private ClientHandler[] players = new ClientHandler[2];
	private HashMap<ClientHandler, Integer> player_Color;
	private String status = Constants.PLAYING;
	private Game game;
	private MessageSender messageSender;
	
	private int rematchcount = 0;
	
	private boolean configured = false;
	private boolean firstAssigned = false;
	private boolean secondAssigned = false;
	private boolean rematch = false;

	
	/**
	 * Creates a new game with a specific gameid
	 * @param gameid Gameid specific for this game. 
	 */
	public GameHandler(int gameid) {
		game = new Game(gameid);
		messageSender = new MessageSender(this);
		player_Color = new HashMap<ClientHandler, Integer>(); 
	}
	
	public Game getGame() {
		return game;
	}
	
	public ClientHandler[] getPlayers() {
		return players;
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

	public ClientHandler getWinner(int pointsWhite, int pointsBlack) {
		if (pointsWhite < pointsBlack) {
			return getPlayer(Constants.BLACK);
		} else {
			return getPlayer(Constants.WHITE);
		}
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
	 * Assigns a color to a player
	 * @param player Clienthandeler
	 * @param color color
	 */
	public void setColorPlayer(ClientHandler player, int color) {
		player_Color.put(player, color);
	}


	
	/**
	 * Handles all the incoming messages of the clients.
	 * Only messages obeying the protocol are processed. 
	 * @param player ClientHandeler sending the message
	 * @param message Message containing information from the client
	 */
	public void HandleIncommingMesg(ClientHandler player, String message) {
        String[] splitmessage = message.split("\\+");
        String cmd = splitmessage[0];
        System.out.println(cmd);

        switch (cmd) {
            case "HANDSHAKE":
                handleHandshake(splitmessage, player);
                break;
            case "SET_CONFIG":
            	handleConfig(splitmessage, player);
                break;
            case "MOVE":
            	handleMove(splitmessage, player);
                break;
            case "EXIT":
            	handleExit(player);
                break;
            case "SET_REMATCH":
            	handleRematch(splitmessage);
                break;
            default:
               unvalidInput(player);
        }
    }
	
	public void handleHandshake(String[] input, ClientHandler player) {
		if (!firstAssigned) {
			assignFirstPlayer(player, input);
			firstAssigned = true;
		} else if (!secondAssigned) {
			assignSecondPlayer(player, input);
			secondAssigned = true;
		} else {
			unvalidInput(player);
		}

	}
	
	public void handleConfig(String[] input, ClientHandler player) {
		if (configured == false && player.equals(players[0])) {
			configureGame(input);
		}
	}
	


	public void handleMove(String[] input, ClientHandler player) {
		int move = Integer.parseInt(input[3]);
		int color = getColor(player);
		int response = game.handleMove(color, move);
		if(response == 1) {
			endGame();
		} else if(response == 2) {
			messageSender.sendAcknowledgeMove(player, move);
			messageSender.sendAcknowledgeMove(getOpponent(player), move);
		} else if(response == 3) {
			messageSender.sendInvalidMove(player, "not a valid move!");
		} else if(response == 4) {
			messageSender.sendInvalidMove(player, "Not your turn!");
		}
	}
	
	
	/**
	 * Removes player from the game after disconnect or exit request. 
	 * @param player
	 */

	public void handleExit(ClientHandler player) {
		messageSender.sendExitMessage(player);
	}
	
	public void handleRematch(String[] input) {
		String choice = input[1];
		rematchcount++;
		if(choice.equals("1")) {
			rematch = true;
			System.out.println(true);
		} else {
			rematch = false;
		}
		if (rematchcount == 2) {
			if (rematch) {
				game.resetGame();
				messageSender.sendAcknowledgeRematch(1);
				messageSender.sendAcknowledgeConfig(game.getBoard().getSize());

			}else {
				messageSender.sendAcknowledgeRematch(0);
			}
		}
	}
	
	
	public void unvalidInput(ClientHandler player) {
		messageSender.sendUnknownCommand(player);
	}
	/**
	 * calls SendGameoverMessage and SendNewGameRequest
	 */
	public void endGame() {
		messageSender.sendGameOverMessage();
		messageSender.sendNewGameRequest();
	}


	/**
	 * Assigns first player and send Request_config message
	 * @param player Clienthandler that send the message.
	 * @param message message containing player name.
	 */
	public void assignFirstPlayer(ClientHandler player, String[] message) {
		setPlayerName(player, message[1]);
		messageSender.sendAcknowledgeHandshake(player, 1);
		messageSender.sendRequestConfig(player);
	}

	
	/**
	 * Assigns second player
	 * @param player Clienthandler that send the message.
	 * @param message containing player name.
	 */
	public void assignSecondPlayer(ClientHandler player, String[] message) {
		setPlayerName(player, message[1]);
		messageSender.sendAcknowledgeHandshake(player, 0);
		if (configured == true) {
			setColorPlayer(players[1], getLeftcolor());
			messageSender.sendAcknowledgeConfig(game.getBoard().getSize());
		}
	}
	

	/**
	 * Configures the game based upon the message send by the client
	 * @param message contains information about the game configuration
	 */
	public void configureGame(String[] message) {
		int color = Integer.parseInt(message[2]);
		int size = Integer.parseInt(message[3]);
		setColorPlayer(players[0], color);
		game.setBoard(size);
		configured = true;
		if (secondAssigned) {
			setColorPlayer(players[1], getLeftcolor());
			messageSender.sendAcknowledgeConfig(size);
		}

	}
	/**
	 * Returns the game state. agreed upon in the protocol. 
	 * @return
	 */
	public String getGameState() {
		return status + ";" + game.getTurn() + ";" + game.getBoard().getStringRepresentation();

	}
	


}
