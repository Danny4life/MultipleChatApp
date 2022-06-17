package org.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    // create a static array list of every client handler object that we have instantiated
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    // use the socket to establish connection between the client and the server
    Socket s;
    // use buffer reader to read data that has been sent from the client
    BufferedReader bufferedReader;
    // use buffer writer to send data back to client
    BufferedWriter bufferedWriter;
    // create a client user to represent each client
    private String clientUser;

    public ClientHandler(Socket s) {
        try {
            this.s = s;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.clientUser = bufferedReader.readLine();
            // add the client to the array list so they can be part of the group chat and receive messages
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUser + " " + "has entered the chat");

        } catch (IOException e) {
            closeEverything(s, bufferedReader, bufferedWriter);
        }
    }

    @Override
    // everything here will be run on a separate thread
    public void run() {
        // get a string variable to hold message from client
        String clientMessage;

        while (s.isConnected()) {
                try {
                    //listen to message from client
                    clientMessage = bufferedReader.readLine();
                    broadcastMessage(clientMessage);
                } catch (IOException e) {
                    closeEverything(s, bufferedReader, bufferedWriter);
                    break;
                }
        }
    }
    public void broadcastMessage(String messageToSend) {
        // loop through our arraylist
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUser.equals(clientUser)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    // use flush to reduce the messages sent
                    clientHandler.bufferedWriter.flush();

                }
            } catch (IOException e) {
                closeEverything(s, bufferedReader, bufferedWriter);
            }
        }
    }
    // create method to show user has left the chat
    public void removeClientHandler() {
        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUser + " " + "has left the chat");
    }

    public void closeEverything(Socket s, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
            removeClientHandler();
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (s != null) {
                    s.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
