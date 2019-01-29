package Tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.portable.InputStream;

import Client.Client;
import Game.Board;
import Game.Intersection;
import Server.Server;

public class GameFlowtest {
	Server server;
	Client client;
  
    	
  	
    
    @Test
    public void Test() {
    	server = new Server();
    	//Enter 8000, entering false ports result in another prompt
    	assertEquals(8000, server.getPort());
    	
    	client = new Client();
 
     }
    
    
    

}
