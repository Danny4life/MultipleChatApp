package org.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// server class is responsible for listening to client who which to connect

public class Server {
    // create a serversocket object that is responsible for listening to in-coming connection from client
    private ServerSocket serverSocket;
    // create a constructor to set up our server socket

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    // create a method startServer that is responsible for keeping our server running
    public void startServer() {
        // we want our server continuously running
        try {
            while (!serverSocket.isClosed()) {
                // wait for a client to connect using the accept method
                Socket s = serverSocket.accept();
                System.out.println("A new user has connected");
                ClientHandler clientHandler = new ClientHandler(s);

                // create a thread object to spawn new threads
                Thread thread = new Thread(clientHandler);
                // use the start method to run the thread
                thread.start();

            }
        } catch (IOException e) {
            closeServer();
        }
    }

    // create a method for handling errors to avoid nested try/catches
    public void closeServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
