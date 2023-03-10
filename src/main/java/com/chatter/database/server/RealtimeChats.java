package com.chatter.database.server;

import com.chatter.database.DatabaseApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RealtimeChats implements Runnable {

    private static List<Client> clients = new ArrayList<>();

    @Override
    public void run() {
        for (int i = 10000; i < 60000; i++) {
            boolean isError = false;
            try {
                ServerSocket server = new ServerSocket(i);
                System.out.println("Server (Chats) port is: " + i);

                while (true) {
                    Socket socket = server.accept();
                    Client client = new Client(socket);
                    clients.add(client);
                    new Thread(client).start();
                }
            } catch (IOException e) {
                isError = true;
            }

            if (!isError) break;
        }
    }

    public void sendGlobalMessage(String users, String message) {
        clients.stream()
                .filter(it -> it.email.contains(users))
                .forEach(it -> it.sendMessage(message));
    }

    class Client implements Runnable {

        private final Socket socket;
        public String email;
        private BufferedReader in;
        private PrintWriter out;


        Client(Socket socket) {
            this.socket = socket;
        }


        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                email = in.readLine();

                detectMessage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void detectMessage() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    String[] split = message.split(" ");
                    if (split[0].equals("newMessage")) { // ***** [newMessage] [userEmails] *****
                        sendGlobalMessage(split[1], split[0]);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private void sendMessage(String message) {
            out.println(message);
        }
    }
}
