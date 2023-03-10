package com.chatter.database.server;

import com.chatter.database.DatabaseApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RealtimeMessages implements Runnable {

    List<Client> clients = new ArrayList<>();

    @Override
    public void run() {
        for (int i = 10000; i < 60000; i++) {
            boolean isError = false;
            try {
                ServerSocket server = new ServerSocket(i);
                System.out.println("Server (Messages) port is: " + i);

                while (true) {
                    Socket socket = server.accept();
                    Client client = new Client(socket);
                    clients.add(client);
                    new Thread(client).start();
                }
            } catch (Exception ignore) {
                isError = true;
            }

            if (!isError) break;
        }
    }

    void sendChatMessage(long chatID, String message) {
        for (Client client : clients) {
            if (client.chatID == chatID) client.sendMessage(message);
        }
    }

    class Client implements Runnable {

        private final Socket socket;

        private BufferedReader in;
        private PrintWriter out;
        long chatID;
        String email;

        Client(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                getClientInfo();
                checkUpdates();
            } catch (Exception ignore) {}
        }

        private void getClientInfo() {
            try {
                chatID = Long.parseLong(in.readLine());
                email = in.readLine();
                out.println("done");
                System.out.println("Client |" + socket.getInetAddress() + "| connect successful -> |" + chatID + "| & |" + email + "|");
            } catch (Exception ignore) {}
        }

        void sendMessage(String message) {
            out.println(message);
        }

        void checkUpdates() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.equals("disconnect")) {
                        in.close();
                        out.close();
                        socket.close();
                    }

                    String[] split = message.split(" "); // [chatID] [option (newMessage)] [message]
                    System.out.println("Client |" + socket.getInetAddress() + "| send -> |" + split[0] + "| & |" + split[1] + "| & |" + split[2] + "|");
                    sendChatMessage(Long.parseLong(split[0]), split[1]);
                }
            } catch (Exception ignore) {
                System.out.println("Client |" + socket.getInetAddress() + "| disconnect");
            }
        }
    }
}
