package Game;
/**
 * Contains the constants present in the protocol. 
 * @author luuk.bosch
 *
 */
public class Constants {
	
public static final int EMPTY = 0;
public static final int BLACK = 1;
public static final int WHITE = 2;
public static final int REMOVED = 99;
public static final String HANDSHAKE = "HANDSHAKE";
public static final String MOVE = "MOVE";
public static final String REQUEST_CONFIG = "REQUEST_CONFIG";
public static final String REQUEST_CONFIG_MESSAGE = "Please  provide a preferred configuration";
public static final String ACKNOWLEDGE_HANDSHAKE = "ACKNOWLEDGE_HANDSHAKE";
public static final String SET_CONFIG = "SET_CONFIG";
public static final String ACKNOWLEDGE_CONFIG = "ACKNOWLEDGE_CONFIG";
public static final String UPDATE_STATUS = "UPDATE_STATUS";
public static final String DELIMITER = "+";
public static final String EXIT = "EXIT";
public static final String WAITING = "WAITING"; 
public static final String PLAYING = "PLAYING";
public static final String GAME_FINISHED = "GAME_FINISHED";
public static final String FINISHED = "FINISHED";
public static final String INVALID_MOVE = "INVALID_MOVE";
public static final String ACKNOWLEDGE_MOVE = "ACKNOWLEDGE_MOVE";
public static final String REQUEST_REMATCH = "REQUEST_REMATCH";
public static final String SET_REMATCH = "SET_REMATCH";
public static final String ACKNOWLEDGE_REMATCH = "AKNOWLEDGE_REMATCH";
public static final String UNKNOWN_COMMAND = "UNKOWN_COMMAND";

}
