package org;

import org.server.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        // create a server socket object with a port number which listens to clients passing information to the port number
        ServerSocket serverSocket = new ServerSocket(8080);
        Server server = new Server(serverSocket);
        // to keep our server running
        server.startServer();

    }
}
