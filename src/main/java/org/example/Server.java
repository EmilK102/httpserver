package org.example;

import java.net.*;
import java.io.IOException;

public class Server {
    private int port;
    private String directory;

    public Server(int port, String directory) {
        this.port = port;
        this.directory = directory;
    }

    void start(){
        try (var server = new ServerSocket(this.port)) {
            while (true){
                var socket = server.accept();
                var threads = new Heandler(socket, this.directory);
                threads.start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DataBaseConnection();
        new Server(8080, "/home/e2kvampyre/IdeaProjects/jdbs_test/files").start();
    }
}


