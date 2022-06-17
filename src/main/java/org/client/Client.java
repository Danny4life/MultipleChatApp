package org.client;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class Client {
    private Socket s;

    private BufferedReader bufferedReader;

    private BufferedWriter bufferedWriter;

    private String userName;

    // create a constructor to instantiate every property


    public Client(Socket s, String userName) {
        try {
            this.s = s;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            this.userName = userName;
        } catch (IOException e) {
            closeEverything(s, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while (s.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(userName + ":" + " " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(s, bufferedReader, bufferedWriter);
        }
    }

    // create a method for listening from message from the server
    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;

                while (s.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        System.out.println(messageFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(s, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket s, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
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
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String userName = scanner.nextLine();
        Socket s = new Socket("localhost", 8080);
        Client client = new Client(s, userName);
        client.listenForMessage();
        client.sendMessage();
    }
}
