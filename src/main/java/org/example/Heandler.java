package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Heandler extends Thread{

    private static final String MESSAGE_NOT_FOUND = "NOT FOUND";
    private static final Map<String,String> CONTENT_TYPES = new HashMap<>(){{
        put("html", "text/html");
        put("txt", "text/html");
        put("","text/plain");
    }};
    private Socket socket;
    private String directory;

    public Heandler(Socket socket, String directory) {
        this.socket = socket;
        this.directory = directory;
    }

    @Override
    public void run() {
        try(var input = this.socket.getInputStream(); var output = this.socket.getOutputStream()){
            var url = this.getRequestUrl(input);
            var filePath = Path.of(this.directory + url);
            if(Files.exists(filePath) && !Files.isDirectory(filePath)){
                var extension = this.getFileExtension(filePath);
                var type = CONTENT_TYPES.get(extension);
                var fileBytes = Files.readAllBytes(filePath);
                this.sendHeader(output,200, "OK", type, fileBytes.length);
                output.write(fileBytes);
            }
            else {
                var type = CONTENT_TYPES.get("text");
                this.sendHeader(output, 404, "Not found", type, MESSAGE_NOT_FOUND.length());
                output.write(MESSAGE_NOT_FOUND.getBytes());
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    private String getRequestUrl(InputStream input){
        var reader = new Scanner(input);
        var line = reader.nextLine();
        return line.split(" ")[1];
    }

    private String getFileExtension(Path path){
        var name = path.getFileName().toString();
        var extensionStart = name.lastIndexOf(".");
        return extensionStart == -1 ? "": name.substring(extensionStart + 1);
    }

    private void sendHeader(OutputStream output,int statusCode, String statusText, String type, long length){
        var ps = new PrintStream(output);
        ps.printf("HTTP/1.1 %s %s%n", statusCode, statusText);
        ps.printf("Content-Type: %s%n", type);
        ps.printf("Content-length: %s%n%n", length);
    }
}
