package Server;

import static Game.Score.getScore;

import Game.Constants;
import Game.Game;

public class MessageSender {
	private GameHandler gamehandler;
	public MessageSender(GameHandler gamehandler) {
		this.gamehandler = gamehandler;
	}



	public void sendGameOverMessage() {
		int pointsWhite = getScore(gamehandler.getGame().getBoard(), Constants.WHITE);
		int pointsBlack = getScore(gamehandler.getGame().getBoard(), Constants.BLACK);
		ClientHandler winner = gamehandler.getWinner(pointsWhite, pointsBlack);
		for (ClientHandler player : gamehandler.getPlayers()) {
			player.sendMessage(Constants.GAME_FINISHED + Constants.DELIMITER + gamehandler.getGame().getid() + Constants.DELIMITER + winner.getPlayerName()
					+ Constants.DELIMITER + pointsBlack + ";"  + pointsWhite + "" + Constants.DELIMITER  + "game finished"); //AANPASSEN
		}
	}
	
	public void sendNewGameRequest() {
		for (ClientHandler player : gamehandler.getPlayers()) {
			player.sendMessage(Constants.REQUEST_REMATCH);
		}
		
	}
	
	public void sendAcknowledgeRematch(int choice) {
		for (ClientHandler player : gamehandler.getPlayers()) {
			player.sendMessage(Constants.ACKNOWLEDGE_REMATCH + Constants.DELIMITER + choice);
		}
		
	}

	public void sendAcknowledgeMove(ClientHandler player, int move) {
		player.sendMessage(Constants.ACKNOWLEDGE_MOVE + Constants.DELIMITER + gamehandler.getGame().getid() + Constants.DELIMITER + move + ";"
				+ gamehandler.getGame().getPrevTurn()  + Constants.DELIMITER + gamehandler.getGameState());

	}

	public void sendAcknowledgeConfig(int size) {
		for (ClientHandler player : gamehandler.getPlayers()) {
			player.sendMessage(Constants.ACKNOWLEDGE_CONFIG + Constants.DELIMITER + player.getPlayerName()
					+ Constants.DELIMITER + gamehandler.getColor(player) + Constants.DELIMITER + gamehandler.getGame().getSize() + Constants.DELIMITER
					+ gamehandler.getGameState() + Constants.DELIMITER + gamehandler.getOpponent(player).getPlayerName());
		}
	}
	
	public void sendUnknownCommand(ClientHandler player) {
		player.sendMessage(Constants.UNKNOWN_COMMAND + "is unkown command");
		
	}

	public void sendAcknowledgeHandshake(ClientHandler player, int isLeader) {
		player.sendMessage(
				Constants.ACKNOWLEDGE_HANDSHAKE + Constants.DELIMITER + gamehandler.getGame().getid() + Constants.DELIMITER + isLeader);
	}
	
	public void sendInvalidMove(ClientHandler player, String text) {
		player.sendMessage(Constants.INVALID_MOVE + Constants.DELIMITER + text);
	}
	
	public void sendExitMessage(ClientHandler player) {
		int pointsWhite = getScore(gamehandler.getGame().getBoard(), Constants.WHITE);
		int pointsBlack = getScore(gamehandler.getGame().getBoard(), Constants.BLACK);
		gamehandler.getOpponent(player).sendMessage(Constants.GAME_FINISHED + Constants.DELIMITER + gamehandler.getGame().getid() + Constants.DELIMITER
				+ gamehandler.getOpponent(player).getPlayerName() + Constants.DELIMITER  + pointsBlack + pointsWhite + "Other player left, You wint!!");	
	}
	
	public void sendRequestConfig(ClientHandler player) {
		player.sendMessage(Constants.REQUEST_CONFIG + Constants.DELIMITER + Constants.REQUEST_CONFIG_MESSAGE);
		
	}
	
	

}
